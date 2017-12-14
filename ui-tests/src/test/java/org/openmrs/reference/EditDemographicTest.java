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

import org.apache.commons.lang.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.reference.page.FindPatientPage;
import org.openmrs.reference.page.RegistrationEditSectionPage;
import org.openmrs.uitestframework.test.TestData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class EditDemographicTest extends ReferenceApplicationTestBase {

    private TestData.PatientInfo testPatient;
    private String prefix;

    @Before
    public void setUp() throws Exception {
        testPatient = createTestPatient();
        prefix = RandomStringUtils.randomAlphanumeric(6);
    }

    @Test
    public void editDemographicTest() throws Exception {
        FindPatientPage findPatientPage = homePage.goToFindPatientRecord();
        findPatientPage.enterPatient(testPatient.identifier);
        ClinicianFacingPatientDashboardPage clinicianFacingPatientDashboardPage = findPatientPage.clickOnFirstPatient();
        RegistrationEditSectionPage registrationEditSectionPage = clinicianFacingPatientDashboardPage.clickOnEditPatient();
        registrationEditSectionPage.enterGivenName(testPatient.givenName+prefix);
        registrationEditSectionPage.enterMiddleName(testPatient.middleName+prefix);
        registrationEditSectionPage.enterFamilyName(testPatient.familyName+prefix);
        registrationEditSectionPage.selectPatientGender("Male");
        registrationEditSectionPage.clickOnBirthdateLabel();
        registrationEditSectionPage.enterBirthDay("21");
        registrationEditSectionPage.selectBirthMonth("May");
        registrationEditSectionPage.enterBirthYear("1992");
        registrationEditSectionPage.clickOnConfirmEdit();
        clinicianFacingPatientDashboardPage = registrationEditSectionPage.confirmPatient();
        assertThat(clinicianFacingPatientDashboardPage.getPatientGivenName(), is(testPatient.givenName+prefix));
    }

    @After
    public void tearDown() throws Exception {
        deletePatient(testPatient.uuid);
    }


}
