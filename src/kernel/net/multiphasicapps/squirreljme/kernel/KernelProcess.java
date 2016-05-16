// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import net.multiphasicapps.squirreljme.kernel.event.EventQueue;
import net.multiphasicapps.squirreljme.kernel.perm.PermissionManager;

/**
 * This represents a process within the kernel. {@link Thread}s are associated
 * with processes and are generally used for permissions.
 *
 * SquirrelJME can run without a memory manager and one is not needed at all
 * (due to lack of reflection) and as such all processes if checks were not
 * made could access other processes freely. However to prevent this, a
 * permission manager is associated with each process and checks are placed in
 * the specific locations to allow or deny specific behaviors of the code.
 *
 * @since 2016/05/16
 */
public final class KernelProcess
{
	/** The owning kernel. */
	protected final Kernel kernel;
	
	/** Is this the kernel process? */
	protected final boolean iskernel;
	
	/** The process event queue. */
	protected final EventQueue eventqueue;
	
	/** The permission manager. */
	protected final PermissionManager permissions =
		new PermissionManager(this);
	
	/** Threads the process own. */
	private final List<Thread> _threads =
		new LinkedList<>();
	
	/**
	 * Initializes the kernel process.
	 *
	 * @param __k The owning kernel.
	 * @param __ik Is this the kernel process.
	 * @throws NullPointerException
	 * @since 2016/05/16
	 */
	KernelProcess(Kernel __k, boolean __ik)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.kernel = __k;
		this.iskernel = __ik;
		
		// Setup event queue
		this.eventqueue = new EventQueue(this);
	}
	
	/**
	 * Returns {@code true} if any threads in the current process are
	 * alive.
	 *
	 * @return {@code true} if any threads are currently alive.
	 * @since 2016/05/16
	 */
	public final boolean areAnyThreadsAlive()
	{
		// Lock
		List<Thread> threads = this._threads;
		synchronized (threads)
		{
			// Go through all threads
			Iterator<Thread> it = threads.iterator();
			while (it.hasNext())
			{
				Thread t = it.next();
				
				// If not alive, remove it
				if (!t.isAlive())
					it.remove();
			}
			
			// Only if there are threads, is the process considered alive.
			return !threads.isEmpty();
		}
	}
	
	/**
	 * Returns {@code true} if the process contains the given thread.
	 *
	 * @param __t The thread to see if this processes manages it.
	 * @return {@code true} if this proccess owns the given thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/16
	 */
	public final boolean containsThread(Thread __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Lock
		List<Thread> threads = this._threads;
		synchronized (threads)
		{
			// Go through all threads and potentially cleanup while searching
			// for the given thread
			Iterator<Thread> it = threads.iterator();
			while (it.hasNext())
			{
				Thread t = it.next();
				
				// Remove dead threads
				if (!t.isAlive())
					it.remove();
				
				// Is this a thread?
				else if (t == __t)
					return true;
			}
		}
		
		// Does not contain it
		return false;
	}
	
	/**
	 * Creates a new thread with the specified code to run which is then
	 * assigned to the current process.
	 *
	 * @param __r The code to run when the thread is started.
	 * @return The newly created thread.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If thread creation is denied.
	 * @since 2016/05/16
	 */
	public final Thread createThread(Runnable __r)
		throws NullPointerException, SecurityException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// Check permission
		this.permissions.createThread();
		
		// Create thread
		Thread rv = new Thread(__r);
		
		// Lock
		List<Thread> threads = this._threads;
		synchronized (threads)
		{
			// Add to thread list
			threads.add(rv);
			
			// Start it
			rv.start();
		}
		
		// Return it
		return rv;
	}
	
	/**
	 * Returns the event queue of the process.
	 *
	 * @return The process event queue.
	 * @since 2016/05/16
	 */
	public final EventQueue eventQueue()
	{
		return this.eventqueue;
	}
	
	/**
	 * Returns {@code true} if this is the kernel process.
	 *
	 * @return {@code true} if this is the kernel process, otherwise it is
	 * a user-space process.
	 * @since 2016/05/16
	 */
	public final boolean isKernelProcess()
	{
		return this.iskernel;
	}
	
	/**
	 * Returns the kernel that this runs under.
	 *
	 * @return The kernel this runs under.
	 * @since 2016/05/16
	 */
	public final Kernel kernel()
	{
		return this.kernel;
	}
	
	/**
	 * Returns the permission manager.
	 *
	 * @return The permission manager.
	 * @since 2016/05/16
	 */
	public final PermissionManager permissions()
	{
		return this.permissions;
	}
	
	/**
	 * Adds a thread to the current process.
	 *
	 * @param __t The thread to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/16
	 */
	final void __addThread(Thread __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

