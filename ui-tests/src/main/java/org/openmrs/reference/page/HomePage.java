package org.openmrs.reference.page;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends AbstractBasePage{

    private String FIND_PATIENT = "coreapps-activeVisitsHomepageLink-coreapps-activeVisitsHomepageLink-extension";
    private String REGISTER_PATIENT = "registrationapp-basicRegisterPatient-homepageLink-registrationapp-basicRegisterPatient-homepageLink-extension";
    private String REGISTER_PATIENT_REF_APP = "referenceapplication-registrationapp-registerPatient-homepageLink-referenceapplication-registrationapp-registerPatient-homepageLink-extension";
    private String ACTIVE_VISITS = "org-openmrs-module-coreapps-activeVisitsHomepageLink-org-openmrs-module-coreapps-activeVisitsHomepageLink-extension";
    private String STYLE_GUIDE = "referenceapplication-styleGuide-referenceapplication-styleGuide-extension";
    private String SYSTEM_ADMIN_APP = "legacy-admin-legacy-admin-extension";
    private String LEGACY_FIND_PATIENT = "legacy-findPatient-legacy-findPatient-extension";

    public HomePage(WebDriver driver) {
        super(driver);
    }

    private boolean isAppButtonPresent(String appId) {
        try {
            return driver.findElement(By.id(appId)) != null;
        } catch (Exception ex) {
            return false;
        }
    }

    private void openApp(String appIdentifier) {
        driver.get(properties.getWebAppUrl());
        clickOn(By.id(appIdentifier));
    }

    public boolean isFindAPatientAppPresented() {
        return isAppButtonPresent(FIND_PATIENT);
    }

    public Boolean isRegisterPatientCustomizedForRefAppPresented() {
        return isAppButtonPresent(REGISTER_PATIENT_REF_APP);
    }

    public Boolean isPatientRegistrationAppPresented() {
        return isAppButtonPresent(REGISTER_PATIENT);
    }

    public Boolean isActiveVisitsAppPresented() {
        return isAppButtonPresent(ACTIVE_VISITS);
    }

    public Boolean isStyleGuideAppPresented() {
        return isAppButtonPresent(STYLE_GUIDE);
    }

    public Boolean isSystemAdministrationAppPresented() {
        return isAppButtonPresent(SYSTEM_ADMIN_APP);
    }

    public Boolean isLegacyFindPatientAppPresented() {
        return isAppButtonPresent(LEGACY_FIND_PATIENT);
    }

	@Override
    public String expectedUrlPath() {
    	return "/openmrs/referenceapplication/home.page";
    }
}

