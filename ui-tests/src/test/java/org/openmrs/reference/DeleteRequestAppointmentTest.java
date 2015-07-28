

package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.reference.helper.TestPatient;
import org.openmrs.reference.page.*;
import org.openmrs.uitestframework.test.TestBase;

import javax.ws.rs.DELETE;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Created by nata on 22.07.2015.
 */
public class DeleteRequestAppointmentTest extends TestBase {
    private HeaderPage headerPage;
    private AppointmentBlocksPage appointmentBlocksPage;
    private ManageAppointmentPage manageAppointmentPage;
    private HomePage homePage;
    private PatientDashboardPage patientDashboardPage;
    private FindPatientPage findPatientPage;
    private String id;

    @Before
    public void setUp() throws Exception {
        homePage = new HomePage(driver);
        loginPage.loginAsAdmin();
        assertPage(homePage);
        headerPage = new HeaderPage(driver);
        appointmentBlocksPage = new AppointmentBlocksPage(driver);
        manageAppointmentPage = new ManageAppointmentPage(driver);
        patientDashboardPage = new PatientDashboardPage(driver);
        findPatientPage = new FindPatientPage(driver);
    }

    @Ignore
    @Test
    public void deleteRequestAppointmentTest() throws Exception {
        homePage.goToActiveVisitPatient();
        patientDashboardPage.clickOnRequest();
        patientDashboardPage.enterAppointmentType("Oncology");
        patientDashboardPage.enterValue("0");
        patientDashboardPage.selectUnits("Day(s)");
        patientDashboardPage.saveRequest();
        id = patientDashboardPage.findPatientId();
        manageAppointmentPage.name();
        headerPage.clickOnHomeIcon();
        appointmentBlocksPage.goToAppointmentBlock();
        appointmentBlocksPage.selectLocation("Registration Desk");
        appointmentBlocksPage.clickOnCurrentDay();
        appointmentBlocksPage.selectLocationBlock("Registration Desk");
        appointmentBlocksPage.enterService("Oncology");
        appointmentBlocksPage.clickOnSave();
        headerPage.clickOnHomeIcon();
        manageAppointmentPage.goToManageAppointment();
        findPatientPage.enterPatient(id);
        findPatientPage.clickOnFirstPatient();
        manageAppointmentPage.deleteRequest();
        if (!manageAppointmentPage.deleteRequestPresent()) {
            manageAppointmentPage.deleteRequest();
        } else {
            assertNotNull(manageAppointmentPage.DELETE_REQUEST);
            headerPage.clickOnHomeIcon();
            appointmentBlocksPage.goToAppointmentBlock();
            appointmentBlocksPage.selectLocation("Registration Desk");
            appointmentBlocksPage.findBlock();
            appointmentBlocksPage.clickOnDelete();
            appointmentBlocksPage.clickOnConfirmDelete();
            assertNotNull(appointmentBlocksPage.DELETE);
        }

    }

    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }
}

