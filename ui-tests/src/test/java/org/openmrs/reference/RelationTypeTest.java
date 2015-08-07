package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.reference.page.*;
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
    private SettingPage settingPage;
    private boolean acceptNextAlert = true;

    @Before
    public void setUp() {
        headerPage = new HeaderPage(driver);
        login();
        homePage = new HomePage(driver);
        relationTypePage = new RelationTypePage(driver);
        settingPage = new SettingPage(driver);
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
        if(relationTypePage.ifExists("Wife/Husband")) {
            relationTypePage.delete();
            closeAlertAndGetItsText();
        }
        relationTypePage.add();
        relationTypePage.save();
        relationTypePage.waitForError();
        assertTrue(driver.getPageSource().contains("A is to B name is required"));
        assertTrue(driver.getPageSource().contains("B is to A name is required"));
        assertTrue(driver.getPageSource().contains("Please fix all errors and try again."));
        relationTypePage.createRelationType("Wife", "Husband", "Marriage");
        settingPage.waitForMessage();
        assertTrue(driver.getPageSource().contains("Relationship type saved"));
        relationTypePage.find("Wife/Husband");
        relationTypePage.fillInDescription("This is Super Marriage");
        relationTypePage.save();
        settingPage.waitForMessage();
        assertTrue(driver.getPageSource().contains("Relationship type saved"));
        relationTypePage.find("Wife/Husband");
        relationTypePage.retire();
        Thread.sleep(200);
        assertTrue(driver.getPageSource().contains("Retire reason cannot be empty."));
        assertTrue(driver.getPageSource().contains("Please fix all errors and try again."));
        relationTypePage.fillInRetireReason("divorce");
        relationTypePage.retire();
        settingPage.waitForMessage();
        assertTrue(driver.getPageSource().contains("Relationship Type retired successfully"));
        relationTypePage.findRetired("Wife/Husband");
        relationTypePage.delete();
        Thread.sleep(200);
        assertTrue(closeAlertAndGetItsText().contains("Are you sure you want to purge this object"));
        settingPage.waitForMessage();
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
