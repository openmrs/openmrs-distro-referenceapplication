package org.openmrs.module.stockmanagement.api.utils;

import java.util.regex.Pattern;

public class StringUtils {
	
	static Pattern emailPattern = Pattern
	        .compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
	
	public static boolean isValidEmail(String email) {
		if (email == null)
			return false;
		if (email.endsWith("."))
			return false;
		return emailPattern.matcher(email).matches();
	}
}
