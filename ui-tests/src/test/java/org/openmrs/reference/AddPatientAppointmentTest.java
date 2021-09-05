/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.page.*;
import org.openmrs.uitestframework.test.TestData;

import static org.junit.Assert.assertTrue;

@Ignore
public class AddPatientAppointmentTest extends LocationSensitiveApplicationTestBase {

    private static final String SERVICE_NAME = "Oncology";
    private TestData.PatientInfo patient;

    @Before
    public void setUp() throws Exception {
        patient = createTestPatient();
        createTestVisit();
    }

    @Test
    @Category(BuildTests.class)
    public void addPatientAppointmentTest() throws Exception {
        AppointmentSchedulingPage appointmentSchedulingPage = homePage.goToAppointmentScheduling();
        ManageProviderSchedulesPage manageProviderSchedulesPage = appointmentSchedulingPage.goToManageProviderSchedules();
        manageProviderSchedulesPage.selectLocation(getLocationName());
        manageProviderSchedulesPage.clickOnCurrentDay();
        manageProviderSchedulesPage.selectLocationBlock(getLocationName());
        manageProviderSchedulesPage.enterMinimumTimeValue("06", "30");
        manageProviderSchedulesPage.clickOnStartTimeButton();
        manageProviderSchedulesPage.enterMaximumTimeValue("09", "30");
        manageProviderSchedulesPage.clickOnEndTimeButton();
        manageProviderSchedulesPage.enterService(SERVICE_NAME);
        manageProviderSchedulesPage.clickOnEndTimeButton();
        manageProviderSchedulesPage.clickOnSave();
        homePage = new HomePage(login());
        appointmentSchedulingPage = homePage.goToAppointmentScheduling();
        FindPatientPage findPatientPage = appointmentSchedulingPage.goToManageAppointments();
        findPatientPage.enterPatient(patient.getName());
        ManageAppointmentsPage manageAppointmentsPage = findPatientPage.clickOnFirstPatientAppointment();
        manageAppointmentsPage.clickOnViewAllTypes();
        manageAppointmentsPage.clickOnService(SERVICE_NAME);
        manageAppointmentsPage.searchAppointment();
        manageAppointmentsPage.clickAppointment();
        findPatientPage = manageAppointmentsPage.saveAppointment();
        findPatientPage.enterPatient(patient.getName());
        manageAppointmentsPage = findPatientPage.clickOnFirstPatientAppointment();
        assertTrue(manageAppointmentsPage.containsText(SERVICE_NAME));
    }

    @After
    public void tearDown() throws Exception {
        deletePatient(patient);
    }

    private void createTestVisit() {
        new TestData.TestVisit(patient.uuid, TestData.getAVisitType(), getLocationUuid(homePage)).create();
    }
}
