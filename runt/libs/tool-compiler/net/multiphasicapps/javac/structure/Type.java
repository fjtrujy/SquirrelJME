// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.structure;

import net.multiphasicapps.javac.token.BufferedTokenSource;
import net.multiphasicapps.javac.token.Token;
import net.multiphasicapps.javac.token.TokenSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This represents the type that may be used in the language.
 *
 * @since 2018/04/24
 */
public final class Type
{
	/**
	 * {@inheritDoc}
	 * @since 2018/04/28
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/28
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/28
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the type with an extra array associated with it.
	 *
	 * @return The type with an extra array.
	 * @since 2018/04/29
	 */
	public final Type withArray()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Parses a single type.
	 *
	 * @param __in The input token source.
	 * @return The parsed type.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureParseException If the type is not valid.
	 * @since 2018/04/24
	 */
	public static Type parseType(BufferedTokenSource __in)
		throws NullPointerException, StructureParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Parses multiple types separated by a comma.
	 *
	 * @param __in The input token source.
	 * @return The parsed types.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureParseException If the types are not valid.
	 * @since 2018/04/24
	 */
	public static Type[] parseTypes(BufferedTokenSource __in)
		throws NullPointerException, StructureParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

