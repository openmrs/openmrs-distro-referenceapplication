package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
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
        deletePatient(patient.uuid);
        headerPage.logOut();
    }

    //Test for RA-720
    @Test
    public void testAddVisitNote() {
        driver.findElement(By.cssSelector("i.icon-calendar")).click();
        currentPage().gotoPage(PatientDashboardPage.URL_PATH + "?patientId=" + patient.uuid);
        assertPage(patientDashboardPage);
        if(!patientDashboardPage.hasActiveVisit()) {
            patientDashboardPage.startVisit();
            patientDashboardPage.waitForVisitLinkHidden();
        }
        patientDashboardPage.visitNote();
        assertNotNull(driver.findElement(By.id("who-when-where")));
        new Select(driver.findElement(By.id("w1"))).selectByVisibleText("Super User");
        new Select(driver.findElement(By.id("w3"))).selectByVisibleText("Isolation Ward");
        driver.findElement(By.id("w5-display")).click();
        driver.findElement(By.linkText("" +Calendar.getInstance().get(Calendar.DAY_OF_MONTH))).click();
        WebElement diagnosisElement = driver.findElement(By.id("diagnosis-search"));
        diagnosisElement.click();
        patientDashboardPage.enterDiagnosis("MALARIA");
        diagnosisElement.clear();
        diagnosisElement.click();
        patientDashboardPage.enterSecondaryDiagnosis("CANCER");
        driver.findElement(By.id("w10")).clear();
        driver.findElement(By.id("w10")).sendKeys("This is a note");
        driver.findElement(By.xpath("//input[@value='Save']")).click();
        assertTrue(driver.getPageSource().contains("MALARIA") && driver.getPageSource().contains("CANCER"));
    }
}
