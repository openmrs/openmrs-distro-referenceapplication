

package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.reference.page.*;
import org.openmrs.uitestframework.test.TestBase;
import static org.junit.Assert.assertTrue;


/**
 * Created by nata on 21.07.2015.
 */
public class AddPatientAppointmentTest extends TestBase {
    private HeaderPage headerPage;
    private AppointmentBlocksPage appointmentBlocksPage;
    private ManageAppointmentPage manageAppointmentPage;
    private FindPatientPage findPatientPage;
    private HomePage homePage;
    private PatientDashboardPage patientDashboardPage;

    @Before
    public void setUp() throws Exception {
        homePage = new HomePage(driver);
        loginPage.loginAsAdmin();
        assertPage(homePage);
        headerPage = new HeaderPage(driver);
        appointmentBlocksPage = new AppointmentBlocksPage(driver);
        manageAppointmentPage = new ManageAppointmentPage(driver);
        findPatientPage = new FindPatientPage(driver);
        patientDashboardPage = new PatientDashboardPage(driver);
    }
    @Ignore
    @Test
    public void addPatientAppointmentTest() throws Exception {
        appointmentBlocksPage.goToAppointmentBlock();
        appointmentBlocksPage.selectLocation("Outpatient Clinic");
        appointmentBlocksPage.clickOnCurrentDay();
        appointmentBlocksPage.selectLocationBlock("Outpatient Clinic");
        appointmentBlocksPage.enterService("Oncology");
        appointmentBlocksPage.clickOnSave();
        headerPage.clickOnHomeIcon();
        manageAppointmentPage.goToManageAppointment();
        findPatientPage.enterPatient("Bob Smith");
        findPatientPage.clickOnFirstPatient();
        if (manageAppointmentPage.deletePresent()){
            manageAppointmentPage.deleteAppointment();
        }
        else {
        manageAppointmentPage.enterService("Oncology");
        manageAppointmentPage.searchAppointment();
        manageAppointmentPage.saveAppointment();
        patientDashboardPage.waitForVisitLink();
        assertTrue(driver.getPageSource().contains("Scheduled an appointment for"));
        headerPage.clickOnHomeIcon();
        appointmentBlocksPage.goToAppointmentBlock();
        appointmentBlocksPage.selectLocation("Outpatient Clinic");
        appointmentBlocksPage.findBlock();
        appointmentBlocksPage.clickOnDelete();
        appointmentBlocksPage.clickOnConfirmDelete();}
        }

    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }
}

