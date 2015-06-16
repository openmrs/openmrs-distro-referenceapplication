package org.openmrs.reference;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.HomePage;
import org.openmrs.uitestframework.test.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

/**
 * Created by nata on 16.06.15.
 */


        import org.junit.*;
        import static org.junit.Assert.*;
        import org.openqa.selenium.*;


public class RecentlyPatient extends TestBase {
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    private HomePage homePage;

    @Before
    public void setUp() throws Exception {
        homePage = new HomePage(driver);
        loginPage.loginAsAdmin();
        assertPage(homePage);

    }

    @Test
    public void testRecentlyPatient() throws Exception {
        driver.findElement(By.id("coreapps-activeVisitsHomepageLink-coreapps-activeVisitsHomepageLink-extension")).click();
//        driver.findElement(By.id("patient-search")).clear();
        driver.findElement(By.id("patient-search")).sendKeys("Bob Smith");
        driver.findElement(By.cssSelector("i.icon-home.small")).click();
        driver.findElement(By.id("coreapps-activeVisitsHomepageLink-coreapps-activeVisitsHomepageLink-extension")).click();
        assertNotNull(driver.findElement(By.id("patient-search-results-table")));
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
