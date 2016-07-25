// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.emulator;

/**
 * This is a flag which is used to determine how playback is to be performed
 * on the emulator.
 *
 * @since 2016/07/25
 */
public enum EmulatorPlayFlag
{
	/**
	 * When the end of the recorded input stream is reached continue with
	 * emulation instead of terminating the emulator.
	 */
	CONTINUE,
	
	/** End. */
	;
}

