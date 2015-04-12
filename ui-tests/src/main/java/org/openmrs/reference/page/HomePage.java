/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends AbstractBasePage {
	
	static final String FIND_PATIENT_APP_ID = "coreapps-activeVisitsHomepageLink-coreapps-activeVisitsHomepageLink-extension";
	static final String REGISTER_PATIENT_APP_ID = "referenceapplication-registrationapp-registerPatient-homepageLink-referenceapplication-registrationapp-registerPatient-homepageLink-extension";
	static final String ACTIVE_VISITS_APP_ID = "org-openmrs-module-coreapps-activeVisitsHomepageLink-org-openmrs-module-coreapps-activeVisitsHomepageLink-extension";
	static final String STYLE_GUIDE_APP_ID = "referenceapplication-styleGuide-referenceapplication-styleGuide-extension";
	static final String SYSTEM_ADMIN_APP_ID = "coreapps-systemadministration-homepageLink-coreapps-systemadministration-homepageLink-extension";
    static final String CONFIGURE_METADATA_APP_ID = "coreapps-configuremetadata-homepageLink-coreapps-configuremetadata-homepageLink-extension";
	static final String DISPENSING_MEDICATION_APP_ID = "dispensing-app-homepageLink-dispensing-app-homepageLink-extension";
	static final String CAPTURE_VITALS_APP_ID = "referenceapplication-vitals-referenceapplication-vitals-extension";
	
	private static final By INPATIENT = By.xpath("/html/body/header/div[@id='session-location']/ul[@class='select']/li[1]");
	private static final By ISOLATION = By.xpath("/html/body/header/div[@id='session-location']/ul[@class='select']/li[2]");
	private static final By LAB = By.xpath("/html/body/header/div[@id='session-location']/ul[@class='select']/li[3]");
	private static final By OUTPATIENT = By.xpath("/html/body/header/div[@id='session-location']/ul[@class='select']/li[4]");
	private static final By PHARMACY = By.xpath("/html/body/header/div[@id='session-location']/ul[@class='select']/li[5]");
	private static final By REGISTRATION = By.xpath("/html/body/header/div[@id='session-location']/ul[@class='select']/li[6]");
	private static final By UNKNOWN = By.xpath("/html/body/header/div[@id='session-location']/ul[@class='select']/li[7]");
	public static final By LOCATIONTEXT = By.xpath("/html/body/div[@id='body-wrapper']/div[@id='content']/div[@id='home-container']/h4");
	public static final By LOCATIONLINK = By.xpath("/html/body/header/ul[@class='user-options']/li[@class='change-location']/a/span");

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

    public int numberOfAppsPresent() {
        return driver.findElements(By.cssSelector("#apps .app")).size();
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

    public Boolean isConfigureMetadataAppPresent() {
        return isAppButtonPresent(CONFIGURE_METADATA_APP_ID);
	}

	public Boolean isDispensingMedicationAppPresent() {
		return isAppButtonPresent(DISPENSING_MEDICATION_APP_ID);
	}
	
	public boolean isCaptureVitalsAppPresent() {
		return isAppButtonPresent(CAPTURE_VITALS_APP_ID);
    }

	@Override
	public String expectedUrlPath() {
		return URL_ROOT + "/referenceapplication/home.page";
	}
	
	public String getLocationText() {
		return driver.findElement(LOCATIONTEXT).getText();
	}
	
	public void switchToInpatient() {
		clickOn(LOCATIONLINK);
		clickOn(INPATIENT);
	}
	
	public void switchToIsolation() {
		clickOn(LOCATIONLINK);
		clickOn(ISOLATION);
	}
	
	public void switchToLab() {
		clickOn(LOCATIONLINK);
		clickOn(LAB);
	}
	
	public void switchToOutpatient() {
		clickOn(LOCATIONLINK);
		clickOn(OUTPATIENT);
	}
	
	public void switchToPharmacy() {
		clickOn(LOCATIONLINK);
		clickOn(PHARMACY);
	}
	
	public void switchToRegistration() {
		clickOn(LOCATIONLINK);
		clickOn(REGISTRATION);
	}
	
	public void switchToUnknown() {
		clickOn(LOCATIONLINK);
		clickOn(UNKNOWN);
	}

}
