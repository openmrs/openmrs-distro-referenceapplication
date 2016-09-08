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
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.reference.page.FindPatientPage;
import org.openmrs.reference.page.MergeVisitsPage;
import org.openmrs.reference.page.PatientVisitsDashboardPage;
import org.openmrs.uitestframework.test.TestData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MergeVisitsTest extends ReferenceApplicationTestBase {

    private TestData.PatientInfo patient;

    @Before
    public void setUp() {
        patient = createTestPatient();
        new TestData.TestVisit(patient.uuid, TestData.getAVisitType(), getLocationUuid(homePage)).create();
    }

    @Test
    @Category(BuildTests.class)
    public void mergeVisitsTest() {
        FindPatientPage findPatientPage = homePage.goToFindPatientRecord();
        findPatientPage.enterPatient(patient.identifier);
        ClinicianFacingPatientDashboardPage clinicianFacingPatientDashboardPage = findPatientPage.clickOnFirstPatient();
        clinicianFacingPatientDashboardPage.addPastVisit();
        clinicianFacingPatientDashboardPage.clickChangeDate();
        PatientVisitsDashboardPage patientVisitsDashboardPage = clinicianFacingPatientDashboardPage.enterDate();
        patientVisitsDashboardPage.clickOnActions();
        MergeVisitsPage mergeVisitsPage = patientVisitsDashboardPage.clickOnMergeVisits();
        mergeVisitsPage.checkFirstVisit();
        mergeVisitsPage.checkSecondVisit();
        mergeVisitsPage= mergeVisitsPage.clickOnMergeSelecetdVisits();
        assertThat(mergeVisitsPage.getAllVisit().size(), is(1));
    }

    @After
    public void tearDown() throws InterruptedException {
        deletePatient(patient.uuid);
    }
}
