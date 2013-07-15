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
    private String DISPENSING_MEDICATION_APP = "dispensing-app-homepageLink-dispensing-app-homepageLink-extension";

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

    public boolean isFindAPatientAppPresent() {
        return isAppButtonPresent(FIND_PATIENT);
    }

    public Boolean isRegisterPatientCustomizedForRefAppPresent() {
        return isAppButtonPresent(REGISTER_PATIENT_REF_APP);
    }

    public void openRegisterAPatientApp(){
        openApp(REGISTER_PATIENT_REF_APP);
    }

    public void openLegacyAdministrationApp(){
        openApp(SYSTEM_ADMIN_APP);
    }

<<<<<<< HEAD
    public Boolean isPatientRegistrationAppPresent() {
        return isAppButtonPresent(REGISTER_PATIENT);
    }

    public Boolean isActiveVisitsAppPresent() {
=======
    public Boolean isActiveVisitsAppPresented() {
>>>>>>> 8e69b4f84eadd3a73702e0e56ae0f168c1afb1bb
        return isAppButtonPresent(ACTIVE_VISITS);
    }

    public Boolean isStyleGuideAppPresent() {
        return isAppButtonPresent(STYLE_GUIDE);
    }

    public Boolean isSystemAdministrationAppPresent() {
        return isAppButtonPresent(SYSTEM_ADMIN_APP);
    }

    public Boolean isLegacyFindPatientAppPresent() {
        return isAppButtonPresent(LEGACY_FIND_PATIENT);
    }

    public Boolean isDispensingMedicationAppPresent(){
        return isAppButtonPresent(DISPENSING_MEDICATION_APP);
    }

	@Override
    public String expectedUrlPath() {
    	return "/openmrs/referenceapplication/home.page";
    }
}

