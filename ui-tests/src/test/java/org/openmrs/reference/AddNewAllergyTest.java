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
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.page.ActiveVisitsPage;
import org.openmrs.reference.page.AddOrEditAllergyPage;
import org.openmrs.reference.page.AllergyPage;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.uitestframework.test.TestData;

import static org.junit.Assert.assertTrue;

public class AddNewAllergyTest extends ReferenceApplicationTestBase {

    private static final String DRUG_NAME = "Aspirin";
    private TestData.PatientInfo patient;

    @Before
    public void setUp() throws Exception {
        patient = createTestPatient();
        createTestVisit();
    }

    @Test
    @Category(BuildTests.class)
    public void addNewAllergyTest() {
        ActiveVisitsPage activeVisitsPage = homePage.goToActiveVisitsSearch();
        activeVisitsPage.search(patient.identifier);
        ClinicianFacingPatientDashboardPage patientDashboardPage = activeVisitsPage.goToPatientDashboardOfLastActiveVisit();

        AllergyPage allergyPage = patientDashboardPage.clickOnAllergiesWidgetLink();

        AddOrEditAllergyPage addOrEditAllergyPage = allergyPage.clickOnAddNewAllergy();
        addOrEditAllergyPage.enterDrug(DRUG_NAME);
        addOrEditAllergyPage.drugId();
        allergyPage = addOrEditAllergyPage.clickOnSaveAllergy();

        assertTrue(allergyPage.getAllergen().contains(DRUG_NAME));
    }

    @After
    public void tearDown() throws Exception {
        deletePatient(patient);
    }

    private void createTestVisit() {
        new TestData.TestVisit(patient.uuid, TestData.getAVisitType(), getLocationUuid(homePage)).create();
    }

}
