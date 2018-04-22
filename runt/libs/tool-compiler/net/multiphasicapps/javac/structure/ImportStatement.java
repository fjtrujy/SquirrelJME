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
 * This represents a single import statement.
 *
 * @since 2018/04/21
 */
public final class ImportStatement
{
	/**
	 * This parses a single import statement.
	 *
	 * @param __in The input token source.
	 * @return The parsed modifiers.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureParseException If this is not a valid import statement.
	 * @since 2018/04/21
	 */
	public static ImportStatement parse(BufferedTokenSource __in)
		throws NullPointerException, StructureParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

