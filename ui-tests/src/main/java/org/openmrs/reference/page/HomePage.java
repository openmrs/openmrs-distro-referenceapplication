package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends AbstractBasePage {
	
	static final String FIND_PATIENT_APP_ID = "coreapps-activeVisitsHomepageLink-coreapps-activeVisitsHomepageLink-extension";
	static final String REGISTER_PATIENT_APP_ID = "referenceapplication-registrationapp-registerPatient-homepageLink-referenceapplication-registrationapp-registerPatient-homepageLink-extension";
	static final String ACTIVE_VISITS_APP_ID = "org-openmrs-module-coreapps-activeVisitsHomepageLink-org-openmrs-module-coreapps-activeVisitsHomepageLink-extension";
	static final String STYLE_GUIDE_APP_ID = "referenceapplication-styleGuide-referenceapplication-styleGuide-extension";
	static final String SYSTEM_ADMIN_APP_ID = "referenceapplication-legacyAdmin-referenceapplication-legacyAdmin-extension";
	static final String DISPENSING_MEDICATION_APP_ID = "dispensing-app-homepageLink-dispensing-app-homepageLink-extension";
	
	public HomePage(WebDriver driver) {
		super(driver);
	}
	
	private boolean isAppButtonPresent(String appId) {
		try {
			return driver.findElement(By.id(appId)) != null;
		}
		catch (Exception ex) {
			return false;
		}
	}
	
	private void openApp(String appIdentifier) {
		driver.get(properties.getWebAppUrl());
		clickOn(By.id(appIdentifier));
        waitForJsVariable("Navigator.isReady");
	}
	
	public boolean isFindAPatientAppPresent() {
		return isAppButtonPresent(FIND_PATIENT_APP_ID);
	}
	
	public Boolean isRegisterPatientCustomizedForRefAppPresent() {
		return isAppButtonPresent(REGISTER_PATIENT_APP_ID);
	}
	
	public void openRegisterAPatientApp() {
		openApp(REGISTER_PATIENT_APP_ID);
	}
	
	public void openLegacyAdministrationApp() {
		openApp(SYSTEM_ADMIN_APP_ID);
	}
	
	public Boolean isActiveVisitsAppPresent() {
		return isAppButtonPresent(ACTIVE_VISITS_APP_ID);
	}
	
	public Boolean isStyleGuideAppPresent() {
		return isAppButtonPresent(STYLE_GUIDE_APP_ID);
	}
	
	public Boolean isSystemAdministrationAppPresent() {
		return isAppButtonPresent(SYSTEM_ADMIN_APP_ID);
	}
	
	public Boolean isDispensingMedicationAppPresent() {
		return isAppButtonPresent(DISPENSING_MEDICATION_APP_ID);
	}
	
	@Override
	public String expectedUrlPath() {
		return URL_ROOT + "/referenceapplication/home.page";
	}
}
