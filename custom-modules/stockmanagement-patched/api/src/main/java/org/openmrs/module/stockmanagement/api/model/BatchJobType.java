/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and
 * limitations under the License.
 *
 * Copyright (C) OpenHMIS.  All Rights Reserved.
 */
package org.openmrs.module.stockmanagement.api.model;

import java.util.Date;

/**
 * The allowable {@link StockOperation} statuses.
 */
public enum BatchJobType {
	/**
	 * The batch job is for generating a report.
	 */
	Report(),
	/**
	 * Other batch job.
	 */
	Other();
	
	BatchJobType() {
	}
	
	public static BatchJobType findByName(String name) {
		BatchJobType result = null;
		for (BatchJobType batchJobType : values()) {
			if (batchJobType.name().equalsIgnoreCase(name)) {
				result = batchJobType;
				break;
			}
		}
		return result;
	}
}
