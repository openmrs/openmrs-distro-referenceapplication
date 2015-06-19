package org.openmrs.reference;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.uitestframework.test.TestBase;
import org.openqa.selenium.By;


/**
 * Created by nata on 16.06.15.
 */


        import org.junit.*;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;



    public class RecentlyPatientTest extends TestBase {
    private HomePage homePage;
    private HeaderPage headerPage;


    @Before
    public void setUp() throws Exception {
        homePage = new HomePage(driver);
        loginPage.loginAsAdmin();
        assertPage(homePage);
        headerPage = new HeaderPage(driver);

    }

    @Test
    public void testRecentlyPatientTest() throws Exception {
        driver.findElement(By.id("coreapps-activeVisitsHomepageLink-coreapps-activeVisitsHomepageLink-extension")).click();
        driver.findElement(By.id("patient-search")).sendKeys("Bob Smith");
        headerPage.clickOnHomeIcon();
        driver.findElement(By.id("coreapps-activeVisitsHomepageLink-coreapps-activeVisitsHomepageLink-extension")).click();
        assertTrue(isElementPresent(By.id("patient-search-results-table")));
    }



    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }

        private boolean isElementPresent(By by) {
            try {
                driver.findElement(by);
                return true;
            } catch (NoSuchElementException e) {
                return false;
            }
    }


}
