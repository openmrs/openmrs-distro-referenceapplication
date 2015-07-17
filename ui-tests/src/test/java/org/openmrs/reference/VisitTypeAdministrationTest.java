package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.VisitTypePage;
import org.openmrs.uitestframework.test.TestBase;
import org.openqa.selenium.Alert;

import static org.junit.Assert.assertTrue;

/**
 * Created by tomasz on 15.07.15.
 */
public class VisitTypeAdministrationTest extends TestBase {

    private HeaderPage headerPage;
    private HomePage homePage;
    private VisitTypePage visitTypePage;
    private boolean acceptNextAlert = true;

    @Before
    public void setUp() {
        headerPage = new HeaderPage(driver);
        homePage = new HomePage(driver);
        visitTypePage = new VisitTypePage(driver);
        login();
    }

    @After
    public void tearDown() throws InterruptedException{
        visitTypePage.clickOnHomeLink();
        headerPage.logOut();
    }

    // Test for RA-766, RA-767, RA-768, RA-769
    @Test
    public void addEditRetireDeleteVisitTypeTest() throws InterruptedException {
        homePage.goToAdministration();
        visitTypePage.manage();
        assertPage(visitTypePage);
        visitTypePage.add();
        visitTypePage.save();
        assertTrue(driver.getPageSource().contains("Invalid name"));
        assertTrue(driver.getPageSource().contains("Please fix all errors and try again."));
        visitTypePage.createVisitType("Private Visit", "When someone haven't insurance they must pay for visit");
        assertTrue(driver.getPageSource().contains("Visit Type saved"));
        visitTypePage.find("Private Visit");
        visitTypePage.fillInName("Payment Visit");
        visitTypePage.save();
        assertTrue(driver.getPageSource().contains("Visit Type saved"));
        visitTypePage.find("Payment Visit");
        visitTypePage.retire();
        assertTrue(driver.getPageSource().contains("Retire reason cannot be empty."));
        assertTrue(driver.getPageSource().contains("Please fix all errors and try again."));
        visitTypePage.fillInRetireReason("Test Ended");
        visitTypePage.retire();
        assertTrue(driver.getPageSource().contains("Visit Type retired successfully"));
        visitTypePage.findRetired("Payment Visit");
        visitTypePage.delete();
        assertTrue(closeAlertAndGetItsText().contains("Are you sure you want to delete this visit type"));
        Thread.sleep(200);
        assertTrue(driver.getPageSource().contains("Visit Type deleted forever successfully"));
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
