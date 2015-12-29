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

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.ActiveVisitsPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.uitestframework.test.TestBase;

public class ActiveVisitsSearchPatientTest extends TestBase {

    private HomePage homePage;
    private ActiveVisitsPage activeVisitsPage;

    @Before
    public void setUp() throws Exception {
        loginPage.loginAsAdmin();
        homePage = new HomePage(driver);
        assertPage(homePage);
        activeVisitsPage = new ActiveVisitsPage(driver);
    }

    @Test
    public void searchPatientTest() throws Exception {
        homePage.goToActiveVisitsSearch();
        String patientName = activeVisitsPage.getPatientName();
        activeVisitsPage.search(patientName);
        assertTrue(activeVisitsPage.getPatientName().contains(patientName));
        String patientId = activeVisitsPage.getPatientId();
        activeVisitsPage.search(patientId);
        assertTrue(activeVisitsPage.getPatientId().contains(patientId));
        String lastSeen = activeVisitsPage.getPatientLastSeen();
        activeVisitsPage.search(lastSeen);
        assertTrue(activeVisitsPage.getPatientLastSeen().contains(lastSeen));
    }
}
