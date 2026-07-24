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
 * The allowable {@link BatchJobStatus} statuses.
 */
public enum BatchJobStatus {
	/**
	 * The batch job is pending.
	 */
	Pending(),
	/**
	 * The batch job is running.
	 */
	Running(),
	/**
	 * The batch job failed.
	 */
	Failed(),
	/**
	 * The batch job completed.
	 */
	Completed(),
	/**
	 * The batch job is cancelled.
	 */
	Cancelled(),
	/**
	 * The batch job has not been run before it is expired.
	 */
	Expired();
	
	BatchJobStatus() {
	}
}
