/********************************************************************************
 * Copyright (c) 2015-2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 ********************************************************************************/


package org.eclipse.mdm.api.odsadapter.utils;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;

import org.asam.ods.TS_UnionSeq;
import org.asam.ods.TS_ValueSeq;
import org.eclipse.mdm.api.base.query.Aggregation;
import org.eclipse.mdm.api.odsadapter.query.ODSAttribute;
import org.junit.Test;

public class ODSConverterTest {

	@Test
	public void testFromODSValueSeqODSDateYear() throws Exception {
		ODSAttribute attr = mock(ODSAttribute.class);

		ODSConverter.fromODSValueSeq(attr, Aggregation.NONE, "", getTS_ValueSeqFromDates("2017"));

		verify(attr).createValue(eq(""), eq(true), eq(LocalDateTime.of(2017, 1, 1, 0, 0)));
	}

	@Test
	public void testFromODSValueSeqODSDateMonth() throws Exception {
		ODSAttribute attr = mock(ODSAttribute.class);

		ODSConverter.fromODSValueSeq(attr, Aggregation.NONE, "", getTS_ValueSeqFromDates("201710"));

		verify(attr).createValue(eq(""), eq(true), eq(LocalDateTime.of(2017, 10, 1, 0, 0)));
	}

	@Test
	public void testFromODSValueSeqODSDate() throws Exception {
		ODSAttribute attr = mock(ODSAttribute.class);

		ODSConverter.fromODSValueSeq(attr, Aggregation.NONE, "", getTS_ValueSeqFromDates("20171004"));

		verify(attr).createValue(eq(""), eq(true), eq(LocalDateTime.of(2017, 10, 4, 0, 0)));
	}

	@Test
	public void testFromODSValueSeqODSDateHour() throws Exception {
		ODSAttribute attr = mock(ODSAttribute.class);

		ODSConverter.fromODSValueSeq(attr, Aggregation.NONE, "", getTS_ValueSeqFromDates("2017100412"));

		verify(attr).createValue(eq(""), eq(true), eq(LocalDateTime.of(2017, 10, 4, 12, 0)));
	}

	@Test
	public void testFromODSValueSeqODSDateMinute() throws Exception {
		ODSAttribute attr = mock(ODSAttribute.class);

		ODSConverter.fromODSValueSeq(attr, Aggregation.NONE, "", getTS_ValueSeqFromDates("201710041213"));

		verify(attr).createValue(eq(""), eq(true), eq(LocalDateTime.of(2017, 10, 4, 12, 13)));
	}

	@Test
	public void testFromODSValueSeqODSDateSecond() throws Exception {
		ODSAttribute attr = mock(ODSAttribute.class);

		ODSConverter.fromODSValueSeq(attr, Aggregation.NONE, "", getTS_ValueSeqFromDates("20171004121314"));

		verify(attr).createValue(eq(""), eq(true), eq(LocalDateTime.of(2017, 10, 4, 12, 13, 14, 0)));
	}

	@Test
	public void testFromODSValueSeqODSDateMillisecond() throws Exception {
		ODSAttribute attr = mock(ODSAttribute.class);

		ODSConverter.fromODSValueSeq(attr, Aggregation.NONE, "", getTS_ValueSeqFromDates("20171004121314123"));

		verify(attr).createValue(eq(""), eq(true), eq(LocalDateTime.of(2017, 10, 4, 12, 13, 14, 123_000_000)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFromODSValueSeqInvalidLength() throws Exception {
		ODSAttribute attr = mock(ODSAttribute.class);

		ODSConverter.fromODSValueSeq(attr, Aggregation.NONE, "", getTS_ValueSeqFromDates("201710041"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFromODSValueSeqInvalidMonth() throws Exception {
		ODSAttribute attr = mock(ODSAttribute.class);

		ODSConverter.fromODSValueSeq(attr, Aggregation.NONE, "", getTS_ValueSeqFromDates("20171304"));
	}

	private TS_ValueSeq getTS_ValueSeqFromDates(String... dates) {
		TS_UnionSeq u = new TS_UnionSeq();
		u.dateVal(dates);
		return new TS_ValueSeq(u, new short[] { 15 });
	}
}
