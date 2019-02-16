// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf.compiler;

import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.Method;
import net.multiphasicapps.classfile.StackMapTable;
import net.multiphasicapps.scrf.RegisterCode;
import net.multiphasicapps.scrf.RegisterMethod;

/**
 * This processor is used to process input methods and their byte code to a
 * format which solely uses registers instead of the Java stack.
 *
 * @since 2019/01/19
 */
@Deprecated
public final class MethodProcessor
{
	/** The method to process. */
	protected final Method input;
	
	/** The VTable to reference into. */
	protected final VTableBuilder vtable;
	
	/**
	 * Initializes the processor.
	 *
	 * @param __v The VTable used for linkage information.
	 * @param __m The method to process.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/19
	 */
	private MethodProcessor(VTableBuilder __v, Method __m)
		throws NullPointerException
	{
		if (__v == null || __m == null)
			throw new NullPointerException("NARG");
		
		this.vtable = __v;
		this.input = __m;
	}
	
	/**
	 * Processes the method.
	 *
	 * @return The processed method.
	 * @throws ClassProcessException If the method could not be processed.
	 * @since 2019/01/19
	 */
	public final RegisterMethod process()
		throws ClassProcessException
	{
		Method input = this.input;
		VTableBuilder vtable = this.vtable;
		
		// Process the byte code of the method
		ByteCode bc = input.byteCode();
		RegisterCode rc = null;
		if (bc != null)
			rc = ByteCodeProcessor.process(this, bc);
		
		throw new todo.TODO();
	}
	
	/**
	 * Processes the given method and compiles it into register form.
	 *
	 * @param __v The VTable used for linkage information.
	 * @param __m The method to process.
	 * @return The compiled register method.
	 * @throws ClassProcessException If the method could not be processed.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/19
	 */
	public static final RegisterMethod process(VTableBuilder __v, Method __m)
		throws ClassProcessException, NullPointerException
	{
		if (__v == null || __m == null)
			throw new NullPointerException("NARG");
		
		return new MethodProcessor(__v, __m).process();
	}
}

