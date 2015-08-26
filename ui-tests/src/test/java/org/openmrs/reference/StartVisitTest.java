package org.openmrs.reference;

/**
 * Created by nata on 17.06.15.
 */
import org.junit.*;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.PatientDashboardPage;
import org.openmrs.uitestframework.test.TestBase;
import org.openmrs.uitestframework.test.TestData;

public class StartVisitTest extends TestBase {
    private HomePage homePage;
    private PatientDashboardPage patientDashboardPage;
    private HeaderPage headerPage;
    private TestData.PatientInfo patient;

    @Before
    public void setUp() throws Exception {

        patient = createTestPatient();
        loginPage.loginAsAdmin();
        homePage = new HomePage(driver);
        assertPage(homePage);
        patientDashboardPage = new PatientDashboardPage(driver);
        headerPage = new HeaderPage(driver);
    }

    @Test
    @Ignore
    public void startVisitTest() throws Exception {

        currentPage().gotoPage(PatientDashboardPage.URL_PATH + "?patientId=" + patient.uuid);
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