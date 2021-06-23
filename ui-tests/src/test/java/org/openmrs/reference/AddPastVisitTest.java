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
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.reference.page.FindPatientPage;
import org.openmrs.reference.page.PatientVisitsDashboardPage;
import org.openmrs.uitestframework.test.TestData;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AddPastVisitTest extends LocationSensitiveApplicationTestBase {

    private TestData.PatientInfo testPatient;

    @Before
    public void setUp() throws Exception {
        testPatient = createTestPatient();
    }

    @Test
    @Category(BuildTests.class)
    public void addPastVisitTest() {
        FindPatientPage findPatientPage = homePage.goToFindPatientRecord();
        findPatientPage.search(testPatient.identifier);
        ClinicianFacingPatientDashboardPage patientDashboardPage = findPatientPage.clickOnFirstPatient();
        PatientVisitsDashboardPage patientVisitsDashboardPage = patientDashboardPage.addPastVisit();
        assertThat(patientVisitsDashboardPage.getVisitList().get(0).getAttribute("class"), is(not("no-results")));
    }

    @After
    public void tearDown() throws Exception {
        deletePatient(testPatient);
    }
}

