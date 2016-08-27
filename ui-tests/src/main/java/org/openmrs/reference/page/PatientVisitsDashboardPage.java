/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PatientVisitsDashboardPage extends Page {
	private static final By CAPTURE_VITALS = By.id("referenceapplication.realTime.vitals");
	private static final By VISIT_LIST = By.cssSelector("#visits-list div");

	public PatientVisitsDashboardPage(Page parent) {
		super(parent);
	}

	@Override
	public String getPageUrl() {
		return "coreapps/patientdashboard/patientDashboard.page";
	}

	public void goToCaptureVitals() {
	    findElement(CAPTURE_VITALS).click();
    }

    public List<WebElement> getVisitList(){
    	return findElements(VISIT_LIST);
	}

}
