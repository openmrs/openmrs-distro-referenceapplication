package org.openmrs.reference;

/**
 * Created by nata on 25.06.15.
 */
import org.junit.*;
import static org.junit.Assert.*;

import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.PatientDashboardPage;
import org.openmrs.uitestframework.test.TestBase;
import org.openmrs.uitestframework.test.TestData;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;


public class InpatientTest extends TestBase {
    private StringBuffer verificationErrors = new StringBuffer();
    private HomePage homePage;
    private PatientDashboardPage patientDashboardPage;
    private HeaderPage headerPage;



    @Before
    public void setUp() throws Exception {

        homePage = new HomePage(driver);
        loginPage.loginAsAdmin();
        assertPage(homePage);
        patientDashboardPage = new PatientDashboardPage(driver);
        headerPage = new HeaderPage(driver);
        homePage.goToActiveVisitPatient();


    }

    @Test
    public void InpatientTest() throws Exception {

//        Admit to Inpatient Test
        if(!patientDashboardPage.impatientPresent()) {
            patientDashboardPage.exitFromImpatient();

        }
        patientDashboardPage.clickOnAdmitToImpatient();
        new Select(driver.findElement(By.id("w5"))).selectByVisibleText("Unknown Location");
        patientDashboardPage.clickOnSave();
        try {
            assertTrue(driver.findElement(By.cssSelector("div.toast-item.toast-type-success > p")).getText().contains("Entered Admission"));
        }
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
//        Transfer to Ward/Service Test
        driver.findElement(By.id("referenceapplication.realTime.simpleTransfer")).click();
        new Select(driver.findElement(By.id("w5"))).selectByVisibleText("Isolation Ward");
        patientDashboardPage.clickOnSave();
        try {
            assertTrue(driver.findElement(By.cssSelector("div.toast-item.toast-type-success > p")).getText().contains("Entered Transfer Within Hospital"));
        }
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
//       Exit from Impatient
        patientDashboardPage.exitFromImpatient();
        try {
            assertTrue(driver.findElement(By.cssSelector("div.toast-item.toast-type-success > p")).getText().contains("Entered Discharge"));
        }
        catch (Error e) {
            verificationErrors.append(e.toString());
        }

    }

    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }

}
