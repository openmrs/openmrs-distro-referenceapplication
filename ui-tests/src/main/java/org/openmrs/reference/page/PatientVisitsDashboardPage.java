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
import org.openqa.selenium.WebDriver;

public class PatientVisitsDashboardPage extends Page {
	private static final By CAPTURE_VITALS = By.id("referenceapplication.realTime.vitals");
	static final By HOME_LOGO = By.className("logo");
	
	public PatientVisitsDashboardPage(Page page) {
		super(page);
	}
	
	// This constructor should not be used anymore. Too many tests rely on it right now to delete it.
	public PatientVisitsDashboardPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public String getPageUrl() {
		return "coreapps/patientdashboard/patientDashboard.page";
	}

	public void goToCaptureVitals() {
	    findElement(CAPTURE_VITALS).click();
    }
	
	public void goToHome() {
		clickOn(HOME_LOGO);
	}
}
