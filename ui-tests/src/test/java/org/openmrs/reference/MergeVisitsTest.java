package org.openmrs.reference;

import org.junit.*;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.uitestframework.test.TestBase;
import org.openmrs.uitestframework.test.TestData;

import static junit.framework.Assert.assertTrue;

/**
 * Created by tomasz on 23.07.15.
 */
public class MergeVisitsTest extends TestBase {

    private HomePage homePage;
    private HeaderPage headerPage;
    private ClinicianFacingPatientDashboardPage patientDashboardPage;
    private TestData.PatientInfo patient;

    @Before
    public void setUp() {
        patient = createTestPatient();
        loginPage.loginAsAdmin();
        homePage = new HomePage(driver);
        assertPage(homePage);
        patientDashboardPage = new ClinicianFacingPatientDashboardPage(driver);
        headerPage = new HeaderPage(driver);
    }

    @After
    public void tearDown() throws InterruptedException {
        headerPage.clickOnHomeIcon();
        deletePatient(patient.uuid);
        headerPage.logOut();
    }

    @Ignore //ignored due to problems in enter date algorithm
    @Test
    public void mergeVisitsTest() {
        currentPage().goToPage(ClinicianFacingPatientDashboardPage.URL_PATH + "?patientId=" + patient.uuid);
        assertPage(patientDashboardPage);
        patientDashboardPage.startVisit();
        Assert.assertTrue(patientDashboardPage.hasActiveVisit());
        patientDashboardPage.back();
        patientDashboardPage.addPastVisit();
        if(patientDashboardPage.errorPresent()) {
            patientDashboardPage.clickChangeDate();
            patientDashboardPage.enterDate();
        }
        patientDashboardPage.back();
        assertTrue(patientDashboardPage.mergeVisits().contains("Visits merged successfully"));

    }
}
