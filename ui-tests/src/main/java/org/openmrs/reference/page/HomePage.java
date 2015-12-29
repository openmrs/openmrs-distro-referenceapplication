package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends Page {

    static final String FIND_PATIENT_APP_ID = "coreapps-activeVisitsHomepageLink-coreapps-activeVisitsHomepageLink-extension";
    static final String REGISTER_PATIENT_APP_ID = "referenceapplication-registrationapp-registerPatient-homepageLink-referenceapplication-registrationapp-registerPatient-homepageLink-extension";
    static final String ACTIVE_VISITS_APP_ID = "org-openmrs-module-coreapps-activeVisitsHomepageLink-org-openmrs-module-coreapps-activeVisitsHomepageLink-extension";
    static final String STYLE_GUIDE_APP_ID = "referenceapplication-styleGuide-referenceapplication-styleGuide-extension";
    static final String SYSTEM_ADMIN_APP_ID = "coreapps-systemadministration-homepageLink-coreapps-systemadministration-homepageLink-extension";
    static final String CONFIGURE_METADATA_APP_ID = "coreapps-configuremetadata-homepageLink-coreapps-configuremetadata-homepageLink-extension";
    static final String DISPENSING_MEDICATION_APP_ID = "dispensing-app-homepageLink-dispensing-app-homepageLink-extension";
    static final String CAPTURE_VITALS_APP_ID = "referenceapplication-vitals-referenceapplication-vitals-extension";
    static final By ACTIVE_PATIENT = By.xpath("//td[2]/a");
    static final By MANAGE_FORM = By.id("formentryapp-forms-homepageLink-formentryapp-forms-homepageLink-extension");
    static final By SYSTEM_ADMINISTRATION = By.id("coreapps-systemadministration-homepageLink-coreapps-systemadministration-homepageLink-extension");
    static final By ADVANCED_ADMINISTRATION = By.id("referenceapplication-legacyAdmin-app");
    static final By FIND_PATIENT_RECORD = By.cssSelector("i.icon-search");
    static final By DATA_MANAGAMENT = By.id("coreapps-datamanagement-homepageLink-coreapps-datamanagement-homepageLink-extension");
    static final By PATIENT_HEADER = By.className("patient-header");

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

    private void openApp(String appIdentifier) throws InterruptedException {
        driver.get(properties.getWebAppUrl());
        clickOn(By.id(appIdentifier));
    }

    public void captureVitals() {
        clickOn(By.id(CAPTURE_VITALS_APP_ID));
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

    public void openRegisterAPatientApp() throws InterruptedException {
        openApp(REGISTER_PATIENT_APP_ID);
    }

    public void openLegacyAdministrationApp()  throws InterruptedException{
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

    public void goToActiveVisitPatient(){
        clickOn(By.id(ACTIVE_VISITS_APP_ID));
        waitForElement(ACTIVE_PATIENT);
        clickOn(ACTIVE_PATIENT);
//        waitForElement(PATIENT_HEADER);
    }

    public void goToActiveVisitsSearch() {
        clickOn(By.id(ACTIVE_VISITS_APP_ID));
    }

    public void goToManageForm() {
        clickOn(By.id(CONFIGURE_METADATA_APP_ID));
        clickOn(MANAGE_FORM);
    }

    public void goToAdministration() {
        clickOn(SYSTEM_ADMINISTRATION);
        clickOn(ADVANCED_ADMINISTRATION);
    }

    public void clickOnFindPatientRecord(){ clickOn(FIND_PATIENT_RECORD);}
    public void goToDataMagament(){ clickOn(DATA_MANAGAMENT);}


    @Override
    public String getPageUrl() {
        return "/referenceapplication/home.page";
    }

    @Override
    public String getPageAliasUrl() {
    	return "/index.htm";
    }

}
