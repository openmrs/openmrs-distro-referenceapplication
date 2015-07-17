package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.RelationTypePage;
import org.openmrs.reference.page.VisitTypePage;
import org.openmrs.uitestframework.test.TestBase;
import org.openqa.selenium.Alert;

import static org.junit.Assert.assertTrue;

/**
 * Created by tomasz on 16.07.15.
 */
public class RelationTypeTest extends TestBase {

    private HeaderPage headerPage;
    private HomePage homePage;
    private RelationTypePage relationTypePage;
    private boolean acceptNextAlert = true;

    @Before
    public void setUp() {
        headerPage = new HeaderPage(driver);
        homePage = new HomePage(driver);
        relationTypePage = new RelationTypePage(driver);
        login();
    }

    @After
    public void tearDown() throws InterruptedException{
        relationTypePage.clickOnHomeLink();
        headerPage.logOut();
    }

    // Test for RA-762, RA-763, RA-764, RA-765
    @Test
    public void addEditRetireDeleteRelationTypeTest() throws InterruptedException {
        homePage.goToAdministration();
        relationTypePage.manage();
        assertPage(relationTypePage);
        relationTypePage.add();
        relationTypePage.save();
        assertTrue(driver.getPageSource().contains("A is to B name is required"));
        assertTrue(driver.getPageSource().contains("B is to A name is required"));
        assertTrue(driver.getPageSource().contains("Please fix all errors and try again."));
        relationTypePage.createRelationType("Wife", "Husband", "Marriage");
        assertTrue(driver.getPageSource().contains("Relationship type saved"));
        relationTypePage.find("Wife/Husband");
        relationTypePage.fillInDescription("This is Super Marriage");
        relationTypePage.save();
        assertTrue(driver.getPageSource().contains("Relationship type saved"));
        relationTypePage.find("Wife/Husband");
        relationTypePage.retire();
        assertTrue(driver.getPageSource().contains("Retire reason cannot be empty."));
        assertTrue(driver.getPageSource().contains("Please fix all errors and try again."));
        relationTypePage.fillInRetireReason("divorce");
        relationTypePage.retire();
        assertTrue(driver.getPageSource().contains("Relationship Type retired successfully"));
        relationTypePage.findRetired("Wife/Husband");
        relationTypePage.delete();
        assertTrue(closeAlertAndGetItsText().contains("Are you sure you want to purge this object"));
        assertTrue(driver.getPageSource().contains("Relationship Type deleted forever successfully"));
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
