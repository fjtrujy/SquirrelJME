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

/**
 * This is used to serialize and deserialize the configurations for the JIT.
 *
 * This is needed by the kernel to re-initialize the JIT settings from the one
 * that was used at kernel compile time.
 *
 * @param <C> The configuration class used.
 * @since 2017/02/02
 */
public abstract class JITConfigSerializer<C extends JITConfig<C>>
{
	/**
	 * De-serializes the specified string and returns a new configuration
	 * that is compatible with the given configuration.
	 *
	 * @param __v The string to deserialize into the configuration.
	 * @return The deserialized configuration.
	 * @throws JITException If the input configuration is not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/02
	 */
	public final JITConfig<C> deserialize(String __v)
		throws JITException, NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Serializes the configuration so that it may be stored in a string.
	 *
	 * @param __c The configuration to serialize.
	 * @return The serialized configuration. 
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/02
	 */
	public final String serialize(JITConfig<C> __c)
		throws NullPointerException
	{
		// Check
		if (__c == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

