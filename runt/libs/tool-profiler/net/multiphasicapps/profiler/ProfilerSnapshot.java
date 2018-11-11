// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.profiler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This represents the main profiler snapshot which contains all of the data
 * within what is to be profiled, it is mutable and accordingly allows for
 * export to NPS formats.
 *
 * @since 2018/11/10
 */
public final class ProfilerSnapshot
{
	/** Threads that are being measured. */
	private final Map<String, ProfiledThread> _threads =
		new LinkedHashMap<>();
	
	/**
	 * Starts profiling the given thread.
	 *
	 * @param __name The name of the thread.
	 * @return A class to handle the profiling of threads via the call stack.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/10
	 */
	public final ProfiledThread measureThread(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		ProfiledThread rv = new ProfiledThread(__name);
		
		// Although the frames are not thread safe, this may be called at
		// any time from any thread although the returned class is only inteded
		// to be used by a single thread
		Map<String, ProfiledThread> threads = this._threads;
		synchronized (threads)
		{
			threads.put(__name, rv);
		}
		
		return rv;
	}
	
	/**
	 * Writes the snapshot information to the given output stream.
	 *
	 * @param __os The stream to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/11
	 */
	public final void writeTo(OutputStream __os)
		throws IOException, NullPointerException
	{
		if (__os == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

