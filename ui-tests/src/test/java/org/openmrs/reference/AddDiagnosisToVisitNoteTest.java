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

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.page.ActiveVisitsPage;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.reference.page.VisitNotePage;
import org.openmrs.uitestframework.test.TestData;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class AddDiagnosisToVisitNoteTest extends LocationSensitiveApplicationTestBase {

    private TestData.PatientInfo patient;

    @Before
    public void setup() {
        patient = createTestPatient();
        createTestVisit();
    }

    @Test
    @Category(BuildTests.class)
    public void addDiagnosisToVisitNoteTest() {
        ActiveVisitsPage activeVisitsPage = homePage.goToActiveVisitsSearch();
        activeVisitsPage.search(patient.identifier);

        ClinicianFacingPatientDashboardPage patientDashboardPage = activeVisitsPage.goToPatientDashboardOfLastActiveVisit();
        VisitNotePage visitNotePage = patientDashboardPage.goToVisitNote();
        visitNotePage.enterDiagnosis("Pne");
        visitNotePage.enterSecondaryDiagnosis("Bleed");
        assertEquals("Pneumonia", visitNotePage.primaryDiagnosis());
        assertEquals("Bleeding", visitNotePage.secondaryDiagnosis());

        patientDashboardPage = visitNotePage.save();

        List<String> diagnoses = patientDashboardPage.getDiagnoses();
        assertThat(diagnoses, hasItems("Pneumonia", "Bleeding"));
    }

    @After
    public void tearDown() throws Exception {
        deletePatient(patient);
    }

    private void createTestVisit() {
        new TestData.TestVisit(patient.uuid, TestData.getAVisitType(), getLocationUuid(homePage)).create();
    }
}
