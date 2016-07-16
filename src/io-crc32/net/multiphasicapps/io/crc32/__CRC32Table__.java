// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.crc32;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This contains the table used for calculation of the CRC.
 *
 * @since 2016/07/16
 */
final class __CRC32Table__
{
	/** Static table reference. */
	private static volatile Reference<__CRC32Table__> _TABLE;
	
	/**
	 * Initializes the table data.
	 *
	 * @since 2016/07/16
	 */
	private __CRC32Table__()
	{
	}
	
	/**
	 * Obtains the CRC table.
	 *
	 * @return The CRC table.
	 * @since 2016/07/16
	 */
	static final __CRC32Table__ __table()
	{
		throw new Error("TODO");
	}
}

