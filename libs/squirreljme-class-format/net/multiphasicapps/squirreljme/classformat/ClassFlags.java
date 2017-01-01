// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classformat;

/**
 * This represents the flags which a class may be.
 *
 * @since 2016/04/23
 */
public class ClassFlags
	extends Flags<ClassFlag>
	implements AccessibleFlags
{
	/**
	 * Initializes the class flags.
	 *
	 * @param __fl The class flags.
	 * @since 2016/04/23
	 */
	public ClassFlags(ClassFlag... __fl)
	{
		super(ClassFlag.class, __fl);
		
		__checkFlags();
	}
	
	/**
	 * Initializes the class flags.
	 *
	 * @param __fl The class flags.
	 * @since 2016/04/23
	 */
	public ClassFlags(Iterable<ClassFlag> __fl)
	{
		super(ClassFlag.class, __fl);
		
		__checkFlags();
	}
	
	/**
	 * Is this class abstract?
	 *
	 * @return {@code true} if it is abstract.
	 * @since 2016/03/15
	 */
	public final boolean isAbstract()
	{
		return contains(ClassFlag.ABSTRACT);
	}
	
	/**
	 * Is this an annotation?
	 *
	 * @return {@code true} if it is an annotation.
	 * @since 2016/03/15
	 */
	public final boolean isAnnotation()
	{
		return contains(ClassFlag.ANNOTATION);
	}
	
	/**
	 * Is this an enumeration?
	 *
	 * @return {@code true} if it is an enumeration.
	 * @since 2016/03/15
	 */
	public final boolean isEnum()
	{
		return contains(ClassFlag.ENUM);
	}
	
	/**
	 * Is this class final?
	 *
	 * @return {@code true} if it is final.
	 * @since 2016/03/15
	 */
	public final boolean isFinal()
	{
		return contains(ClassFlag.FINAL);
	}
	
	/**
	 * Is this an interface?
	 *
	 * @return {@code true} if an interface.
	 * @since 2016/03/15
	 */
	public final boolean isInterface()
	{
		return contains(ClassFlag.INTERFACE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/15
	 */
	@Override
	public final boolean isPackagePrivate()
	{
		return !isPublic();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public final boolean isPrivate()
	{
		// Classes are never private
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public final boolean isProtected()
	{
		// Classes are never protected
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/15
	 */
	@Override
	public final boolean isPublic()
	{
		return contains(ClassFlag.PUBLIC);
	}
	
	/**
	 * Is there special handling for super-class method calls?
	 *
	 * @return {@code true} if the super-class invocation special flag is set.
	 * @since 2016/03/15
	 */
	public final boolean isSpecialInvokeSpecial()
	{
		return contains(ClassFlag.SUPER);
	}
	
	/**
	 * Checks that the given flags are valid.
	 *
	 * @throws ClassFormatException If they are not valid.
	 * @since 2016/04/23
	 */
	private final void __checkFlags()
		throws ClassFormatException
	{
		// Interface?
		if (isInterface())
		{
			// {@squirreljme.error AY01 An interface must also be abstract.
			// (The class flags)}
			if (!isAbstract())
				throw new ClassFormatException(String.format("AY01 %s", this));
			
			// {@squirreljme.error AY02 An interface cannot be {@code final} or
			// {@code enum} and it must not have the special flag set. (The
			// class flags)}
			if (isFinal() || isSpecialInvokeSpecial() || isEnum())
				throw new ClassFormatException(String.format("AY02 %s", this));
		}
		
		// Normal class
		else
		{
			// {@squirreljme.error AY03 Annotations must be interfaces. (The
			// class flags)}
			if (isAnnotation())
				throw new ClassFormatException(String.format("AY03 %s", this));
				
			// {@squirreljme.error AY04 A class cannot be both {@code abstract}
			// and {@code final}. (The class flags)}
			if (isAbstract() && isFinal())
				throw new ClassFormatException(String.format("AY04 %s", this));
		}
	}
}

