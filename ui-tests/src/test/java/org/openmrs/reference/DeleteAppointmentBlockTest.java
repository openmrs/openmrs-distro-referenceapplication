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

import static org.junit.Assert.assertTrue;

@Ignore
public class DeleteAppointmentBlockTest extends ManageProviderSchedulesTest {

    int firstAppointmentIndex = 0;
    private String correctStartTimeFirtAppointment = "08";
    private String outboundStartTime = "30";
    private String firstAppointment = "Gynecology";

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    /*
     * @verifies creation and delete of an appointment
     */
    @Test
    public void deleteAppointmentBlockTest() throws Exception {

        //The logic behind the text is to create an appointment and delete it
        appointmentBlocksPage.selectLocation(locationName);
        appointmentBlocksPage.clickOnDay();
        /*
         * Here we make negative test: using outbound values for start time should result
         * that Save button is disabled
         */
        appointmentBlocksPage.enterStartTime(outboundStartTime);
        assertTrue(!appointmentBlocksPage.isSaveEnabled());
        appointmentBlocksPage.enterStartTime(correctStartTimeFirtAppointment);
        appointmentBlocksPage.enterService("gyne");
        appointmentBlocksPage.enterProvider(provider);
        appointmentBlocksPage.clickOnSave();
        assertTrue(appointmentBlocksPage.getServiceOfDay(firstAppointmentIndex).equals(firstAppointment));
        //let'remove the appointment
        appointmentBlocksPage.clickOnAppointment();
        appointmentBlocksPage.clickOnDelete();
        appointmentBlocksPage.clickOnleft();
        appointmentBlocksPage.clickOnConfirmDelete();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }
}
