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

import org.junit.After;
import org.junit.Before;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.AppointmentBlocksPage;
import org.openmrs.reference.page.AppointmentSchedulingPage;
import org.openmrs.uitestframework.test.TestData;

/**
 * This class is meant to create the preconditions (set-up) and to clean up the environment (tear
 * down) after the tests AddEditAppointmbetBlockTest and DeleteAppointmentBlocktTest have been
 * executed. Subclassing ManageProviderSchedulsTest is a possible solution for a common set-up.
 * Other possibilities are Rules or extending TestSetup. The latter possibilities might be applied
 * in future if the test code become hard to maintain/read
 */
public class ManageProviderSchedulesTest extends ReferenceApplicationTestBase {

	protected HomePage homePage;
	protected AppointmentBlocksPage appointmentBlocksPage;
	protected String locationName;
	protected String locationUuid;
	protected String provider;

	@Before
	public void setUp() throws Exception {
		homePage = new HomePage(page);
		assertPage(homePage.waitForPage());
		AppointmentSchedulingPage appointmentSchedulingPage = homePage.goToAppointmentScheduling();
		appointmentBlocksPage = appointmentSchedulingPage.goToAppointmentBlock();
		/*
		 * TODO The following code should be re-nabled when bug AM-186 is fixed
		 * Location might not be available when running this test, thus we create one
		 * locationName = "Location-"+TestData.randomSuffix();
		 * We must be sure that the location has been created, the test will fail otherwise
		 * locationUuid = TestData.createLocation(locationName);
		 * assertNotNull(locationUuid);
		*/
		locationName = "Isolation Ward";
		provider = "Provider " + TestData.randomSuffix();
	}

	@After
	public void tearDown() throws Exception {
		/*
		 *  TODO delete the location. Re-enable when bug AM-186 is fixed.
		 * TestData.permanetDelete(locationUuid); 
		 */
	}
}
