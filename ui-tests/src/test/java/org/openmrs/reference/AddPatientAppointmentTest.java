

package org.openmrs.reference;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.reference.page.AppointmentBlocksPage;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.reference.page.FindPatientPage;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.ManageAppointmentPage;
import org.openmrs.uitestframework.test.TestBase;


/**
 * Created by nata on 21.07.2015.
 */
public class AddPatientAppointmentTest extends TestBase {
    private HeaderPage headerPage;
    private AppointmentBlocksPage appointmentBlocksPage;
    private ManageAppointmentPage manageAppointmentPage;
    private FindPatientPage findPatientPage;
    private HomePage homePage;
    private ClinicianFacingPatientDashboardPage patientDashboardPage;

    @Before
    public void setUp() throws Exception {
        homePage = new HomePage(page);
        assertPage(homePage);
        headerPage = new HeaderPage(driver);
        appointmentBlocksPage = new AppointmentBlocksPage(driver);
        manageAppointmentPage = new ManageAppointmentPage(driver);
        findPatientPage = new FindPatientPage(driver);
        patientDashboardPage = new ClinicianFacingPatientDashboardPage(page);
    }

    @Ignore //ignored due to timezone error
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

