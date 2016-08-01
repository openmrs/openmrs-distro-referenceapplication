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

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.helper.PatientGenerator;
import org.openmrs.reference.helper.TestPatient;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.reference.page.EditDemographicsPage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class EditDemographicTest extends ReferenceApplicationTestBase {
    private EditDemographicsPage editDemographicsPage;
    private ClinicianFacingPatientDashboardPage patientDashboardPage;

    @Test
    @Category(BuildTests.class)
    public void editDemographicTest() throws Exception {
        TestPatient patient = PatientGenerator.generateTestPatient();

        patientDashboardPage = homePage.goToActiveVisitPatient();
        editDemographicsPage = patientDashboardPage.clickOnEditPatient();

        editDemographicsPage.enterPatientGivenName(patient.givenName);
        editDemographicsPage.enterPatientMiddleName(patient.middleName);
        editDemographicsPage.enterPatientFamilyName(patient.familyName);
        editDemographicsPage.clickOnGenderLabel();
        editDemographicsPage.selectPatientGender(patient.gender);
        editDemographicsPage.clickOnBirthdateLabel();
        editDemographicsPage.enterBirthDay(patient.birthDay);
        editDemographicsPage.selectBirthMonth(patient.birthMonth);
        editDemographicsPage.enterBirthYear(patient.birthYear);
        editDemographicsPage.clickOnConfirmEdit();
        editDemographicsPage.confirmPatient();

        String[] names = patientDashboardPage.name().split(" ");

        assertEquals(names[0], patient.givenName);
        assertEquals(names[names.length - 1], patient.familyName);

        assertEquals(patientDashboardPage.gender(), patient.gender);

        String[] birthdate = patientDashboardPage.birthdate().split("\\.");

        assertEquals(Integer.parseInt(birthdate[0]), Integer.parseInt(patient.birthDay));
        assertTrue(patient.birthMonth.contains(birthdate[1]));
        assertEquals(birthdate[2], patient.birthYear);
    }


}
