package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.PatientDashboardPage;
import org.openmrs.uitestframework.test.TestBase;
import org.openmrs.uitestframework.test.TestData.PatientInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by tomasz on 19.06.15.
 */
public class VisitNoteTest extends TestBase {

    private HomePage homePage;
    private HeaderPage headerPage;
    private PatientDashboardPage patientDashboardPage;
    private PatientInfo patient;

    @Before
    public void before() {
        homePage = new HomePage(driver);
        headerPage = new HeaderPage(driver);
        patient = createTestPatient();
        patientDashboardPage = new PatientDashboardPage(driver);
        loginPage.loginAsAdmin();
        assertPage(homePage);
    }

    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
//        deletePatient(patient.uuid);
        headerPage.logOut();
    }

    //Test for RA-720, RA-682, RA-694
    @Test
    public void testAddEditVisitNote() {
        currentPage().gotoPage(PatientDashboardPage.URL_PATH + "?patientId=" + patient.uuid);
        assertPage(patientDashboardPage);
        if(!patientDashboardPage.hasActiveVisit()) {
            patientDashboardPage.startVisit();
            patientDashboardPage.waitForVisitLinkHidden();
        }
        patientDashboardPage.visitNote();
        assertNotNull(patientDashboardPage.findPageElement());
        patientDashboardPage.selectProviderAndLocation();
        patientDashboardPage.addDiagnosis("MALARIA");
        patientDashboardPage.enterSecondaryDiagnosis("CANCER");
        patientDashboardPage.addNote("This is a note");
        patientDashboardPage.save();
        assertTrue(driver.getPageSource().contains("MALARIA") && driver.getPageSource().contains("CANCER"));
        patientDashboardPage.goToEditVisitNote();
        patientDashboardPage.deleteDiagnosis();
        patientDashboardPage.addSecondaryDiagnosis("flue");
        assertEquals("flue", patientDashboardPage.secondaryDiagnosis());
        patientDashboardPage.save();
        assertNotNull(patientDashboardPage.visitLink());

        patientDashboardPage.deleteVisitNote();
        assertTrue(driver.getPageSource().contains("Are you sure you want to delete this encounter from the visit?"));
        patientDashboardPage.confirmDeletion();
        assertNotNull(patientDashboardPage.visitLink());

    }
}
