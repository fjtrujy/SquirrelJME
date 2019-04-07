// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

import dev.shadowtail.classfile.nncc.NearNativeByteCodeHandler;
import dev.shadowtail.classfile.xlate.ByteCodeProcessor;
import java.util.Collection;
import java.util.Iterator;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.collections.UnmodifiableIterator;

/**
 * This is similar to {@link ByteCode} except that it instead of using a
 * stack for intermediate Java operations, this instead uses registers. This
 * provides a more concise and easier to use format by virtual machines.
 *
 * @see ByteCode
 * @since 2019/03/09
 */
public final class RegisterCode
	implements Iterable<RegisterInstruction>
{
	/** The translation method to use. */
	public static final TranslationMethod USED_METHOD;
	
	/** Instructions for this code. */
	private final RegisterInstruction[] _instructions;
	
	/** Line number table. */
	private final short[] _lines;
	
	/**
	 * Initializes the translation method.
	 *
	 * @since 2019/04/03
	 */
	static
	{
		// Get from system property
		TranslationMethod use = null;
		try
		{
			// {@squirreljme.property cc.squirreljme.register.method=method
			// This specifies the method to use when translating the byte code
			// to register code.		 (quick; experimental)}
			String val = System.getProperty("cc.squirreljme.register.method");
			if (val != null)
				try
				{
					use = TranslationMethod.valueOf(val.toUpperCase());
				}
				catch (IllegalArgumentException e)
				{
				}
		}
		catch (SecurityException e)
		{
		}
		
		// Set
		USED_METHOD = (use == null ? TranslationMethod.DEFAULT : use);
	}
	
	/**
	 * Initializes the register code.
	 *
	 * @param __i The associated instructions.
	 * @param __l The lines for each instruction.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/22
	 */
	public RegisterCode(RegisterInstruction[] __i, short[] __l)
		throws NullPointerException
	{
		__i = (__i == null ? new RegisterInstruction[0] : __i.clone());
		for (RegisterInstruction i : __i)
			if (i == null)
				throw new NullPointerException("NARG");
		
		this._instructions = __i;
		this._lines = (__l == null ? new short[0] : __l.clone());
	}
	
	/**
	 * Initializes the register code.
	 *
	 * @param __i The associated instructions.
	 * @param __l The lines for each instruction.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/22
	 */
	public RegisterCode(Collection<RegisterInstruction> __i, short[] __l)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		RegisterInstruction[] ii = __i.<RegisterInstruction>toArray(
			new RegisterInstruction[__i.size()]);
		for (RegisterInstruction i : ii)
			if (i == null)
				throw new NullPointerException("NARG");
		
		this._instructions = ii;
		this._lines = (__l == null ? new short[0] : __l.clone());
	}
	
	/**
	 * Gets the instruction at this index.
	 *
	 * @param __dx The index to get.
	 * @return The register at this index.
	 * @since 2019/03/26
	 */
	public final RegisterInstruction get(int __dx)
	{
		return this._instructions[__dx];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/23
	 */
	@Override
	public final Iterator<RegisterInstruction> iterator()
	{
		return UnmodifiableIterator.<RegisterInstruction>of(
			this._instructions);
	}
	
	/**
	 * Returns the length of the register code.
	 *
	 * @return The register code length.
	 * @since 2019/03/26
	 */
	public final int length()
	{
		return this._instructions.length;
	}
	
	/**
	 * The line number table.
	 *
	 * @return The line number table.
	 * @since 2018/03/24
	 */
	public final short[] lines()
	{
		return this._lines.clone();
	}
	
	/**
	 * This translates the input byte code and creates a register code which
	 * removes all stack operations and maps them to register operations.
	 *
	 * @param __bc The input byte code.
	 * @return The resulting register code.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/09
	 */
	public static final RegisterCode of(ByteCode __bc)
		throws NullPointerException
	{
		return RegisterCode.of(__bc, TranslationMethod.DEFAULT);
	}
	
	/**
	 * This translates the input byte code and creates a register code which
	 * removes all stack operations and maps them to register operations. A
	 * specific translator is used.
	 *
	 * @param __bc The input byte code.
	 * @param __tm The translator to use.
	 * @return The resulting register code.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/03
	 */
	public static final RegisterCode of(ByteCode __bc, TranslationMethod __tm)
		throws NullPointerException
	{
		if (__bc == null || __tm == null)
			throw new NullPointerException("NARG");
		
		NearNativeByteCodeHandler nnbc = new NearNativeByteCodeHandler();
		new ByteCodeProcessor(__bc, nnbc).process();
		
		if (true)
			throw new todo.TODO();
		
		return __tm.translator(__bc).convert();
	}
}

