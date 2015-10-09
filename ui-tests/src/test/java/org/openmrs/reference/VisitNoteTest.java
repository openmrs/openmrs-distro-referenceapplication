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
@Ignore
public class VisitNoteTest extends TestBase {

    private HomePage homePage;
    private HeaderPage headerPage;
    private PatientDashboardPage patientDashboardPage;
    private PatientInfo patient;

    @Before
    public void before() {
        headerPage = new HeaderPage(driver);
        patient = createTestPatient();
        loginPage.loginAsAdmin();
        homePage = new HomePage(driver);
        patientDashboardPage = new PatientDashboardPage(driver);
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
    public void testAddEditVisitNote() throws InterruptedException {
        currentPage().goToPage(PatientDashboardPage.URL_PATH + "?patientId=" + patient.uuid);
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
        patientDashboardPage.back();
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
