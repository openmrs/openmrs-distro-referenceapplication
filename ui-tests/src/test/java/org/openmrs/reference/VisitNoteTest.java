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
import org.openmrs.reference.page.*;
import org.openmrs.uitestframework.test.TestData;
import org.openmrs.uitestframework.test.TestData.PatientInfo;

import static org.junit.Assert.assertEquals;

public class VisitNoteTest extends LocationSensitiveApplicationTestBase {

    private static final String DIAGNOSIS_PRIMARY = "CANCER";
    private static final String DIAGNOSIS_SECONDARY = "MALARIA";
    private static final String DIAGNOSIS_SECONDARY_UPDATED = "Pneumonia";
    private PatientVisitsDashboardPage patientVisitsDashboardPage;
    private EditVisitNotePage editVisitNotePage;
    

    private PatientInfo patient;

    @Before
    public void setup() {
        patient = createTestPatient();
        createTestVisit();
        patientVisitsDashboardPage = new PatientVisitsDashboardPage(page);
        editVisitNotePage = new  EditVisitNotePage(page);     
    }

    @Test
    @Category(BuildTests.class)
    public void visitNoteTest() throws Exception {


        ActiveVisitsPage activeVisitsPage = homePage.goToActiveVisitsSearch();
        activeVisitsPage.search(patient.identifier);
        ClinicianFacingPatientDashboardPage patientDashboardPage = activeVisitsPage.goToPatientDashboardOfLastActiveVisit();
        VisitNotePage visitNotePage = patientDashboardPage.goToVisitNote();

        visitNotePage.selectProviderAndLocation();
        visitNotePage.addDiagnosis(DIAGNOSIS_PRIMARY);
        visitNotePage.addSecondaryDiagnosis(DIAGNOSIS_SECONDARY);
        visitNotePage.addNote("This is a note");
        patientDashboardPage = visitNotePage.save();
        patientDashboardPage.waitForPage();
//      assertEquals(DIAGNOSIS_PRIMARY, visitNotePage.primaryDiagnosis());
//      assertEquals(DIAGNOSIS_SECONDARY, visitNotePage.secondaryDiagnosis());
        patientDashboardPage.waitForPage();
        patientVisitsDashboardPage = patientDashboardPage.goToRecentVisits();

        EditVisitNotePage editVisitNotePage = patientVisitsDashboardPage.goToEditVisitNote();
//    Deleting visit Diagnosis
        editVisitNotePage.deleteDiagnosis();
        editVisitNotePage.confirmDeleteDiagnosis();
        patientVisitsDashboardPage.waitForPage();
        patientVisitsDashboardPage.goToVisitNote();
        
//      Add a new  diagnosis
        visitNotePage.selectProviderAndLocation();
        visitNotePage.addDiagnosis(DIAGNOSIS_PRIMARY);
        visitNotePage.addSecondaryDiagnosis(DIAGNOSIS_SECONDARY);
        visitNotePage.addNote("This is a note");
        patientDashboardPage = visitNotePage.save();
 
 //     Editing visit Note 
        editVisitNotePage.waitForPage();
        editVisitNotePage = patientVisitsDashboardPage.goToEditVisitNote();
        editVisitNotePage.EditVisitNote();
        editVisitNotePage.addSecondaryDiagnosis(DIAGNOSIS_SECONDARY_UPDATED);
        assertEquals(DIAGNOSIS_SECONDARY_UPDATED, patientDashboardPage.secondaryDiagnosis());
        patientVisitsDashboardPage = patientDashboardPage.goToRecentVisits();

        //TODO Delete function doesn't work on int02
        patientVisitsDashboardPage.deleteVisitNote();
        patientDashboardPage.confirmDeletion();
        //Assert that visit note is not present on encounter list and its done
    }

    @After
    public void tearDown() throws Exception {
        deletePatient(patient);
    }

    private void createTestVisit() {
        new TestData.TestVisit(patient.uuid, TestData.getAVisitType(), getLocationUuid(homePage)).create();
    }

}
