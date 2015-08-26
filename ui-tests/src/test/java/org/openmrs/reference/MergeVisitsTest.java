package org.openmrs.reference;

import org.junit.*;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.PatientDashboardPage;
import org.openmrs.uitestframework.test.TestBase;
import org.openmrs.uitestframework.test.TestData;

import static junit.framework.Assert.assertTrue;

/**
 * Created by tomasz on 23.07.15.
 */
public class MergeVisitsTest extends TestBase {

    private HomePage homePage;
    private HeaderPage headerPage;
    private PatientDashboardPage patientDashboardPage;
    private TestData.PatientInfo patient;

    @Before
    public void setUp() {
        patient = createTestPatient();
        loginPage.loginAsAdmin();
        homePage = new HomePage(driver);
        assertPage(homePage);
        patientDashboardPage = new PatientDashboardPage(driver);
        headerPage = new HeaderPage(driver);
    }

    @After
    public void tearDown() throws InterruptedException {
        headerPage.clickOnHomeIcon();
        deletePatient(patient.uuid);
        headerPage.logOut();
    }

    @Test
    @Ignore
    public void mergeVisitsTest() {
        currentPage().gotoPage(PatientDashboardPage.URL_PATH + "?patientId=" + patient.uuid);
        assertPage(patientDashboardPage);
        patientDashboardPage.startVisit();
        Assert.assertTrue(patientDashboardPage.hasActiveVisit());
        patientDashboardPage.addPastVisit();
        if(patientDashboardPage.errorPresent()) {
            patientDashboardPage.clickChangeDate();
            patientDashboardPage.enterDate();
        }
        assertTrue(patientDashboardPage.mergeVisits().contains("Visits merged successfully"));

    }
}
