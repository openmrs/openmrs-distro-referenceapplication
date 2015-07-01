package org.openmrs.reference;

/**
 * Created by nata on 25.06.15.
 */
import org.junit.*;
import static org.junit.Assert.*;

import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.PatientDashboardPage;
import org.openmrs.uitestframework.test.TestBase;

public class ExitFromInpatientTest extends TestBase {
    private HomePage homePage;
    private PatientDashboardPage patientDashboardPage;
    private HeaderPage headerPage;



    @Before
    public void setUp() throws Exception {

        homePage = new HomePage(driver);
        loginPage.loginAsAdmin();
        assertPage(homePage);
        patientDashboardPage = new PatientDashboardPage(driver);
        headerPage = new HeaderPage(driver);
        homePage.goToActiveVisitPatient();


    }

    @Test
    public void exitFromInpatientTest() throws Exception {

        if(!patientDashboardPage.inpatientPresent()) {
            patientDashboardPage.exitFromInpatient();

        }
        patientDashboardPage.clickOnAdmitToInpatient();
        patientDashboardPage.selectLocation("Unknown Location");
        assertTrue(patientDashboardPage.location().getText().contains("Unknown Location"));
        patientDashboardPage.clickOnSave();
        patientDashboardPage.exitFromInpatient();
        assertTrue(patientDashboardPage.visitLink().getText().contains("Entered Discharge"));
    }

    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }

}
