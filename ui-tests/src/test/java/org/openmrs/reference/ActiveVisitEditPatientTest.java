package org.openmrs.reference;

/**
 * Created by nata on 17.06.15.
 */


import org.junit.*;
import static org.junit.Assert.*;

import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.PatientDashboardPage;
import org.openmrs.reference.page.RegistrationPage;
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
    private RegistrationPage registrationPage;

    @Before
    public void setUp() throws Exception {

        homePage = new HomePage(driver);
        loginPage.loginAsAdmin();
        assertPage(homePage);
        patient = createTestPatient();
        patientDashboardPage = new PatientDashboardPage(driver);
        registrationPage = new RegistrationPage(driver);
        headerPage = new HeaderPage(driver);




    }

    @Test
    public void testActiveVisitEditPatient() throws Exception {
        currentPage().gotoPage(PatientDashboardPage.URL_PATH + "?patientId=" + patient.uuid);
        patientDashboardPage.startVisit();
        patientDashboardPage.visitLink();
        driver.findElement(By.linkText("Edit")).click();
        driver.findElement(By.name("givenName")).clear();
        driver.findElement(By.name("givenName")).sendKeys("John");
        driver.findElement(By.name("middleName")).clear();
        driver.findElement(By.name("middleName")).sendKeys("Bob");
        driver.findElement(By.name("familyName")).clear();
        driver.findElement(By.name("familyName")).sendKeys("Smith");
        driver.findElement(By.id("genderLabel")).click();
        patient.gender = "Male";
        driver.findElement(By.id("birthdateLabel")).click();
        driver.findElement(By.id("birthdateDay-field")).clear();
        driver.findElement(By.id("birthdateDay-field")).sendKeys("02");
        new Select(driver.findElement(By.id("birthdateMonth-field"))).selectByVisibleText("April");
        driver.findElement(By.id("birthdateYear-field")).clear();
        driver.findElement(By.id("birthdateYear-field")).sendKeys("1985");
        driver.findElement(By.xpath("//ul[@id='formBreadcrumb']/li[2]/span")).click();
        driver.findElement(By.xpath("//input[@value='Confirm']")).click();
        try {
            assertTrue(driver.findElement(By.cssSelector("h1.name > span")).getText().contains("John"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertTrue(driver.findElement(By.xpath("//div[@id='content']/div[5]/div/h1/span[2]")).getText().contains("Smith"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertTrue(driver.findElement(By.cssSelector("span.gender-age > span")).getText().contains("Male"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }

    }

    @After
    public void tearDown() throws Exception {

        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }


}
