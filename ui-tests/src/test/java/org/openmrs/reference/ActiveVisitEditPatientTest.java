package org.openmrs.reference;

/**
 * Created by nata on 17.06.15.
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


public class ActiveVisitEditPatientTest extends TestBase {
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    private HomePage homePage;
    private PatientDashboardPage patientDashboardPage;
    private TestData.PatientInfo patient;
    private HeaderPage headerPage;

    @Before
    public void setUp() throws Exception {

        homePage = new HomePage(driver);
        loginPage.loginAsAdmin();
        assertPage(homePage);
        patient = createTestPatient();
        patientDashboardPage = new PatientDashboardPage(driver);



    }

    @Test
    public void testActiveVisitEditPatient() throws Exception {
        currentPage().gotoPage(PatientDashboardPage.URL_PATH + "?patientId=" + patient.uuid);
        patientDashboardPage.startVisit();
        Assert.assertEquals("Today", patientDashboardPage.visitLink().getText().trim());
        driver.findElement(By.linkText("Edit")).click();
        driver.findElement(By.name("givenName")).clear();
        driver.findElement(By.name("givenName")).sendKeys("John");
        driver.findElement(By.name("middleName")).clear();
        driver.findElement(By.name("middleName")).sendKeys("");
        driver.findElement(By.name("familyName")).clear();
        driver.findElement(By.name("familyName")).sendKeys("Bob");
        driver.findElement(By.id("genderLabel")).click();
        driver.findElement(By.id("birthdateLabel")).click();
        driver.findElement(By.id("birthdateDay-field")).clear();
        driver.findElement(By.id("birthdateDay-field")).sendKeys("02");
        new Select(driver.findElement(By.id("birthdateMonth-field"))).selectByVisibleText("April");
        driver.findElement(By.id("birthdateYear-field")).clear();
        driver.findElement(By.id("birthdateYear-field")).sendKeys("1985");
        driver.findElement(By.xpath("//ul[@id='formBreadcrumb']/li[2]/span")).click();
        driver.findElement(By.xpath("//input[@value='Confirm']")).click();
        driver.findElement(By.cssSelector("i.icon-home.small")).click();
        driver.findElement(By.linkText("Logout")).click();
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}
