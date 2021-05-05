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
import org.openmrs.reference.page.ActiveVisitsPage;
import org.openmrs.uitestframework.test.TestData;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SearchActiveVisitsTest extends ReferenceApplicationTestBase {

    TestData.PatientInfo patient;

    @Before
    public void setup() {
        patient = createTestPatient();
        new TestData.TestVisit(patient.uuid, TestData.getAVisitType(), getLocationUuid(homePage)).create();
    }

    @Test
<<<<<<< HEAD
    @Category(BuildTests.class)
=======
    @Category(org.openmrs.reference.groups.BuildTests.class)
>>>>>>> 578e892... RATEST-155T
    public void searchActiveVisitsByPatientNameOrIdOrLastSeenTest() throws Exception {
        ActiveVisitsPage activeVisitsPage = homePage.goToActiveVisitsSearch();
        activeVisitsPage.search(patient.identifier);

        String patientName = activeVisitsPage.getPatientNameOfLastActiveVisit();
        activeVisitsPage.search(patientName);
        assertThat(activeVisitsPage.getPatientNameOfLastActiveVisit(), is(equalTo(patientName)));

        activeVisitsPage.search("");

        String patientId = activeVisitsPage.getPatientIdOfLastActiveVisit();
        activeVisitsPage.search(patientId);
        assertThat(activeVisitsPage.getPatientIdOfLastActiveVisit(), is(equalTo(patientId)));

        activeVisitsPage.search("");

        String lastSeen = activeVisitsPage.getPatientLastSeenValueOfLastActiveVisit();
        activeVisitsPage.search(lastSeen);
        assertThat(activeVisitsPage.getPatientLastSeenValueOfLastActiveVisit(), is(equalTo(lastSeen)));
    }

    @After
    public void tearDown() throws Exception {
        deletePatient(patient);
    }
}
