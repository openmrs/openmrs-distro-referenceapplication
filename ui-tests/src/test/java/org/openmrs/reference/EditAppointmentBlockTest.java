

package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.reference.page.*;
import org.openmrs.uitestframework.test.TestBase;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Created by nata on 08.07.2015.
 */
public class EditAppointmentBlockTest extends TestBase {
    private HomePage homePage;
    private HeaderPage headerPage;
    private AppointmentBlocksPage appointmentBlocksPage;

    @Before
    public void setUp() throws Exception {
        homePage = new HomePage(driver);
        loginPage.loginAsAdmin();
        assertPage(homePage);
        headerPage = new HeaderPage(driver);
        appointmentBlocksPage = new AppointmentBlocksPage(driver);
    }

    @Test
    public void editAppointmentBlockTest() throws Exception {
        appointmentBlocksPage.goToAppointmentBlock();
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
        appointmentBlocksPage.clickOnConfirmDelete();
    }
    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }
}

