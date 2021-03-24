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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import static org.junit.Assert.assertTrue;

@Ignore
public class AddEditAppointmentBlockTest extends ManageProviderSchedulesTest {

	private final String startTimeFirstAppointment = "09";

	private String firstAppointment = "Gynecology";

	private String secondAppointment = "Dermatology";

	int firstAppointmentIndex = 0;

	int secondAppointmentIndex = 1;

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	/*
	 *@verifies the creation and editing of an appointment 
	 */
	@Test
	@Category(BuildTests.class)
	public void editAppointmentBlockTest() throws Exception {
	
		/*
		 * The logic behind the text is:
		 * create an appointment and assert that exists
		 * change the service and assert that it has been changed
		 * search the newly created service
		 * delete the appointment
		*/
		appointmentBlocksPage.selectLocation(locationName);
		appointmentBlocksPage.clickOnDay();
		appointmentBlocksPage.enterStartTime(startTimeFirstAppointment);
		appointmentBlocksPage.enterService("gyne");
		appointmentBlocksPage.enterProvider(provider);
		appointmentBlocksPage.clickOnSave();
		assertTrue(appointmentBlocksPage.getServiceOfDay(firstAppointmentIndex).equals(firstAppointment));
		appointmentBlocksPage.clickOnAppointment();
		appointmentBlocksPage.clickOnEdit();
		appointmentBlocksPage.removeAppointment();
		appointmentBlocksPage.enterService("derma");
		appointmentBlocksPage.clickOnSave();
		assertTrue(appointmentBlocksPage.getServiceOfDay(firstAppointmentIndex).equals(secondAppointment));
		appointmentBlocksPage.clickOnAppointment();
		appointmentBlocksPage.clickOnDelete();
		//click on "left area" is needed because the tooltip shadows the delete button
		appointmentBlocksPage.clickOnleft();
		appointmentBlocksPage.clickOnConfirmDelete();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
