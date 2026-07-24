package org.openmrs.module.stockmanagement.api.dto.reporting;

public enum Fullfillment {
	All(), Full(), Partial(), None;
	
	public static Fullfillment findByName(String name) {
		return findInList(name, values());
	}
	
	public static Fullfillment findInList(String name, Fullfillment[] parameterList) {
		Fullfillment result = null;
		for (Fullfillment enumValue : parameterList) {
			if (enumValue.name().equalsIgnoreCase(name)) {
				result = enumValue;
				break;
			}
		}
		return result;
	}
}
