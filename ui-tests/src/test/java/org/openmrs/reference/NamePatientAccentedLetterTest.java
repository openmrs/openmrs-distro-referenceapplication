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

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.helper.PatientGenerator;
import org.openmrs.reference.helper.TestPatient;
import org.openmrs.reference.page.*;
import org.openmrs.uitestframework.test.TestBase;
import org.junit.*;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


public class NamePatientAccentedLetterTest extends ReferenceApplicationTestBase {
    private TestPatient patient;

    @Before
    public void setUp() throws Exception {

        patient = PatientGenerator.generateTestPatient();
    }

    @Test
    @Category(BuildTests.class)
    @Ignore("Test depends on server configuration, fails on fresh setup")
    public void namePatientAccentedLetterTest() throws Exception {
        RegistrationPage registrationPage = homePage.goToRegisterPatientApp();
        patient.givenName = "Mike";
        patient.familyName = "Kłoczkowski";
        patient.gender = "Male";
        registrationPage.enterPatient(patient);
        ClinicianFacingPatientDashboardPage dashboardPage = registrationPage.confirmPatient();
        String name = dashboardPage.getPatientFamilyName();
        assertThat(name, Matchers.is(patient.familyName));
        patient.uuid = dashboardPage.getPatientUuidFromUrl();
    }



    @After
    public void tearDown() throws Exception {
        deletePatient(patient.uuid);
        waitForPatientDeletion(patient.uuid);
    }

}
