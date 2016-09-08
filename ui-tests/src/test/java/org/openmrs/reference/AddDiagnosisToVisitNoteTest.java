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
import org.openmrs.reference.page.VisitNotePage;
import org.openmrs.uitestframework.test.TestData;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

public class AddDiagnosisToVisitNoteTest extends ReferenceApplicationTestBase {

    private static final String VISIT_TYPE_UUID = "7b0f5697-27e3-40c4-8bae-f4049abfb4ed";

    private TestData.PatientInfo patient;

    @Before
    public void setup(){
        patient = createTestPatient();
        createTestVisit();
    }
    
    @Test
    @Category(BuildTests.class)
    public void AddDiagnosisToVisitNoteTest() throws Exception {

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
        //There's a validation error when deleting a patient with a visit note. Some obs has an invalid value and cannot be voided.
        //deletePatient(patient.uuid);
    }

    private void createTestVisit(){
        new TestData.TestVisit(patient.uuid, VISIT_TYPE_UUID, getLocationUuid(homePage)).create();
    }
}
