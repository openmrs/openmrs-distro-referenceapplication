/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

public class HomePage extends Page {

    private static final String FIND_PATIENT_APP_ID = "coreapps-activeVisitsHomepageLink-coreapps-activeVisitsHomepageLink-extension";
    private static final String REGISTER_PATIENT_APP_ID = "referenceapplication-registrationapp-registerPatient-homepageLink-referenceapplication-registrationapp-registerPatient-homepageLink-extension";
    private static final String ACTIVE_VISITS_APP_ID = "org-openmrs-module-coreapps-activeVisitsHomepageLink-org-openmrs-module-coreapps-activeVisitsHomepageLink-extension";
    private static final String APPOINTMENT_SCHEDULING_APP_ID = "appointmentschedulingui-homeAppLink-appointmentschedulingui-homeAppLink-extension";
    private static final String SYSTEM_ADMIN_APP_ID = "coreapps-systemadministration-homepageLink-coreapps-systemadministration-homepageLink-extension";
    private static final String CONFIGURE_METADATA_APP_ID = "org-openmrs-module-adminui-configuremetadata-homepageLink-org-openmrs-module-adminui-configuremetadata-homepageLink-extension";
    private static final String CAPTURE_VITALS_APP_ID = "referenceapplication-vitals-referenceapplication-vitals-extension";
    private static final String DATA_MANAGEMENT_APP_ID = "coreapps-datamanagement-homepageLink-coreapps-datamanagement-homepageLink-extension";
    private static final By CONFIGURE_METADATA = By.id("org-openmrs-module-adminui-configuremetadata-homepageLink-org-openmrs-module-adminui-configuremetadata-homepageLink-extension");
    private static final By MANAGE_FORM = By.id("formentryapp-forms-homepageLink-formentryapp-forms-homepageLink-extension");
    private static final By SYSTEM_ADMINISTRATION = By.id("coreapps-systemadministration-homepageLink-coreapps-systemadministration-homepageLink-extension");
    private static final By FIND_PATIENT_RECORD = By.id("coreapps-activeVisitsHomepageLink-coreapps-activeVisitsHomepageLink-extension");
    private static final By DATA_MANAGEMENT = By.id("coreapps-datamanagement-homepageLink-coreapps-datamanagement-homepageLink-extension");
    private static final By APPOINTMENT_SCHEDULING = By.id("appointmentschedulingui-homeAppLink-appointmentschedulingui-homeAppLink-extension");
    private static final By LOGGED_IN_USER = By.xpath("//*[@id='navbarSupportedContent']/ul/li[1]");

    public HomePage(Page page) {
        super(page);
    }

    private boolean isAppButtonPresent(String appId) {
        try {
            return driver.findElement(By.id(appId)) != null;
        } catch (Exception ex) {
            return false;
        }
    }

    public String getLoggedUsername() {
        return findElement(LOGGED_IN_USER).getText();
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

    public RegistrationPage goToRegisterPatientApp() throws InterruptedException {
        clickOn(By.id(REGISTER_PATIENT_APP_ID));
        return new RegistrationPage(this);
    }

    public SystemAdministrationPage goToSystemAdministrationPage() {
        clickOn(By.id(SYSTEM_ADMIN_APP_ID));
        return new SystemAdministrationPage(this);
    }

    public Boolean isActiveVisitsAppPresent() {
        return isAppButtonPresent(ACTIVE_VISITS_APP_ID);
    }

    public Boolean isAppointmentSchedulingAppPresent() {
        return isAppButtonPresent(APPOINTMENT_SCHEDULING_APP_ID);
    }

    public Boolean isSystemAdministrationAppPresent() {
        return isAppButtonPresent(SYSTEM_ADMIN_APP_ID);
    }

    public Boolean isConfigureMetadataAppPresent() {
        return isAppButtonPresent(CONFIGURE_METADATA_APP_ID);
    }

    public boolean isCaptureVitalsAppPresent() {
        return isAppButtonPresent(CAPTURE_VITALS_APP_ID);
    }

    public boolean isDataManagementAppPresent() {
        return isAppButtonPresent(DATA_MANAGEMENT_APP_ID);
    }

    public ClinicianFacingPatientDashboardPage goToActiveVisitPatient() {
        return goToActiveVisitsSearch().goToPatientDashboardOfLastActiveVisit();
    }

    public ActiveVisitsPage goToActiveVisitsSearch() {
        clickOn(By.id(ACTIVE_VISITS_APP_ID));
        return new ActiveVisitsPage(this);
    }

    public void goToManageForm() {
        clickOn(By.id(CONFIGURE_METADATA_APP_ID));
        clickOn(MANAGE_FORM);
    }

    public AdministrationPage goToAdministration() {
        clickOn(SYSTEM_ADMINISTRATION);
        return new SystemAdministrationPage(this).goToAdvancedAdministration();
    }

    public ConfigureMetadataPage goToConfigureMetadata() {
        clickOn(CONFIGURE_METADATA);
        return new ConfigureMetadataPage(this);
    }

    public FindPatientPage goToFindPatientRecord() {
        clickOn(FIND_PATIENT_RECORD);
        return new FindPatientPage(this);
    }

    public AppointmentSchedulingPage goToAppointmentScheduling() {
        clickOn(APPOINTMENT_SCHEDULING);
        return new AppointmentSchedulingPage(this);
    }

    public DataManagementPage goToDataManagement() {
        clickOn(DATA_MANAGEMENT);
        return new DataManagementPage(this);
    }

    @Override
    public String getPageUrl() {
        return "/referenceapplication/home.page";
    }

    @Override
    public String getPageAliasUrl() {
        return "/index.htm";
    }

}
