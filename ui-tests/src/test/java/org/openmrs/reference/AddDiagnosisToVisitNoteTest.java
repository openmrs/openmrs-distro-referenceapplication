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

import org.junit.Test;
import org.junit.Ignore;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.reference.page.VisitNotePage;


public class AddDiagnosisToVisitNoteTest extends ReferenceApplicationTestBase {
    private ClinicianFacingPatientDashboardPage patientDashboardPage;
    private VisitNotePage visitNotePage;
    
    @Test
    @Category(BuildTests.class)
    public void AddDiagnosisToVisitNoteTest() throws Exception {
    	
        patientDashboardPage = homePage.goToActiveVisitPatient();
        visitNotePage = patientDashboardPage.goToVisitNote();
        visitNotePage.enterDiagnosis("Pne");
        visitNotePage.enterSecondaryDiagnosis("Bleed");
        assertEquals("Pneumonia", visitNotePage.primaryDiagnosis());
        assertEquals("Bleeding", visitNotePage.secondaryDiagnosis());
        visitNotePage.save();
        visitNotePage.waitForElementWithSpecifiedMaxTimeout(ClinicianFacingPatientDashboardPage.VISIT_LINK, 30L);
        patientDashboardPage.endVisit();

    }
}
