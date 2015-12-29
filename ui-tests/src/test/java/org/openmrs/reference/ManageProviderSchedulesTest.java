

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
 * Created by nata on 17.07.2015.
 */
public class ManageProviderSchedulesTest extends TestBase {
    private HomePage homePage;
    private HeaderPage headerPage;
    private AppointmentBlocksPage appointmentBlocksPage;

    @Before
    public void setUp() throws Exception {
        
        homePage = new HomePage(page);
        assertPage(homePage);
        headerPage = new HeaderPage(driver);
        appointmentBlocksPage = new AppointmentBlocksPage(driver);
    }

    @Ignore //ignored due to RA-904
    @Test
    public void manageProviderSchedulesTest() throws Exception {
        appointmentBlocksPage.goToAppointmentBlock();
        appointmentBlocksPage.selectLocation("Isolation Ward");
        appointmentBlocksPage.clickOnCurrentDay();
        appointmentBlocksPage.selectLocationBlock("Isolation Ward");
        appointmentBlocksPage.enterStartTime("08");
        appointmentBlocksPage.enterService("gyne");
        appointmentBlocksPage.enterProvider("Super User");
        appointmentBlocksPage.clickOnSave();
        assertNotNull("Gynecology", appointmentBlocksPage.CURRENT_DAY);
        appointmentBlocksPage.findBlock();
        appointmentBlocksPage.clickOnEdit();
        appointmentBlocksPage.enterService("derma");
        appointmentBlocksPage.clickOnSave();
        assertNotNull("Dermatology", appointmentBlocksPage.CURRENT_DAY);
        appointmentBlocksPage.findBlock();
        appointmentBlocksPage.clickOnDelete();
        appointmentBlocksPage.clickOnConfirmDelete();}

    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }
}

