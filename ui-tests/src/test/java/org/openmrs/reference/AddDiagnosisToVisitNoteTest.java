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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.helper.TestPatient;
import org.openmrs.reference.page.ActiveVisitsPage;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.reference.page.FindPatientPage;
import org.openmrs.reference.page.PatientVisitsDashboardPage;
import org.openmrs.reference.page.RegistrationPage;
import org.openmrs.reference.page.VisitNotePage;


public class AddDiagnosisToVisitNoteTest extends ReferenceApplicationTestBase {
    private ClinicianFacingPatientDashboardPage patientDashboardPage;
    private VisitNotePage visitNotePage;
    private ActiveVisitsPage activeVisits;
    private FindPatientPage findPatientPage;
    private RegistrationPage registrationPage;
    private TestPatient patient;
    private PatientVisitsDashboardPage patientVisitsDashboard;
    
    /* To make this test more stable, a new patient is registered, then given an active visit
     * The test is then performed on this new patient
     */ 
    @Before
    public void setUp() throws InterruptedException {
    	registrationPage = homePage.openRegisterAPatientApp();
    	patient = new TestPatient();
    	registrationPage.enterGenericPatient(patient);
    	patientDashboardPage = registrationPage.confirmPatient();
    	patientDashboardPage.goToHome();
    	findPatientPage = homePage.clickOnFindPatientRecord();
    	patientDashboardPage = findPatientPage.clickOnFirstPatient();
    	patientVisitsDashboard = patientDashboardPage.startVisit();
    	patientVisitsDashboard.goToHome();
    }
    
    @Test // RA-1117
    @Category(BuildTests.class)
    public void AddDiagnosisVisitNoteTest() throws InterruptedException {
        activeVisits = homePage.goToActiveVisitsSearch();
        patientDashboardPage = activeVisits.goToPatientDashboardOfLastActiveVisit();
        visitNotePage = patientDashboardPage.goToVisitNote();
        visitNotePage.enterDiagnosis("Pne");
        visitNotePage.enterSecondaryDiagnosis("Bleed");
        assertEquals("Pneumonia", visitNotePage.primaryDiagnosis());
        assertEquals("Bleeding", visitNotePage.secondaryDiagnosis());
        visitNotePage.save();
        assertNotNull(patientDashboardPage.visitLink());
        patientDashboardPage.endVisit();
    }
}
