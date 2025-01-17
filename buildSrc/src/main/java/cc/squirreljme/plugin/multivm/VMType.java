// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.SquirrelJMEPluginConfiguration;
import cc.squirreljme.plugin.util.GradleJavaExecSpecFiller;
import cc.squirreljme.plugin.util.GuardedOutputStream;
import cc.squirreljme.plugin.util.JavaExecSpecFiller;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.SourceSet;
import org.gradle.process.ExecResult;

/**
 * Represents the type of virtual machine to run.
 *
 * @since 2020/08/06
 */
public enum VMType
	implements VMSpecifier
{
	/** Hosted virtual machine. */
	HOSTED("Hosted", "jar",
		":emulators:emulator-base")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/08/15
		 */
		@Override
		public void processLibrary(Task __task, boolean __isTest,
			InputStream __in, OutputStream __out)
			throws IOException, NullPointerException
		{
			if (__in == null || __out == null)
				throw new NullPointerException("NARG");
			
			// Is just pure copy of the JAR
			VMHelpers.copy(__in, __out);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2020/08/15
		 */
		@Override
		public void spawnJvmArguments(Task __task, JavaExecSpecFiller __execSpec,
			String __mainClass, Map<String, String> __sysProps,
			Path[] __libPath, Path[] __classPath, String... __args)
			throws NullPointerException
		{
			if (__task == null || __execSpec == null || __mainClass == null ||
				__sysProps == null || __classPath == null || __args == null)
				throw new NullPointerException("NARG");
				
			// Add our selection of libraries into the hosted environment so in
			// the event the active libraries are needed, they are available.
			Map<String, String> sysProps = new LinkedHashMap<>(__sysProps);
			sysProps.put("squirreljme.hosted.libraries",
				VMHelpers.classpathAsString(__libPath));
			sysProps.put("squirreljme.hosted.classpath",
				VMHelpers.classpathAsString(__classPath));
			
			// Can we directly refer to the emulator library already?
			// Only if it has not already been given, doing it here will enable
			// every sub-process quick access to the library
			if (!sysProps.containsKey("squirreljme.emulator.libpath"))
			{
				Path emuLib = VMHelpers.findEmulatorLib(__task);
				if (emuLib != null && Files.exists(emuLib))
					sysProps.put("squirreljme.emulator.libpath",
						emuLib.toString());
			}
			
			// Start with the base emulator class path
			List<Object> classPath = new ArrayList<>();
			classPath.add(VMHelpers.projectRuntimeClasspath(
				__task.getProject().project(this.emulatorProject)));
			
			// Add all of the emulator outputs
			Set<Path> vmSupportPath = new LinkedHashSet<>(); 
			for (File file : __task.getProject().project(this.emulatorProject)
				.getTasks().getByName("jar").getOutputs().getFiles())
				vmSupportPath.add(file.toPath());
			
			// Use all the supporting path
			classPath.addAll(vmSupportPath);
			
			// Append the target class path on top of this, as everything
			// will be running directly
			classPath.addAll(Arrays.asList(__classPath));
			
			// Add the VM classpath so it can be recreated if we need to spawn
			// additional tasks such as by the launcher
			sysProps.put("squirreljme.hosted.vm.supportpath",
				VMHelpers.classpathAsString(vmSupportPath));
			sysProps.put("squirreljme.hosted.vm.classpath",
				VMHelpers.classpathAsString(VMHelpers.resolvePath(classPath)));
			
			// Declare system properties that are all the originally defined
			// system properties
			for (Map.Entry<String, String> e : __sysProps.entrySet())
				sysProps.put("squirreljme.orig." + e.getKey(), e.getValue());
			
			// Debug
			__task.getLogger().debug("Hosted ClassPath: {}", classPath);
			
			// Use the classpath we previously determined
			__execSpec.classpath(classPath);
			
			// Main class was the directly specified class, we do not
			// need to handle the standard VM factory launcher
			__execSpec.setMain(__mainClass);
			
			// Use the passed arguments directly
			__execSpec.setArgs(Arrays.asList(__args));
			
			// Any desired system properties
			__execSpec.systemProperties(sysProps);
		}
	},
	
	/** SpringCoat virtual machine. */
	SPRINGCOAT("SpringCoat", "jar",
		":emulators:springcoat-vm")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/08/15
		 */
		@Override
		public void processLibrary(Task __task, boolean __isTest,
			InputStream __in, OutputStream __out)
			throws IOException, NullPointerException
		{
			if (__in == null || __out == null)
				throw new NullPointerException("NARG");
			
			// Is just pure copy of the JAR
			VMHelpers.copy(__in, __out);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2020/08/15
		 */
		@Override
		public void spawnJvmArguments(Task __task, JavaExecSpecFiller __execSpec,
			String __mainClass, Map<String, String> __sysProps,
			Path[] __libPath, Path[] __classPath, String... __args)
			throws NullPointerException
		{
			// Use a common handler to execute the VM as the VMs all have
			// the same entry point handlers and otherwise
			this.spawnVmViaFactory(__task, __execSpec, __mainClass,
				__sysProps, __libPath, __classPath, __args);
		}
	},
	
	/** SummerCoat virtual machine. */
	SUMMERCOAT("SummerCoat", "sqc",
		":emulators:summercoat-vm")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/08/15
		 */
		@Override
		public void processLibrary(Task __task, boolean __isTest,
			InputStream __in, OutputStream __out)
			throws NullPointerException
		{
			if (__task == null || __in == null || __out == null)
				throw new NullPointerException("NARG");
			
			// Need to access the config for ROM building
			SquirrelJMEPluginConfiguration config =
				SquirrelJMEPluginConfiguration
				.configuration(__task.getProject());
			
			// Class path is of the compiler target, it does not matter
			Path[] classPath = VMHelpers.runClassPath(__task.getProject()
				.getRootProject().project(":modules:aot-" +
					this.vmName(VMNameFormat.LOWERCASE)),
				SourceSet.MAIN_SOURCE_SET_NAME, VMType.HOSTED);
			
			// Setup arguments for compilation
			Collection<String> args = new ArrayList<>();
			
			// The engine to use
			args.add("-Xcompiler:" + this.vmName(VMNameFormat.LOWERCASE));
			
			// The name of this JAR
			args.add("-Xname:" + __task.getProject().getName());
			
			// Perform compilation
			args.add("compile");
			
			// Is this a boot loader? This is never valid for tests as they
			// are just extra libraries, it does not make sense to have them
			// be loadable.
			if (!__isTest && config.isBootLoader)
				args.add("-boot");
			
			// Call the AOT backend
			ExecResult exitResult = __task.getProject().javaexec(__spec ->
				{
					// Figure out the arguments to the JVM, it does not matter
					// what the classpath is
					VMType.HOSTED.spawnJvmArguments(__task,
						new GradleJavaExecSpecFiller(__spec),
						"cc.squirreljme.jvm.aot.Main",
						Collections.emptyMap(),
						classPath,
						classPath,
						args.toArray(new String[args.size()]));
					
					// Use the error stream directory
					__spec.setErrorOutput(new GuardedOutputStream(System.err));
					
					// Processing is done directly from the input
					__spec.setStandardInput(__in);
					
					// The caller will consume the entire output of what was
					// processed, so
					__spec.setStandardOutput(__out);
					
					// Ignore error states, let us handle it instead of Gradle
					// so we could handle multiple different exit codes.
					__spec.setIgnoreExitValue(true);
				});
			
			// Processing the library did not work?
			int code;
			if ((code = exitResult.getExitValue()) != 0)
				throw new RuntimeException(String.format(
					"Failed to process library (exit code %d).", code));
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2020/11/21
		 */
		@Override
		public Iterable<Task> processLibraryDependencies(
			VMLibraryTask __task)
			throws NullPointerException
		{
			Project project = __task.getProject().getRootProject()
				.project(":modules:aot-" +
					this.vmName(VMNameFormat.LOWERCASE));
			Project rootProject = project.getRootProject();
			
			// Make sure the AOT compiler is always up to date when this is
			// ran, otherwise things can be very weird if it is not updated
			// which would not be a good thing at all
			Collection<Task> rv = new LinkedList<>();
			for (ProjectAndTaskName task : VMHelpers.runClassTasks(project,
				SourceSet.MAIN_SOURCE_SET_NAME, VMType.HOSTED))
				rv.add(rootProject.project(task.project).getTasks()
					.getByName(task.task));
			
			// Make sure the hosted environment is working since it needs to
			// be kept up to date as well
			for (Task task : new VMEmulatorDependencies(__task,
				VMType.HOSTED).call())
				rv.add(task);
			
			return rv;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2020/11/27
		 */
		@Override
		public void processRom(Task __task, OutputStream __out,
			Collection<Path> __libs)
			throws IOException, NullPointerException
		{
			if (__task == null || __out == null || __libs == null)
				throw new NullPointerException("NARG");
			
			// Class path is of the compiler target, it does not matter
			Path[] classPath = VMHelpers.runClassPath(__task.getProject()
				.getRootProject().project(":modules:aot-" +
					this.vmName(VMNameFormat.LOWERCASE)),
				SourceSet.MAIN_SOURCE_SET_NAME, VMType.HOSTED);
			
			// Setup arguments for compilation
			Collection<String> args = new ArrayList<>();
			
			// The engine to use
			args.add("-Xcompiler:" + this.vmName(VMNameFormat.LOWERCASE));
			
			// Perform ROM creation
			args.add("-Xname:squirreljme");
			args.add("rom");
			
			// Put down paths to libraries to link together
			for (Path path : __libs)
				args.add(path.toString());
			
			// Call the AOT backend
			ExecResult exitResult = __task.getProject().javaexec(__spec ->
				{
					// Figure out the arguments to the JVM, it does not matter
					// what the classpath is
					VMType.HOSTED.spawnJvmArguments(__task,
						new GradleJavaExecSpecFiller(__spec),
						"cc.squirreljme.jvm.aot.Main",
						Collections.emptyMap(),
						classPath,
						classPath,
						args.toArray(new String[args.size()]));
					
					// Use the error stream directory
					__spec.setErrorOutput(new GuardedOutputStream(System.err));
					
					// The caller will consume the entire output of what was
					// processed, so
					__spec.setStandardOutput(__out);
					
					// Ignore error states, let us handle it instead of Gradle
					// so we could handle multiple different exit codes.
					__spec.setIgnoreExitValue(true);
				});
			
			// Processing the library did not work?
			int code;
			if ((code = exitResult.getExitValue()) != 0)
				throw new RuntimeException(String.format(
					"Failed to process ROM (exit code %d).", code));
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2020/08/15
		 */
		@Override
		public void spawnJvmArguments(Task __task, JavaExecSpecFiller __execSpec,
			String __mainClass, Map<String, String> __sysProps,
			Path[] __libPath, Path[] __classPath, String... __args)
			throws NullPointerException
		{
			// Use a common handler to execute the VM as the VMs all have
			// the same entry point handlers and otherwise
			this.spawnVmViaFactory(__task, __execSpec, __mainClass,
				__sysProps, __libPath, __classPath, __args);
		}
	},
	
	/* End. */
	;
	
	/** The proper name of the VM. */
	public final String properName;
	
	/** The extension for the VM. */
	public final String extension;
	
	/** The project used for the emulator. */
	public final String emulatorProject;
	
	/**
	 * Returns the proper name of the virtual machine.
	 * 
	 * @param __properName The proper name of the VM.
	 * @param __extension The library extension.
	 * @param __emulatorProject The project used for the emulator.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/06
	 */
	VMType(String __properName, String __extension,
		String __emulatorProject)
		throws NullPointerException
	{
		if (__properName == null || __extension == null ||
			__emulatorProject == null)
			throw new NullPointerException("NARG");
		
		this.properName = __properName;
		this.extension = __extension;
		this.emulatorProject = __emulatorProject;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/16
	 */
	@Override
	public final String emulatorProject()
	{
		return this.emulatorProject;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/23
	 */
	@Override
	public final boolean hasRom()
	{
		return this == VMType.SUMMERCOAT;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/07
	 */
	@Override
	public String outputLibraryName(Project __project, String __sourceSet)
		throws NullPointerException
	{
		if (__project == null || __sourceSet == null)
			throw new NullPointerException("NARG");
		
		// The main library should never show its original source set
		if (SourceSet.MAIN_SOURCE_SET_NAME.equals(__sourceSet))
			return __project.getName() + "." + this.extension;
		
		// Otherwise include the source sets
		return __project.getName() + "-" + __sourceSet + "." + this.extension;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/27
	 */
	@Override
	public String outputRomName(String __sourceSet)
		throws NullPointerException
	{
		if (SourceSet.MAIN_SOURCE_SET_NAME.equals(__sourceSet))
			return "squirreljme." + this.extension;
		return "squirreljme-" + __sourceSet + "." + this.extension;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/21
	 */
	@Override
	public Iterable<Task> processLibraryDependencies(
		VMLibraryTask __task)
		throws NullPointerException
	{
		return Collections.emptyList();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/27
	 */
	@Override
	public void processRom(Task __task, OutputStream __out,
		Collection<Path> __libs)
		throws IOException, NullPointerException
	{
		throw new RuntimeException(this.name() + " is not ROM capable.");
	}
	
	/**
	 * Spawns a virtual machine using the standard {@code VmFactory} class.
	 * 
	 * @param __task The task being executed, may be used as context.
	 * @param __execSpec The execution specification.
	 * @param __mainClass The main class to execute.
	 * @param __sysProps The system properties to define.
	 * @param __libPath The library path to use.
	 * @param __classPath The class path of the execution target.
	 * @param __args Arguments to the started program.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/15
	 */
	public void spawnVmViaFactory(Task __task, JavaExecSpecFiller __execSpec,
		String __mainClass, Map<String, String> __sysProps, Path[] __libPath,
		Path[] __classPath, String[] __args)
		throws NullPointerException
	{
		if (__task == null || __execSpec == null || __mainClass == null ||
			__sysProps == null || __libPath == null || __classPath == null ||
			__args == null)
			throw new NullPointerException("NARG");
		
		// Determine the class-path for the emulator
		List<Path> vmClassPath = new ArrayList<>();
		for (File file : VMHelpers.projectRuntimeClasspath(
			__task.getProject().project(this.emulatorProject)))
			vmClassPath.add(file.toPath());
		
		// Add all of the emulator outputs
		for (File file : __task.getProject().project(this.emulatorProject)
			.getTasks().getByName("jar").getOutputs().getFiles())
			vmClassPath.add(file.toPath());
		
		// Debug
		__task.getLogger().debug("VM ClassPath: {}", vmClassPath);
		
		// Build arguments to the VM
		Collection<String> vmArgs = new LinkedList<>();
		
		// Add emulator to launch
		vmArgs.add("-Xemulator:" + this.vmName(VMNameFormat.LOWERCASE));
		
		// Add library paths, suites that are available for consumption
		vmArgs.add("-Xlibraries:" + VMHelpers.classpathAsString(__libPath));
		
		// Determine where profiler snapshots are to go, try to use the
		// profiler directory for that
		Path profilerDir = ((__task instanceof VMExecutableTask) ?
			VMHelpers.profilerDir(__task.getProject(), this,
			((VMExecutableTask)__task).getSourceSet()).get() :
			__task.getProject().getBuildDir().toPath());
		
		// Use the main class name unless this is a test, so that they are
		// named better
		String profilerClass = (__mainClass.equals(
			VMHelpers.SINGLE_TEST_RUNNER) && __args.length > 0 ?
			__args[0] : __mainClass);
		vmArgs.add("-Xsnapshot:" + profilerDir.resolve(
			__task.getProject().getName() + "_" +
			profilerClass.replace('.', '-') + ".nps"));
		
		// Class path for the target program to launch
		vmArgs.add("-classpath");
		vmArgs.add(VMHelpers.classpathAsString(__classPath));
		
		// Any system properties
		for (Map.Entry<String, String> sysProp : __sysProps.entrySet())
			vmArgs.add("-D" + sysProp.getKey() + "=" + sysProp.getValue());
		
		// Main class of the target to run
		vmArgs.add(__mainClass);
		
		// Any arguments to the target run
		vmArgs.addAll(Arrays.asList(__args));
		
		// Classpath used for execution
		Path[] classPath = vmClassPath.<Path>toArray(
			new Path[vmClassPath.size()]);
		
		// Launching is effectively the same as the hosted run but with the
		// VM here instead. System properties are passed through so that the
		// holding VM and the sub-VM share the same properties.
		VMType.HOSTED.spawnJvmArguments(__task, __execSpec,
			"cc.squirreljme.emulator.vm.VMFactory", __sysProps,
			__libPath, classPath,
			vmArgs.<String>toArray(new String[vmArgs.size()]));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/07
	 */
	@Override
	public String vmName(VMNameFormat __format)
		throws NullPointerException
	{
		String properName = this.properName;
		switch (__format)
		{
			case LOWERCASE:
				return properName.toLowerCase();
				
			case CAMEL_CASE:
				return Character.toLowerCase(properName.charAt(0)) +
					properName.substring(1);
				
			case PROPER_NOUN:
			default:
				return properName;
		}
	}
}
