/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.page.ActiveVisitsPage;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.reference.page.RegistrationEditSectionPage;
import org.openmrs.uitestframework.test.TestData;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

public class XSSOnPhoneNumberFieldTest extends LocationSensitiveApplicationTestBase {

    private TestData.PatientInfo patient;

    @Before
    public void setup(){
        patient = createTestPatient();
        createTestVisit();
    }

    @Test
    @Category(BuildTests.class)
    public void  XSSOnPhoneNumberFieldTest() throws Exception {
        ActiveVisitsPage activeVisitsPage = homePage.goToActiveVisitsSearch();
        activeVisitsPage.search(patient.identifier);
        ClinicianFacingPatientDashboardPage patientDashboardPage = activeVisitsPage.goToPatientDashboardOfLastActiveVisit();
        patientDashboardPage.clickOnShowContact();
        RegistrationEditSectionPage registrationEditSectionPage = patientDashboardPage.clickOnEditContact();
        registrationEditSectionPage.clickOnPhoneNumberEdit();
        registrationEditSectionPage.clearPhoneNumber();
        registrationEditSectionPage.enterPhoneNumber("<script>alert(0)</script>");
        registrationEditSectionPage.clickOnConfirmEdit();
        assertThat(registrationEditSectionPage.getValidationErrors(), is(not(empty())));
        registrationEditSectionPage.clearPhoneNumber();
        registrationEditSectionPage.enterPhoneNumber("111111111");
        registrationEditSectionPage.clickOnConfirmEdit();
        patientDashboardPage = registrationEditSectionPage.confirmPatient();
        //Ignored as show contact may hide under success message
        //patientDashboardPage.clickOnShowContact();
        //assertThat(patientDashboardPage.getTelephoneNumber(), is("111111111"));
    }

    @After
    public void teardown(){
        deletePatient(patient.uuid);
    }

    private void createTestVisit(){
        new TestData.TestVisit(patient.uuid, TestData.getAVisitType(), getLocationUuid(homePage)).create();
    }
}

