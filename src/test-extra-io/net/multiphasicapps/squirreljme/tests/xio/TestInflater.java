// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.tests.xio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.io.DataProcessorInputStream;
import net.multiphasicapps.io.InflateDataProcessor;
import net.multiphasicapps.squirreljme.test.TestChecker;
import net.multiphasicapps.squirreljme.test.TestInvoker;

/**
 * This contains tests for the extra IO inflate decompression algorithm.
 *
 * @since 2016/03/10
 */
public class TestInflater
	implements TestInvoker
{
	/** Compressed "TestingTesting". */
	private static final byte[] SAMPLE_A_IN =
		new byte[]{11, 73, 45, 46, -55, -52, 75, 15, -127, 80, 0};
	
	/** Uncompressed "TestingTesting". */
	private static final byte[] SAMPLE_A_OUT =
		new byte[]{84, 101, 115, 116, 105, 110, 103, 84, 101, 115, 116, 105,
		110, 103};
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/03
	 */
	public String invokerName()
	{
		return "extraio.inflater";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/03
	 */
	public void runTests(TestChecker __tc)
		throws NullPointerException
	{
		// Check
		if (__tc == null)
			throw new NullPointerException();
		
		// Run checks on samples
		__check(__tc, "a", SAMPLE_A_IN, SAMPLE_A_OUT);
	}
	
	/**
	 * Checks whether the inflater works for the given input.
	 *
	 * @param __tc The test checker.
	 * @param __id The identification of this test.
	 * @param __in The input bytes.
	 * @param __out The output bytes.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/10
	 */
	private void __check(TestChecker __tc, String __id, byte[] __in,
		byte[] __out)
		throws NullPointerException
	{
		// Check
		if (__tc == null || __id == null || __in == null || __out == null)
			throw new NullPointerException();
		
		// Open the input
		try (InputStream in = new DataProcessorInputStream(
			new ByteArrayInputStream(__in), new InflateDataProcessor());
			ByteArrayOutputStream out = new ByteArrayOutputStream())
		{
			// Read input to the output
			for (;;)
			{
				// Read in
				int b = in.read();
				
				// EOF?
				if (b < 0)
					break;
				
				// Write
				out.write(b);
			}
			
			// Check the array
			__tc.checkEquals(__id, __out, out.toByteArray());
		}
		
		// Failed
		catch (IOException ioe)
		{
			__tc.exception(__id, ioe);
		}
	}
}

