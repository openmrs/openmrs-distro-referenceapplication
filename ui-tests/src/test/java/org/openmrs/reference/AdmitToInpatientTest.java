package org.openmrs.reference;

import static org.junit.Assert.assertTrue;

/**
 * Created by nata on 25.06.15.
 */
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.uitestframework.test.TestBase;


public class AdmitToInpatientTest extends TestBase {
    private HomePage homePage;
    private ClinicianFacingPatientDashboardPage patientDashboardPage;
    private HeaderPage headerPage;



    @Before
    public void setUp() throws Exception {
        homePage = new HomePage(page);
        assertPage(homePage);
        patientDashboardPage = new ClinicianFacingPatientDashboardPage(page);
        headerPage = new HeaderPage(driver);
        homePage.goToActiveVisitPatient();


    }

    @Ignore//ignored due to adding choose provider functionality
    @Test
    public void admitToInpatientTest() throws Exception {

        if(patientDashboardPage.inpatientPresent()) {
            patientDashboardPage.exitFromInpatient();

        }
        patientDashboardPage.clickOnAdmitToInpatient();
        patientDashboardPage.selectLocation("Unknown Location");
        assertTrue(patientDashboardPage.location().getText().contains("Unknown Location"));

        patientDashboardPage.clickOnSave();
        assertTrue(patientDashboardPage.visitLink().getText().contains("Entered Admission"));
        patientDashboardPage.exitFromInpatient();
        patientDashboardPage.waitForVisitLink();
        assertTrue(patientDashboardPage.visitLink().getText().contains("Entered Discharge"));

    }

    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }

}
