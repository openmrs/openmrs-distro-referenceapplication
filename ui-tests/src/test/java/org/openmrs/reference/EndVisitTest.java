package org.openmrs.reference;

/**
 * Created by nata on 17.06.15.
 */
import org.junit.*;
import static org.junit.Assert.*;

import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.uitestframework.test.TestBase;

public class EndVisitTest extends TestBase {
    private HomePage homePage;
    private ClinicianFacingPatientDashboardPage patientDashboardPage;
    private HeaderPage headerPage;

    @Before
    public void setUp() throws Exception {

        loginPage.loginAsAdmin();
        homePage = new HomePage(driver);
        assertPage(homePage);
        patientDashboardPage = new ClinicianFacingPatientDashboardPage(driver);
        headerPage = new HeaderPage(driver);
    }

    @Ignore//ignored due to possible application logout
    @Test
    public void EndVisitTest() throws Exception {

        homePage.goToActiveVisitPatient();
        patientDashboardPage.endVisit();
        assertNotNull(patientDashboardPage.END_VISIT);
        patientDashboardPage.waitForVisitLinkHidden();
    }

    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }

}