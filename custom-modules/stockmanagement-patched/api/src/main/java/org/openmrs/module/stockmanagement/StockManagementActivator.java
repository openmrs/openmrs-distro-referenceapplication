/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.stockmanagement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.stockmanagement.tasks.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This class contains the logic that is run every time this module is either started or shutdown
 */
public class StockManagementActivator extends BaseModuleActivator {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * @see #started()
	 */
	public void started() {
		log.info("Starting Stock Management");

		try{
			List<StartupTask> startUpTasks = new ArrayList<>();
			startUpTasks.add(new LocationTagsSynchronize());
			startUpTasks.add(new LocationTreeSynchronize());
			startUpTasks.add(new PartySynchronize());
			startUpTasks.sort(Comparator.comparing(StartupTask::getPriority));

			for(StartupTask task : startUpTasks){
				task.execute();
			}
		}catch (Exception exception){
			log.error(exception);
		}

		log.info("Started Stock Management");
	}
	
	/**
	 * @see #shutdown()
	 */
	public void shutdown() {
		log.info("Shutdown Stock Management");
	}
	
}
