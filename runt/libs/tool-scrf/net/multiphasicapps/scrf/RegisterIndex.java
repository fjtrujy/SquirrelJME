// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents an index to a register.
 *
 * @since 2019/02/05
 */
@Deprecated
public final class RegisterIndex
	implements Comparable<RegisterIndex>
{
	/** The register index. */
	protected final int index;
	
	/** The string representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the index.
	 *
	 * @param __i The index of the register.
	 * @since 2019/02/05
	 */
	public RegisterIndex(int __i)
	{
		this.index = __i;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/02/05
	 */
	@Override
	public final int compareTo(RegisterIndex __o)
	{
		return this.index - __o.index;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/02/05
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof RegisterIndex))
			return false;
		
		return this.index == ((RegisterIndex)__o).index;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/02/06
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<String>((rv = String.format(
			"I@%d", this.index)));
		
		return rv;
	}
}

