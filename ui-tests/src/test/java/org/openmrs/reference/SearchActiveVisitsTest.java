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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.page.ActiveVisitsPage;

public class SearchActiveVisitsTest extends ReferenceApplicationTestBase {

    @Test
    @Category(BuildTests.class)
    @Ignore("It searches for active visit but doesn't add any")
    public void searchActiveVisitsByPatientNameOrIdOrLastSeenTest() throws Exception {
        ActiveVisitsPage activeVisitsPage = homePage.goToActiveVisitsSearch();

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
}
