package org.openmrs.reference;

/**
 * Created by nata on 22.06.15.
 */
import org.junit.*;
import static org.junit.Assert.*;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.PatientDashboardPage;
import org.openmrs.uitestframework.test.TestBase;


public class AddDiagnosisToVisitNoteTest extends TestBase {
    private HomePage homePage;
    private PatientDashboardPage patientDashboardPage;
    private HeaderPage headerPage;


    @Before
    public void setUp() throws Exception {
        loginPage.loginAsAdmin();
        homePage = new HomePage(driver);
        assertPage(homePage);
        patientDashboardPage = new PatientDashboardPage(driver);
        headerPage = new HeaderPage(driver);
    }

    @Test
    public void AddDiagnosisToVisitNoteTest() throws Exception {
        homePage.goToActiveVisitPatient();
        patientDashboardPage.visitNote();
        patientDashboardPage.enterDiagnosis("Pne");
        patientDashboardPage.enterSecondaryDiagnosis("Bleed");
        assertEquals("Pneumonia", patientDashboardPage.primaryDiagnosis());
        assertEquals("Bleeding", patientDashboardPage.secondaryDiagnosis());
        patientDashboardPage.save();
        assertNotNull(patientDashboardPage.visitLink());

    }

    @After
    public void tearDown ()throws Exception {
        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }
}
