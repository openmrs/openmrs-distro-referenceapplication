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

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.page.ActiveVisitsPage;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.reference.page.RegistrationEditSectionPage;
import org.openmrs.uitestframework.test.TestData;

public class XSSOnPhoneNumberFieldTest extends LocationSensitiveApplicationTestBase {

    private TestData.PatientInfo patient;

    @Before
    public void setup() {
        patient = createTestPatient();
        createTestVisit();
    }

    @Test
    @Category(BuildTests.class)
    public void XSSOnPhoneNumberFieldTest() throws Exception {
        ActiveVisitsPage activeVisitsPage = homePage.goToActiveVisitsSearch();
        activeVisitsPage.search(patient.identifier);
        ClinicianFacingPatientDashboardPage patientDashboardPage = activeVisitsPage.goToPatientDashboardOfLastActiveVisit();
        patientDashboardPage.clickOnShowContact();
        RegistrationEditSectionPage registrationEditSectionPage = patientDashboardPage.clickOnEditContact();
        registrationEditSectionPage.clickOnPhoneNumberEdit();
        registrationEditSectionPage.clearPhoneNumber();
        registrationEditSectionPage.enterPhoneNumber("<script>alert(0)</script>");
        registrationEditSectionPage.clickOnConfirmEdit();
        assertTrue(registrationEditSectionPage.getInvalidPhoneNumberNotification().contains(("Must be a valid phone number (with +, -, numbers or parentheses)")));
        registrationEditSectionPage.clearPhoneNumber();
        registrationEditSectionPage.enterPhoneNumber("111111111");
        registrationEditSectionPage.clickOnConfirmEdit();
        patientDashboardPage = registrationEditSectionPage.confirmPatient();
    }

    @After
    public void teardown() {
        deletePatient(patient);
    }

    private void createTestVisit() {
        new TestData.TestVisit(patient.uuid, TestData.getAVisitType(), getLocationUuid(homePage)).create();
    }
}

