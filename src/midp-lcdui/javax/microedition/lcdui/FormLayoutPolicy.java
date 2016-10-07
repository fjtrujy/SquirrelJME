// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

public abstract class FormLayoutPolicy
{
	public static int DIRECTION_LTR =
		0;
	
	public static int DIRECTION_RTL =
		1;
	
	protected FormLayoutPolicy(Form __f)
	{
		throw new Error("TODO");
	}
	
	protected abstract void doLayout(int __vpx, int __vpy, int __vpw,
		int __vph, int[] __ts);
	
	protected abstract Item getTraverse(Item __i, int __dir);
	
	protected final Form getForm()
	{
		throw new Error("TODO");
	}
	
	protected final int getHeight(Item __i)
	{
		throw new Error("TODO");
	}
	
	protected final int getWidth(Item __i)
	{
		throw new Error("TODO");
	}
	
	protected final int getX(Item __i)
	{
		throw new Error("TODO");
	}
	
	protected final int getY(Item __i)
	{
		throw new Error("TODO");
	}
	
	protected final boolean isValid(Item __i)
	{
		throw new Error("TODO");
	}
	
	protected final void setPosition(Item __i, int __x, int __y)
	{
		throw new Error("TODO");
	}
	
	protected final void setSize(Item __i, int __w, int __h)
	{
		throw new Error("TODO");
	}
	
	protected final void setValid(Item __i)
	{
		throw new Error("TODO");
	}
	
	public static final int getLayoutDirection()
	{
		throw new Error("TODO");
	}
}

