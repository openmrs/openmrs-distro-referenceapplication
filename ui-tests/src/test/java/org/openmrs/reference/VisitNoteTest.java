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

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.page.ActiveVisitsPage;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.reference.page.EditVisitNotePage;
import org.openmrs.reference.page.PatientVisitsDashboardPage;
import org.openmrs.reference.page.VisitNotePage;
import org.openmrs.uitestframework.test.TestData;
import org.openmrs.uitestframework.test.TestData.PatientInfo;

public class VisitNoteTest extends LocationSensitiveApplicationTestBase {

    private static final String DIAGNOSIS_PRIMARY = "Gum Cancer";
    private static final String DIAGNOSIS_SECONDARY = "Malaria";
    private static final String DIAGNOSIS_SECONDARY_UPDATED = "Pneumonia";
    private PatientVisitsDashboardPage patientVisitsDashboardPage;
    private PatientInfo patient;

    @Before
    public void setup() {
        patient = createTestPatient();
        createTestVisit();
        patientVisitsDashboardPage = new PatientVisitsDashboardPage(page);  
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
        assertEquals(DIAGNOSIS_SECONDARY, visitNotePage.secondaryDiagnosis());
        assertEquals(DIAGNOSIS_PRIMARY, visitNotePage.primaryDiagnosis());
        visitNotePage.addNote("This is a note");
        patientDashboardPage = visitNotePage.save();
        patientDashboardPage.waitForPage();
        patientVisitsDashboardPage = patientDashboardPage.goToRecentVisits();
        EditVisitNotePage editVisitNotePage = patientVisitsDashboardPage.goToEditVisitNote();
        
        //Editing visit Note 
        editVisitNotePage.waitForPage();
        editVisitNotePage.removeDiagnosis();
        editVisitNotePage.clearNote();
        editVisitNotePage.addNote("This is edited note");
        editVisitNotePage.addSecondaryDiagnosis(DIAGNOSIS_SECONDARY_UPDATED);
        assertEquals(DIAGNOSIS_SECONDARY_UPDATED, patientDashboardPage.secondaryDiagnosis());
        patientDashboardPage = visitNotePage.save();
      
        //Add a new  diagnosis
        patientDashboardPage.goToVisitNote();
        editVisitNotePage.selectProviderAndLocation();
        editVisitNotePage.addDiagnosis(DIAGNOSIS_PRIMARY);
        editVisitNotePage.addSecondaryDiagnosis(DIAGNOSIS_SECONDARY);
        assertEquals(DIAGNOSIS_SECONDARY, visitNotePage.secondaryDiagnosis());
        assertEquals(DIAGNOSIS_PRIMARY, visitNotePage.primaryDiagnosis());
        editVisitNotePage.addNote("This is a new note");
        patientDashboardPage = visitNotePage.save();
        
        //Deleting visit Note.....
        visitNotePage.viewVisitNote();
        visitNotePage.deleteDiagnosis();
        visitNotePage.confirmDeleteDiagnosis();
    }

    @After
    public void tearDown() throws Exception {
        deletePatient(patient);
    }

    private void createTestVisit() {
        new TestData.TestVisit(patient.uuid, TestData.getAVisitType(), getLocationUuid(homePage)).create();
    }

}
