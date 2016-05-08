// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.codeparse;

import net.multiphasicapps.narf.classinterface.NCIByteBuffer;
import net.multiphasicapps.narf.classinterface.NCIClass;
import net.multiphasicapps.narf.classinterface.NCICodeAttribute;
import net.multiphasicapps.narf.classinterface.NCILookup;
import net.multiphasicapps.narf.classinterface.NCIMethod;
import net.multiphasicapps.narf.classinterface.NCIPool;
import net.multiphasicapps.narf.program.NRBasicBlock;
import net.multiphasicapps.narf.program.NRProgram;
import net.multiphasicapps.util.boxed.BoxedIntegerList;

/**
 * This class is given a method which is then parsed.
 *
 * @since 2016/04/20
 */
public class NCPCodeParser
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The library for class lookup (optimization). */
	protected final NCILookup lookup;
	
	/** The containing class. */
	protected final NCIClass outerclass;
	
	/** The constant pool. */
	protected final NCIPool constantpool;
	
	/** The method to parse. */
	protected final NCIMethod method;
	
	/** The code attribute. */
	protected final NCICodeAttribute code;
	
	/** The actual code. */
	protected final NCIByteBuffer actual;
	
	/** Has this work been done already? */
	private volatile boolean _did;
	
	/** Operation positions. */
	private volatile int[] _opos;
	
	/**
	 * Initializes the code parser.
	 *
	 * @param __lib The lookup to find class definitions (used for final
	 * optimizations and such).
	 * @param __m The method to parse.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/27
	 */
	public NCPCodeParser(NCILookup __lib, NCIMethod __m)
		throws NullPointerException
	{
		// Check
		if (__lib == null || __m == null)
			throw new NullPointerException("NARG");
		
		// Set
		lookup = __lib;
		method = __m;
		outerclass = __m.outerClass();
		constantpool = outerclass.constantPool();
		code = __m.code();
		actual = code.code();
	}
	
	/**
	 * Parses and returns the resultant program.
	 *
	 * @return The decoded program.
	 * @since 2016/04/27
	 */
	public NRProgram get()
	{
		// Lock
		synchronized (lock)
		{
			// {@squirreljme.error ND01 Program parsing is being or has already
			// been performed.}
			if (_did)
				throw new IllegalStateException("ND01");
			_did = true;
		}
		
		// Local cache
		NCICodeAttribute code = this.code;
		NCIByteBuffer actual = this.actual;
		
		// Calculation all the operation positions
		int[] opos = new __OpPositions__(actual).get();
		_opos = opos;
		
		throw new Error("TODO");
	}
}

