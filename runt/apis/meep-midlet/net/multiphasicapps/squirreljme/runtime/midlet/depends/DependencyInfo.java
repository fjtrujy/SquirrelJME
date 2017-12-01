// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.midlet.depends;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import net.multiphasicapps.collections.ArrayUtils;
import net.multiphasicapps.collections.EmptySet;
import net.multiphasicapps.collections.SortedTreeSet;
import net.multiphasicapps.collections.UnmodifiableSet;
import net.multiphasicapps.squirreljme.runtime.midlet.InvalidSuiteException;
import net.multiphasicapps.strings.StringUtils;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestAttributes;
import net.multiphasicapps.tool.manifest.JavaManifestKey;

/**
 * This contains the information which specifies all of the dependencies which
 * and application depends on.
 *
 * @since 2017/11/30
 */
public final class DependencyInfo
{
	/** The configuration. */
	protected final Configuration config;
	
	/** Profiles to use. */
	private final Set<Profile> profiles;
	
	/** Dependencies. */
	private final Set<SuiteDependency> depends;
	
	/**
	 * Initializes the dependency information.
	 *
	 * @param __config The configuration to use.
	 * @param __profiles The profiles to implement.
	 * @param __deps The dependencies to use.
	 * @since 2017/11/30
	 */
	public DependencyInfo(Configuration __config, Profile[] __profiles,
		SuiteDependency[] __deps)
	{
		this(__config,
			(__profiles == null ? null : Arrays.<Profile>asList(__profiles)),
			(__deps == null ? null : Arrays.<SuiteDependency>asList(__deps)));
	}
	
	/**
	 * Initializes the dependency information.
	 *
	 * @param __config The configuration to use.
	 * @param __profiles The profiles to implement.
	 * @param __deps The dependencies to use.
	 * @since 2017/11/30
	 */
	public DependencyInfo(Configuration __config,
		Collection<Profile> __profiles, Collection<SuiteDependency> __deps)
	{
		this.config = __config;
		this.profiles = (__profiles == null ? EmptySet.<Profile>empty() :
			UnmodifiableSet.<Profile>of(new SortedTreeSet<>(__profiles)));
		this.depends = (__deps == null ? EmptySet.<SuiteDependency>empty() :
			UnmodifiableSet.<SuiteDependency>of(new SortedTreeSet<>(__deps)));
	}
	
	/**
	 * Checks if the dependency information is empty.
	 *
	 * @return If the dependency info is empty.
	 * @since 2017/11/30
	 */
	public final boolean isEmpty()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Matches this dependency information to see if any of the provided
	 * fields would statisfy the dependencies that are needed.
	 *
	 * @param __prov The provided information.
	 * @return The result of the match.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/30
	 */
	public final MatchResult match(ProvidedInfo __prov)
		throws NullPointerException
	{
		if (__prov == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns dependency information which contains no optional
	 * dependencies.
	 *
	 * @return Dependency information with no optionals.
	 * @since 2017/11/30
	 */
	public final DependencyInfo noOptionals()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Parses the given manifest and returns all of the dependencies which
	 * are required by what is specified in the manifest.
	 *
	 * @param __man The manifest to parse.
	 * @return The parsed dependency information.
	 * @throws InvalidSuiteException If the manifest is not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/20
	 */
	public static final DependencyInfo parseManifest(JavaManifest __man)
		throws InvalidSuiteException, NullPointerException
	{
		if (__man == null)
			throw new NullPointerException("NARG");
		
		JavaManifestAttributes attr = __man.getMainAttributes();
		
		// Resulting dependencies
		Configuration config = null;
		Set<Profile> profiles = new LinkedHashSet<>();
		Set<SuiteDependency> dependencies = new LinkedHashSet<>();
		
		// The CLDC library to use
		String xconfig = attr.getValue("microedition-configuration");
		if (xconfig != null)
			config = new Configuration(xconfig.trim());
		
		// Profiles needed to run
		String xprofiles = attr.getValue("microedition-configuration");
		if (xprofiles != null)
			for (String s : StringUtils.basicSplit(" \t", xprofiles))
				profiles.add(new Profile(s));
		
		// Determine the prefix to use, for MIDlets or liblets
		String prefix = (attr.getValue("midlet-name") != null ?
			"midlet-dependency-" : "liblet-dependency-");
		
		// Parse entries in sequential order
		for (int i = 1; i >= 1; i++)
		{
			// Stop if no more values are read
			String value = attr.getValue(prefix + i);
			if (value == null)
				break;
			
			// Decode dependency
			dependencies.add(new SuiteDependency(value));
		}
		
		// Build
		return new DependencyInfo(config, profiles, dependencies);
	}
}

