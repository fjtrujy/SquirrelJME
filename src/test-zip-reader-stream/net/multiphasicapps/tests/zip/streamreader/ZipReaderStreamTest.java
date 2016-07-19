// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tests.zip.streamreader;

import java.io.ByteArrayInputStream;
import java.util.Random;
import net.multiphasicapps.tests.IndividualTest;
import net.multiphasicapps.tests.InvalidTestException;
import net.multiphasicapps.tests.TestComparison;
import net.multiphasicapps.tests.TestGroupName;
import net.multiphasicapps.tests.TestFamily;
import net.multiphasicapps.tests.TestInvoker;
import net.multiphasicapps.tests.zip.streamwriter.ZipWriterStreamTest;
import net.multiphasicapps.zip.streamreader.ZipStreamEntry;
import net.multiphasicapps.zip.streamreader.ZipStreamReader;

/**
 * This contains the set of tests which test that reading of ZIP files works
 * correctly.
 *
 * @since 2016/07/19
 */
public class ZipReaderStreamTest
	implements TestInvoker
{
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public void runTest(IndividualTest __t)
		throws NullPointerException, Throwable
	{
		// Check
		if (__t == null)
			throw new NullPointerException();
		
		// Create a ZIP with a bunch of random files
		byte[] zipdata = ZipWriterStreamTest.generateZipFile(__t);
		
		// See if the contents can be read correctly
		try (ByteArrayInputStream bais = new ByteArrayInputStream(zipdata);
			ZipStreamReader zsr = new ZipStreamReader(bais))
		{
			for (;;)
				try (ZipStreamEntry ze = zsr.nextEntry())
				{
					// Stop if no more entries remain
					if (ze == null)
						break;
					
					// Make sure the entry matches
					ZipWriterStreamTest.compareEntry(__t, ze, ze.name());
				}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/19
	 */
	@Override
	public TestFamily testFamily()
	{
		// Generate some random seeds
		Random rand = new Random(0x1989_07_06);
		
		return new TestFamily(
			"net.multiphasicapps.zip.streamreader.ZipStreamReader",
			Long.toString(rand.nextLong()),
			Long.toString(rand.nextLong()),
			Long.toString(rand.nextLong()));
	}
}

