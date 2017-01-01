// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classformat;

/**
 * This represents the verification state of local variables.
 *
 * @since 2016/08/28
 */
class __SMTLocals__
	extends __SMTTread__
{
	/**
	 * Initializes the local variable types.
	 *
	 * @param __n The number of local variables used.
	 * @since 2016/05/12
	 */
	__SMTLocals__(int __n)
	{
		super(__n);
	}
	
	/**
	 * Initializes local variable state from an existing one.
	 *
	 * @param __l The state to copy from.
	 * @since 2016/08/29
	 */
	__SMTLocals__(__SMTLocals__ __l)
	{
		super(__l);
	}
}

