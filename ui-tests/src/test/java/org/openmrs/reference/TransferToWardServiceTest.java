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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.uitestframework.test.TestData;

import static org.junit.Assert.assertTrue;

@Ignore
public class TransferToWardServiceTest extends LocationSensitiveApplicationTestBase {

    private static final String INPATIENT_WARD = "Inpatient Ward";

    private static final String ISOLATION_WARD = "Isolation Ward";

    private TestData.PatientInfo testPatient;

    @Before
    public void createTestData() throws Exception {
        testPatient = createTestPatient();
        String visitType = TestData.getAVisitType();
        String visit = new TestData.TestVisit(testPatient.uuid, visitType, getLocationUuid(homePage)).create();
    }

    @Test
    @Category(BuildTests.class)
    public void transferToWardServiceTest() {

        ClinicianFacingPatientDashboardPage patientDashboardPage = homePage.goToActiveVisitPatient();

        patientDashboardPage.clickOnLocation();
        patientDashboardPage.goToAdmitToInpatient().confirm(INPATIENT_WARD);
        patientDashboardPage.waitForPage();

        patientDashboardPage.clickOnLocation();
        patientDashboardPage.goToTransferToWardServicePage().confirm(ISOLATION_WARD);
        patientDashboardPage.waitForPage();

        assertTrue(patientDashboardPage.containsText(ISOLATION_WARD));
    }

    @After
    public void deleteTestPatient() {
        deletePatient(testPatient);
    }
}
