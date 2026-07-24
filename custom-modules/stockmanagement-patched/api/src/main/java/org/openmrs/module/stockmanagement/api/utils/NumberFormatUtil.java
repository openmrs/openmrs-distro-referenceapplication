package org.openmrs.module.stockmanagement.api.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NumberFormatUtil {
	
	public static final DecimalFormat QtyDisplayFormat = new DecimalFormat("#,###,###,##0.##");
	
	public static final DecimalFormat IntegerDisplayFormat = new DecimalFormat("#,###,###,##0");
	
	public static String qtyDisplayFormat(BigDecimal value) {
		return QtyDisplayFormat.format(value);
	}
	
	public static String integerDisplayFormat(Integer value) {
		return IntegerDisplayFormat.format(value);
	}
}
