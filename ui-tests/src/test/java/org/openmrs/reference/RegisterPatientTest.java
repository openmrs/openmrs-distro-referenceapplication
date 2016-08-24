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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.helper.PatientGenerator;
import org.openmrs.reference.helper.TestPatient;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.reference.page.RegistrationPage;

public class RegisterPatientTest extends ReferenceApplicationTestBase {
	private TestPatient patient;

    @After
    public void tearDown() throws Exception {
        deletePatient(patient.uuid);
        waitForPatientDeletion(patient.uuid);
    }

    // Test for Story RA-71
    @Ignore("RA-1187")
    @Test
    @Category(org.openmrs.reference.groups.BuildTests.class)
    public void registerPatient() throws InterruptedException {
        RegistrationPage registrationPage = homePage.goToRegisterPatientApp();
        patient = PatientGenerator.generateTestPatient();
        registrationPage.enterPatient(patient);

        String address = patient.address1 + ", " +
        		patient.address2 + ", " +
        		patient.city + ", " +
        		patient.state + ", " +
        		patient.country + ", " +
        		patient.postalCode;

        assertThat(registrationPage.getNameInConfirmationPage(), containsString(patient.givenName + ", " + patient.familyName));
        assertThat(registrationPage.getGenderInConfirmationPage(), containsString(patient.gender));
        assertThat(registrationPage.getBirthdateInConfirmationPage(), containsString(patient.birthDay + ", " + patient.birthMonth + ", " + patient.birthYear));
        assertThat(registrationPage.getAddressInConfirmationPage(), containsString(address));
        assertThat(registrationPage.getPhoneInConfirmationPage(), containsString(patient.phone));

        ClinicianFacingPatientDashboardPage dashboardPage = registrationPage.confirmPatient();
        patient.uuid = patientIdFromUrl();
		assertThat(dashboardPage.getPatientGivenName(), is(patient.givenName));
        assertThat(dashboardPage.getPatientFamilyName(), is(patient.familyName));
    }

    // Test for RA-472
    @Ignore("RA-1187")
    @Test
    public void registerUnidentifiedPatient() throws InterruptedException {
        RegistrationPage registrationPage = homePage.goToRegisterPatientApp();
        patient = PatientGenerator.generateTestPatient();
        registrationPage.enterUnidentifiedPatient(patient);

        assertTrue(registrationPage.getGenderInConfirmationPage().contains(patient.gender));

        ClinicianFacingPatientDashboardPage dashboardPage = registrationPage.confirmPatient();
        patient.uuid = patientIdFromUrl();
        assertThat(dashboardPage.getPatientGivenName(), is("UNKNOWN"));
        assertThat(dashboardPage.getPatientFamilyName(), is("UNKNOWN"));
    }
}
