// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.meep.lui;

/**
 * This stores the character data for {@link Display}s.
 *
 * @since 2016/09/11
 */
public final class DisplayScreen
{
	/** The number of columns to display. */
	protected final int columns;
	
	/** The number of rows to display. */
	protected final int rows;
	
	/** Notifier for display update events. */
	protected final DisplayUpdateNotifier notifier;
	
	/**
	 * Initializes the display screen.
	 *
	 * @param __n The notifier to call when the display detects that characters
	 * have changed. This parameter is optional.
	 * @param __c The number of columns to display.
	 * @param __r The number of rows to display.
	 * @throws IndexOutOfBoundsException If the number of columns and/or rows
	 * is zero or negative.
	 * @since 2016/09/11
	 */
	public DisplayScreen(DisplayUpdateNotifier __n, int __c, int __r)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error DA04 Cannot create a display which uses a
		// zero or negative number of columns and/or rows.}
		if (__c <= 0 || __r <= 0)
			throw new IndexOutOfBoundsException("DA04");
		
		// Set
		this.notifier = __n;
		this.columns = __c;
		this.rows = __r;
	}
}

