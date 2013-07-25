package org.openmrs.reference.page;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends AbstractBasePage{

    private String findPatientApp = "coreapps-activeVisitsHomepageLink-coreapps-activeVisitsHomepageLink-extension";
    private String registerPatientApp = "referenceapplication-registrationapp-registerPatient-homepageLink-referenceapplication-registrationapp-registerPatient-homepageLink-extension";
    private String activeVisitsApp = "org-openmrs-module-coreapps-activeVisitsHomepageLink-org-openmrs-module-coreapps-activeVisitsHomepageLink-extension";
    private String styleGuideApp = "referenceapplication-styleGuide-referenceapplication-styleGuide-extension";
    private String systemAdminApp = "legacy-admin-legacy-admin-extension";
    private String dispensingMedicationApp = "dispensing-app-homepageLink-dispensing-app-homepageLink-extension";

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
        return isAppButtonPresent(findPatientApp);
    }

    public Boolean isRegisterPatientCustomizedForRefAppPresent() {
        return isAppButtonPresent(registerPatientApp);
    }

    public void openRegisterAPatientApp(){
        openApp(registerPatientApp);
    }

    public void openLegacyAdministrationApp(){
        openApp(systemAdminApp);
    }

    public Boolean isActiveVisitsAppPresent() {
        return isAppButtonPresent(activeVisitsApp);
    }

    public Boolean isStyleGuideAppPresent() {
        return isAppButtonPresent(styleGuideApp);
    }

    public Boolean isSystemAdministrationAppPresent() {
        return isAppButtonPresent(systemAdminApp);
    }

    public Boolean isDispensingMedicationAppPresent(){
        return isAppButtonPresent(dispensingMedicationApp);
    }

	@Override
    public String expectedUrlPath() {
    	return "/openmrs/referenceapplication/home.page";
    }
}

