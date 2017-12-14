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

public class AppointmentSchedulingPage extends Page {

    private static final By MANAGE_SERVICES_TYPES = By.id("appointmentschedulingui-manageAppointmentTypes-app");
    private static final By MANAGE_PROVIDER_SCHEDULES = By.id("appointmentschedulingui-scheduleProviders-app");
    private static final By MANAGE_APPOINTMENTS = By.id("appointmentschedulingui-manageAppointments-app");
    private static final By DAILY_APPOINTMENTS = By.id("appointmentschedulingui-scheduledAppointments-app");

    public AppointmentSchedulingPage(Page page) {
        super(page);
    }

    public ManageProviderSchedulesPage goToManageProviderSchedules() {
        clickOn(MANAGE_PROVIDER_SCHEDULES);
        return new ManageProviderSchedulesPage(this);
    }

    public FindPatientPage goToManageAppointments() {
        clickOn(MANAGE_APPOINTMENTS);
        return new FindPatientPage(this);
    }

    public ManageServiceTypesPage goToManageServices() {
        clickOn(MANAGE_SERVICES_TYPES);
        return new ManageServiceTypesPage(this);
    }

    public AppointmentBlocksPage goToAppointmentBlock(){
        clickOn(MANAGE_PROVIDER_SCHEDULES);
        return new AppointmentBlocksPage(this);
    }

    @Override
    public String getPageUrl() {
        return "/appointmentschedulingui/home.page";
    }
}