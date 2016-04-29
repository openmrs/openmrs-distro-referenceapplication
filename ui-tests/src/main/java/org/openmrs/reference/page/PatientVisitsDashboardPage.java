package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PatientVisitsDashboardPage extends Page {
	private static final By CAPTURE_VITALS = By.id("referenceapplication.realTime.vitals");

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

}
