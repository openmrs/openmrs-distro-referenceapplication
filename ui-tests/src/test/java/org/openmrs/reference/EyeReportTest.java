package org.openmrs.reference;

/**
 * Created by nata on 25.06.15.
 */
import org.junit.*;
import static org.junit.Assert.*;

import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.ManageFormsPage;
import org.openmrs.reference.page.PatientDashboardPage;
import org.openmrs.uitestframework.test.TestBase;

public class EyeReportTest extends TestBase {
    private HomePage homePage;
    private PatientDashboardPage patientDashboardPage;
    private HeaderPage headerPage;
    private ManageFormsPage manageFormsPage;



    @Before
    public void setUp() throws Exception {

        homePage = new HomePage(driver);
        loginPage.loginAsAdmin();
        assertPage(homePage);
        patientDashboardPage = new PatientDashboardPage(driver);
        headerPage = new HeaderPage(driver);
        manageFormsPage = new ManageFormsPage(driver);
        homePage.goToActiveVisitPatient();


    }

    @Ignore
    @Test
    public void eyeReportTest() throws Exception {
        patientDashboardPage.clickOnEyeForm();
        manageFormsPage.clickOnCalendarEyeReport();
        manageFormsPage.selectYear("1999");
        manageFormsPage.selectMoth("12");
        manageFormsPage.selectDay("3");
        manageFormsPage.selectWho("Super, User");
        manageFormsPage.selectWhere("Inpatient Ward");
        manageFormsPage.enterForm();
        assertTrue(driver.getPageSource().contains("Entered File upload test for"));
        manageFormsPage.clickOnCurrentDate();
        manageFormsPage.enterForm();
        assertTrue(patientDashboardPage.visitLink().getText().contains("Entered File upload test for"));

    }

    @After
    public void tearDown() throws Exception {
        patientDashboardPage.waitForVisitLinkHidden();
        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }

}

