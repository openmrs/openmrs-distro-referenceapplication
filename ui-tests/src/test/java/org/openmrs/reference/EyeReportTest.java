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
import org.openmrs.reference.page.ManageFormsPage;
import org.openmrs.uitestframework.test.TestBase;

public class EyeReportTest extends TestBase {
    private HomePage homePage;
    private ClinicianFacingPatientDashboardPage patientDashboardPage;
    private HeaderPage headerPage;
    private ManageFormsPage manageFormsPage;


    @Before
    public void setUp() throws Exception {
        homePage = new HomePage(page);
        assertPage(homePage);
        patientDashboardPage = new ClinicianFacingPatientDashboardPage(page);
        headerPage = new HeaderPage(driver);
        manageFormsPage = new ManageFormsPage(driver);
        homePage.goToActiveVisitPatient();
    }

    @Ignore// ignored due to eye form changes
    @Test
    public void eyeReportTest() throws Exception {
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

