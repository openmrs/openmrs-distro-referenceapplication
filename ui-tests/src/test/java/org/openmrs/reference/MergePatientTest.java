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

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.page.*;
import org.junit.*;
import org.openmrs.uitestframework.test.TestData;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MergePatientTest extends ReferenceApplicationTestBase {
    TestData.PatientInfo testPatient;
    TestData.PatientInfo testPatient1;

    @Before
    public void setUp() throws Exception {

        testPatient = createTestPatient();
        testPatient1 = createTestPatient();
    }

    @Test
    @Category(BuildTests.class)
    public void mergePatientByNameTest() throws Exception {

        DataManagementPage dataManagementPage = homePage.goToDataManagement();
        MergePatientsPage mergePatientsPage = dataManagementPage.goToMergePatient();
        mergePatientsPage.enterPatient1(testPatient.identifier);
        mergePatientsPage.enterPatient2(testPatient1.identifier);
        mergePatientsPage.clickOnContinue();
        mergePatientsPage.clickOnMergePatient();
        PatientVisitsDashboardPage dashboardPage = mergePatientsPage.clickOnContinue();

        assertThat(dashboardPage.getPatientFamilyName(), is(testPatient1.familyName));
    }



    @After
    public void tearDown() throws Exception {
        deletePatient(testPatient.uuid);
        deletePatient(testPatient1.uuid);
    }

}
