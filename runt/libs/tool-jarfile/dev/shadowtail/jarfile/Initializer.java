// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

import cc.squirreljme.runtime.cldc.vki.Allocator;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is used to build the initialization sequence accordingly. It is used
 * determine the initial amount of memory needed along with all the various
 * actions which need to be performed at this point.
 *
 * The initializer starts with a memory sequence chunk which could later be
 * freed when it is no longer needed potentially.
 *
 * @since 2019/04/30
 */
public final class Initializer
{
	/** Operations. */
	private final List<Operation> _ops =
		new ArrayList<>();
	
	/** Current allocated temporary space. */
	private byte[] _bytes = new byte[65536];
	
	/** Current size of the initializer, includes mem link for freeing. */
	private int _size =
		8;
	
	/**
	 * Allocates memory in the initialization sequence.
	 *
	 * @param __sz The number of bytes to allocate.
	 * @return The pointer address of the allocation.
	 * @since 2019/04/30
	 */
	public final int allocate(int __sz)
	{
		// Round allocation to 4-bytes
		__sz = (__sz + 3) & (~3);
		
		// Calculate the next size of the boot area
		int nowsize = this._size,
			nextsize = nowsize + __sz;
		
		// If the memory space is too small, grow it
		byte[] bytes = this._bytes;
		if (nextsize > bytes.length)
			this._bytes = (bytes = Arrays.copyOf(bytes, nextsize + 2048));
		
		// Debug
		todo.DEBUG.note("%d + %d => %d", nowsize, __sz, nextsize);
		
		// Continue at the end
		this._size = nextsize;
		return nowsize;
	}
	
	/**
	 * Writes a value to the given address.
	 *
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @since 2019/04/30
	 */
	public final void memWriteByte(int __addr, int __v)
	{
		this.memWriteByte(null, __addr, __v);
	}
	
	/**
	 * Writes a value to the given address.
	 *
	 * @param __m The modifier to use when writing.
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @since 2019/04/30
	 */
	public final void memWriteByte(Modifier __m, int __addr, int __v)
	{
		// Record action?
		if (__m != null && __m != Modifier.NONE)
			this._ops.add(new Operation(__m, 1, __addr));
		
		// Write data
		byte[] bytes = this._bytes;
		bytes[__addr] = (byte)__v;
	}
	
	/**
	 * Writes a value to the given address.
	 *
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @since 2019/04/30
	 */
	public final void memWriteInt(int __addr, int __v)
	{
		this.memWriteInt(null, __addr, __v);
	}
	
	/**
	 * Writes a value to the given address.
	 *
	 * @param __m The modifier to use when writing.
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @since 2019/04/30
	 */
	public final void memWriteInt(Modifier __m, int __addr, int __v)
	{
		// Record action?
		if (__m != null && __m != Modifier.NONE)
			this._ops.add(new Operation(__m, 4, __addr));
		
		// Write data
		byte[] bytes = this._bytes;
		bytes[__addr++] = (byte)(__v >>> 24);
		bytes[__addr++] = (byte)(__v >>> 16);
		bytes[__addr++] = (byte)(__v >>> 8);
		bytes[__addr++] = (byte)(__v);
	}
	
	/**
	 * Writes a value to the given address.
	 *
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @since 2019/04/30
	 */
	public final void memWriteShort(int __addr, int __v)
	{
		this.memWriteShort(null, __addr, __v);
	}
	
	/**
	 * Writes a value to the given address.
	 *
	 * @param __m The modifier to use when writing.
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @since 2019/04/30
	 */
	public final void memWriteShort(Modifier __m, int __addr, int __v)
	{
		// Record action?
		if (__m != null && __m != Modifier.NONE)
			this._ops.add(new Operation(__m, 2, __addr));
		
		// Write data
		byte[] bytes = this._bytes;
		bytes[__addr++] = (byte)(__v >>> 8);
		bytes[__addr++] = (byte)(__v);
	}
	
	/**
	 * Converts and builds the initializer sequence.
	 *
	 * @return The byte array representing the sequence.
	 * @since 2019/04/30
	 */
	public final byte[] toByteArray()
	{
		List<Operation> ops = this._ops;
		byte[] bytes = this._bytes;
		int size = this._size;
		
		// The initializer memory is actually a chunk of allocated memory so
		// store the block information for usage.
		this.memWriteInt(null, Allocator.OFF_MEMPART_SIZE,
			size);
		this.memWriteInt(Modifier.RAM_OFFSET, Allocator.OFF_MEMPART_NEXT,
			size);
		
		// Write initializer RAM
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
			DataOutputStream dos = new DataOutputStream(baos))
		{
			// Write initializer memory chunk
			dos.writeInt(size);
			dos.write(bytes);
			
			// Write out operations
			int n = ops.size();
			dos.writeInt(n);
			for (int i = 0; i < n; i++)
			{
				Operation op = ops.get(i);
				
				// Write operation tag and address offset
				dos.writeByte((op.size << 4) | (op.mod.ordinal()));
				dos.writeInt(op.addr);
			}
			
			// Done!
			return baos.toByteArray();
		}
		
		// {@squirreljme.error BC02 Could not export boot area.}
		catch (IOException e)
		{
			throw new RuntimeException("BC02", e);
		}
	}
}
