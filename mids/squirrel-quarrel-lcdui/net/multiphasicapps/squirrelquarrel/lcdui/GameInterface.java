// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.lcdui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.Graphics;
import net.multiphasicapps.squirrelquarrel.Game;
import net.multiphasicapps.squirrelquarrel.GameSpeed;
import net.multiphasicapps.squirrelquarrel.Level;

/**
 * This class provides an interface to the game, allowing for input to be
 * handled along with the game itself.
 *
 * @since 2017/02/08
 */
public class GameInterface
	extends Canvas
	implements Runnable
{
	/** The game to draw and interact with. */
	protected final Game game;
	
	/** The level to render and interact with. */
	protected final Level level;
	
	/** The width of the level in pixels. */
	protected final int levelpxw;
	
	/** The height of the level in pixels. */
	protected final int levelpxh;
	
	/** The current game speed. */
	private volatile GameSpeed _speed =
		GameSpeed.NORMAL;
	
	/** The number of frames which have been rendered. */
	private volatile int _renderframe;
	
	/** The viewport X position. */
	private volatile int _viewx;
	
	/** The viewport Y position. */
	private volatile int _viewy;
	
	/** The view width. */
	private volatile int _vieww;
	
	/** The view height. */
	private volatile int _viewh;
	
	/**
	 * Initializes the game.
	 *
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/08
	 */
	public GameInterface(Game __g)
		throws NullPointerException
	{
		//super(false, true);
		
		// Check
		if (__g == null)
			throw new NullPointerException("NARG");
		
		// Setup details
		setTitle("Squirrel Quarrel");
		
		// Set
		this.game = __g;
		
		// Quicker to store pixel sizes here
		Level level;
		this.level = (level = __g.level());
		this.levelpxw = level.pixelWidth();
		this.levelpxh = level.pixelHeight();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/08
	 */
	@Override
	public void paint(Graphics __g)
	{
		// Get the current frame the game is on
		Game game = this.game;
		Level level = game.level();
		int framenum = game.frameCount();
		
		// Get the viewport
		int viewx = this._viewx,
			viewy = this._viewy;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public void run()
	{
		Game game = this.game;
		for (;;)
		{
			// Get the current game speed and entry time
			GameSpeed speed = this._speed;
			long enter = System.nanoTime();
			
			// Run a single game cycle
			game.run();
			
			// Request a repaint if there is enough time to draw
			long exit = System.nanoTime();
			if ((exit - enter) < speed.nanoFrameTime())
				repaint();
			
			// Delay thread for the next frame
			exit = System.nanoTime();
			long durr = (speed.nanoFrameTime() - (exit - enter)) / 1_000_000L;
			if (durr > 0)
				try
				{
					Thread.sleep(durr);
				}
				catch (InterruptedException e)
				{
				}
		}
	}
	
	/**
	 * Translates the viewport.
	 *
	 * @param __x The relative X translation.
	 * @param __y The relative Y translation.
	 * @since 2017/02/10
	 */
	public void translateViewport(int __x, int __y)
	{
		// Translate the viewport
		int viewx = this._viewx + __x,
			viewy = this._viewy + __y,
			vieww = this._vieww,
			viewh = this._viewh,
			levelpxw = this.levelpxw,
			levelpxh = this.levelpxh;
		
		// Cap right side
		if (viewx + vieww >= levelpxw)
			viewx = levelpxw - vieww;
		
		// Cop bottom side
		if (viewy + viewh >= levelpxh)
			viewy = levelpxh - viewh;
		
		// Cap left side
		if (viewx < 0)
			viewx = 0;
		
		// Cap top
		if (viewy < 0)
			viewy = 0;
		
		// Set new viewport
		this._viewx = viewx;
		this._viewy = viewy;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	protected void sizeChanged(int __w, int __h)
	{
		// Super-class might do some things
		super.sizeChanged(__w, __h);
		
		// Correct the viewport
		this._vieww = __w;
		this._viewh = __h;
		translateViewport(0, 0);
	}
}

