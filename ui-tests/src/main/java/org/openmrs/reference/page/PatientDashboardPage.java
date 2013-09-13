package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.WebDriver;


public class PatientDashboardPage extends AbstractBasePage {

	public PatientDashboardPage(WebDriver driver) {
	    super(driver);
    }

	@Override
    public String expectedUrlPath() {
	    return URL_ROOT + "/coreapps/clinicianfacing/patient.page";
    }
	
}