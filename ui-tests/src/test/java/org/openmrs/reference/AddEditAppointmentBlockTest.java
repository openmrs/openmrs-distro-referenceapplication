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
import org.openmrs.reference.page.*;
import org.openmrs.uitestframework.test.TestBase;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class AddEditAppointmentBlockTest extends ReferenceApplicationTestBase {

    @Test
    @Ignore
    @Category(BuildTests.class)
    public void editAppointmentBlockTest() throws Exception {
        AppointmentSchedulingPage appointmentSchedulingPage = homePage.goToAppointmentScheduling();
        AppointmentBlocksPage appointmentBlocksPage = appointmentSchedulingPage.goToAppointmentBlock();

        appointmentBlocksPage.selectLocation("Outpatient Clinic");
        appointmentBlocksPage.clickOnCurrentDay();
        appointmentBlocksPage.selectLocationBlock("Outpatient Clinic");
        appointmentBlocksPage.enterService("derm");
        appointmentBlocksPage.clickOnSave();

        assertNotNull("Dermatology", appointmentBlocksPage.CURRENT_DAY);

        appointmentBlocksPage.findBlock();
        appointmentBlocksPage.clickOnEdit();
        appointmentBlocksPage.enterProvider("Jake Smith");
        appointmentBlocksPage.clickOnServiceDelete();
        appointmentBlocksPage.enterService("onco");
        appointmentBlocksPage.clickOnSave();

        assertNotNull("Oncology", appointmentBlocksPage.CURRENT_DAY);

        appointmentBlocksPage.findBlock();
        appointmentBlocksPage.clickOnDelete();
        appointmentBlocksPage.clickOnClose();
        appointmentBlocksPage.clickOnConfirmDelete();
    }
}

