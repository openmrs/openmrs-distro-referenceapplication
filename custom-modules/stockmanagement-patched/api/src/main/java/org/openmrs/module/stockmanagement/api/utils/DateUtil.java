/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.stockmanagement.api.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openmrs.module.stockmanagement.api.StockManagementException;
import org.openmrs.util.OpenmrsUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	private static final SimpleDateFormat ddMMMyyyyformatter = new SimpleDateFormat("dd-MMM-yyyy");
	
	public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	
	public static Date today() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static String formatDDMMMyyyy(Date date) {
		if (date == null) {
			return null;
		}
		return ddMMMyyyyformatter.format(date);
	}
	
	public static Date parseDate(String dateValue) {
		IllegalArgumentException pex = null;
		String[] supportedFormats = { DATE_FORMAT, "yyyy-MM-dd'T'HH:mm:ss.SSS", "yyyy-MM-dd'T'HH:mm:ssZ",
		        "yyyy-MM-dd'T'HH:mm:ssXXX", "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" };
		for (int i = 0; i < supportedFormats.length; i++) {
			try {
				Date date = DateTime.parse(dateValue, DateTimeFormat.forPattern(supportedFormats[i])).toDate();
				return date;
			}
			catch (IllegalArgumentException ex) {
				pex = ex;
			}
		}
		throw new StockManagementException(
		        "Error converting date - correct format (ISO8601 Long): yyyy-MM-dd'T'HH:mm:ss.SSSZ", pex);
	}
	
	public static String formatDateForJson(Date dateValue) {
		DateTimeFormatter dtf = DateTimeFormat.forPattern(DATE_FORMAT);
		return dtf.print(dateValue.getTime());
	}
	
	public static Date endOfDay(Date date) {
		if (date == null)
			return date;
		return OpenmrsUtil.getLastMomentOfDay(date);
	}
	
	public static String formatForFile(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		return formatter.format(date);
	}
}
