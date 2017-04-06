// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import net.multiphasicapps.squirreljme.executable.ExecutableClass;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.linkage.ClassExport;
import net.multiphasicapps.squirreljme.linkage.ClassExtendsLink;
import net.multiphasicapps.squirreljme.linkage.ClassFlags;
import net.multiphasicapps.squirreljme.linkage.ClassImplementsLink;
import net.multiphasicapps.squirreljme.linkage.FieldFlags;
import net.multiphasicapps.squirreljme.linkage.MethodFlags;

/**
 * This is the part of the JIT which accepts a class file which is parsed and
 * then recompiled to native machine code as it is being processed.
 *
 * @since 2017/04/02
 */
public final class JIT
{
	/** The magic number of the class file. */
	private static final int _MAGIC_NUMBER =
		0xCAFEBABE;
	
	/** The input class file stream. */
	protected final DataInputStream input;
	
	/** The configuration for the JIT. */
	protected final JITConfig config;
	
	/** Link table for the parsed class. */
	protected final LinkTable linktable =
		new LinkTable();
	
	/** The export of this class. */
	private volatile ClassExport _thisexport;
	
	/**
	 * Initializes the JIT processor.
	 *
	 * @param __is The input class file to process.
	 * @param __conf The configuration for the JIT.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/02
	 */
	JIT(DataInputStream __is, JITConfig __conf)
		throws NullPointerException
	{
		// Check
		if (__is == null || __conf == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.input = __is;
		this.config = __conf;
	}
	
	/**
	 * Runs the JIT.
	 *
	 * @return The resulting executable generated by the JIT.
	 * @throws IOException On read errors.
	 * @throws JITException If compilation failed.
	 * @since 2017/04/02
	 */
	public ExecutableClass run()
		throws IOException, JITException
	{
		DataInputStream input = this.input;
		
		// {@squirreljme.error AQ0b The magic number of the input data stream
		// does not match that of the Java class file. (The magic number which
		// was read)}
		int fail;
		if ((fail = input.readInt()) != _MAGIC_NUMBER)
			throw new JITException(String.format("AQ0b %08x", fail));
		
		// {@squirreljme.error AQ0c The version number of the input class file
		// is not valid. (The version number)}
		int cver = input.readShort() | (input.readShort() << 16);
		ClassVersion version = ClassVersion.findVersion(cver);
		if (version == null)
			throw new JITException(String.format("AQ0c %d.%d",
				cver >>> 16, (cver & 0xFFFF)));
		
		// Parse the constant pool
		__Pool__ pool = new __Pool__(input);
		
		// Read class flags and the name
		ClassFlags clflags = __FlagDecoder__.__class(
			input.readUnsignedShort());
		ClassNameSymbol thisname = pool.get(input.readUnsignedShort()).
			<ClassNameSymbol>get(true, ClassNameSymbol.class);
		
		// Create initial export
		LinkTable linktable = this.linktable;
		ClassExport thisexport;
		linktable.export((thisexport = new ClassExport(thisname, clflags)));
		this._thisexport = thisexport;
		
		// {@squirreljme.error AQ0p A superclass was not specified and this
		// class is not the Object class, or a superclass was specified and
		// this is the object class.}
		ClassNameSymbol supername = pool.get(input.readUnsignedShort()).
			<ClassNameSymbol>optional(true, ClassNameSymbol.class);
		if ((supername == null) !=
			(thisname.equals(ClassNameSymbol.of("java/lang/Object"))))
			throw new JITException("AQ0p");
		
		// Link that
		linktable.link(new ClassExtendsLink(thisexport, supername));
		
		// Handle interfaces
		int n = input.readUnsignedShort();
		for (int i = 0, hi = 0; i < n; i++)
		{
			// Read class name
			ClassNameSymbol iname = pool.get(input.readUnsignedShort()).
				<ClassNameSymbol>get(true, ClassNameSymbol.class);
			
			// {@squirreljme.error AQ0r Duplicate implementation of an
			// interface. (The interface being linked)}
			ClassImplementsLink link;
			int lid = linktable.link(
				(link = new ClassImplementsLink(thisexport, iname)));
			if (lid <= hi)
				throw new JITException(String.format("AQ0r %s", link));
			
			// Set higher value
			if (lid > hi)
				hi = lid;
		}
		
		// Read fields
		n = input.readUnsignedShort();
		for (int i = 0; i < n; i++)
		{
			// Read flags
			FieldFlags ff = __FlagDecoder__.__field(clflags,
				input.readUnsignedShort());
			
			throw new todo.TODO();
		}
		
		// Read methods
		n = input.readUnsignedShort();
		for (int i = 0; i < n; i++)
		{
			// Read methods
			MethodFlags mf = __FlagDecoder__.__method(clflags,
				input.readUnsignedShort());
			
			throw new todo.TODO();
		}
		
		// Ignore attributes
		if (true)
			throw new todo.TODO();
		
		// Generate final executable
		throw new todo.TODO();
	}
}

