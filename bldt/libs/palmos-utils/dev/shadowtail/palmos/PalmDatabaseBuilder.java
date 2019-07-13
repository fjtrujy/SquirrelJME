// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.palmos;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to build PalmOS databases and resource databases.
 *
 * @since 2019/07/13
 */
public final class PalmDatabaseBuilder
{
	/** The type of database to create. */
	protected final PalmDatabaseType dbtype;
	
	/** The entries within the database. */
	private final List<PalmRecord> _records =
		new ArrayList<>();
	
	/** The creator of the database. */
	private String _creator =
		"????";
	
	/** The type of the database. */
	private String _type =
		"????";
	
	/** The name of the database. */
	private String _name =
		"Untitled";
	
	/**
	 * Initializes the database builder.
	 *
	 * @param __type The database type.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/13
	 */
	public PalmDatabaseBuilder(PalmDatabaseType __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		this.dbtype = __type;
	}
	
	/**
	 * Adds the specified entry.
	 *
	 * @param __type The entry type.
	 * @param __id The ID to use.
	 * @return The stream to the entry data.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/13
	 */
	public final OutputStream addEntry(String __type, int __id)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		// Create a record writer to write there
		return new __RecordWriter__(__type, __id, this._records);
	}
	
	/**
	 * Sets the creator of the database.
	 *
	 * @param __creat The creator to use.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/13
	 */
	public final PalmDatabaseBuilder setCreator(String __creat)
		throws NullPointerException
	{
		if (__creat == null)
			throw new NullPointerException("NARG");
		
		this._creator = __creat;
		return this;
	}
	
	/**
	 * Sets the name of the database.
	 *
	 * @param __name The name to use.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/13
	 */
	public final PalmDatabaseBuilder setName(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		this._name = __name;
		return this;
	}
	
	/**
	 * Sets the type of the database.
	 *
	 * @param __type The type to use.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/13
	 */
	public final PalmDatabaseBuilder setType(String __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		this._type = __type;
		return this;
	}
	
	/**
	 * Returns the byte array representing the database.
	 *
	 * @return The byte array of the database.
	 * @since 2019/07/13
	 */
	public final byte[] toByteArray()
	{
		// Just write to a stream
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			// Write the database info
			this.writeTo(baos);
			
			// Return the resulting array
			return baos.toByteArray();
		}
		
		// {@squirreljme.error BP01 Could not write the database.}
		catch (IOException e)
		{
			throw new RuntimeException("BP01", e);
		}
	}
	
	/**
	 * Writes the database to the output.
	 *
	 * @param __out The output stream.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/13
	 */
	public final void writeTo(OutputStream __out)
		throws IOException, NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}
