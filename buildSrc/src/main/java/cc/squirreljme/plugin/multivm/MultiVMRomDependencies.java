// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.util.concurrent.Callable;

/**
 * Dependencies for building the ROM, will depend on every library that exists.
 *
 * @since 2020/08/23
 */
public class MultiVMRomDependencies
	implements Callable<Iterable<MultiVMLibraryTask>>
{
	/** The task to execute for. */
	protected final MultiVMRomTask task;
	
	/** The source set used. */
	protected final String sourceSet;
	
	/** The virtual machine creating for. */
	protected final VirtualMachineSpecifier vmType;
	
	/**
	 * Initializes the ROM dependency task.
	 * 
	 * @param __task The task to use.
	 * @param __sourceSet The source set.
	 * @param __vmType The virtual machine type.
	 * @throws NullPointerException on null arguments.
	 * @since 2020/08/23
	 */
	public MultiVMRomDependencies(MultiVMRomTask __task, String __sourceSet,
		VirtualMachineSpecifier __vmType)
		throws NullPointerException
	{
		if (__task == null || __sourceSet == null || __vmType == null)
			throw new NullPointerException("NARG");
		
		this.task = __task;
		this.sourceSet = __sourceSet;
		this.vmType = __vmType;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/23
	 */
	@Override
	public Iterable<MultiVMLibraryTask> call()
	{
		throw new Error("TODO -- MultiVMRomDependencies");
	}
}
