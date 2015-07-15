package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.VisitTypePage;
import org.openmrs.uitestframework.test.TestBase;

import static org.junit.Assert.assertTrue;

/**
 * Created by tomasz on 15.07.15.
 */
public class VisitTypeAdministrationTest extends TestBase {

    private HeaderPage headerPage;
    private HomePage homePage;
    private VisitTypePage visitTypePage;

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

    @Test
    public void addVisitTypeTest() throws InterruptedException {
        homePage.goToAdministration();
        visitTypePage.manageVisitTypes();
        assertPage(visitTypePage);
        visitTypePage.addVisitType();
        visitTypePage.saveVisitType();
        assertTrue(driver.getPageSource().contains("Invalid name"));
        assertTrue(driver.getPageSource().contains("Please fix all errors and try again."));
        visitTypePage.fillInVisitType("Private Visit", "When someone haven't insurance they must pay for visit");
        assertTrue(driver.getPageSource().contains("Visit Type saved"));
        visitTypePage.retireVisitType("Private Visit","Test Ended");
    }
}
