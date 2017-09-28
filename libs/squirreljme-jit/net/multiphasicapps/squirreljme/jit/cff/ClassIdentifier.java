// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.cff;

/**
 * This is used to identify the fragment of a class or package.
 *
 * @since 2017/09/27
 */
public final class ClassIdentifier
	extends Identifier
{
	/**
	 * Initializes the class name.
	 *
	 * @param __s The class name.
	 * @since 2017/09/27
	 */
	public ClassIdentifier(String __s)
	{
		super(__s);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/27
	 */
	@Override
	public boolean equals(Object __o)
	{
		return (__o instanceof ClassIdentifier) && super.equals(__o);
	}
}

