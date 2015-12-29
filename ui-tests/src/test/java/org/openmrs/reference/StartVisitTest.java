package org.openmrs.reference;

/**
 * Created by nata on 17.06.15.
 */
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.uitestframework.test.TestBase;
import org.openmrs.uitestframework.test.TestData;

public class StartVisitTest extends TestBase {
    private HomePage homePage;
    private ClinicianFacingPatientDashboardPage patientDashboardPage;
    private HeaderPage headerPage;
    private TestData.PatientInfo patient;

    @Before
    public void setUp() throws Exception {

        patient = createTestPatient();

        homePage = new HomePage(page);
        assertPage(homePage);
        patientDashboardPage = new ClinicianFacingPatientDashboardPage(page);
        headerPage = new HeaderPage(driver);
    }

    @Ignore //ignored due to logout error (RA-894)
    @Test
    public void startVisitTest() throws Exception {

    	patientDashboardPage.go(patient.uuid);
        assertPage(patientDashboardPage);
        patientDashboardPage.startVisit();
        Assert.assertTrue(patientDashboardPage.hasActiveVisit());
    }

    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        deletePatient(patient.uuid);
        headerPage.logOut();
    }

}