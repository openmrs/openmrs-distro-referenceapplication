package org.openmrs.module.stockmanagement.api.dto.reporting;

public enum MostLeastMoving {
	MostMoving(), LeastMoving();
	
	public static MostLeastMoving findByName(String name) {
		return findInList(name, values());
	}
	
	public static MostLeastMoving findInList(String name, MostLeastMoving[] parameterList) {
		MostLeastMoving result = null;
		for (MostLeastMoving enumValue : parameterList) {
			if (enumValue.name().equalsIgnoreCase(name)) {
				result = enumValue;
				break;
			}
		}
		return result;
	}
}
