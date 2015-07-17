package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.ManagePersonPage;
import org.openmrs.reference.page.RelationTypePage;
import org.openmrs.uitestframework.test.TestBase;
import org.openqa.selenium.Alert;

import static org.junit.Assert.assertTrue;

/**
 * Created by tomasz on 16.07.15.
 */
public class ManagePersonTest extends TestBase {

    private HeaderPage headerPage;
    private HomePage homePage;
    private ManagePersonPage managePersonPage;
    private boolean acceptNextAlert = true;

    @Before
    public void setUp() {
        headerPage = new HeaderPage(driver);
        homePage = new HomePage(driver);
        managePersonPage = new ManagePersonPage(driver);
        login();
    }

    @After
    public void tearDown() throws InterruptedException{
        managePersonPage.clickOnHomeLink();
        headerPage.logOut();
    }

    @Test
    public void createEditDeletePersonTest() throws InterruptedException {
        homePage.goToAdministration();
        managePersonPage.manage();
        assertPage(managePersonPage);
        managePersonPage.add();
        managePersonPage.create();
        assertTrue(driver.getPageSource().contains("Please select a name"));
        assertTrue(driver.getPageSource().contains("Please select a valid birthdate or age"));
        assertTrue(driver.getPageSource().contains("Please select a gender"));
        managePersonPage.createPerson("Arystoteles", "80", 'M');
        managePersonPage.fillInFamilyName("Greek");
        managePersonPage.save();
        assertTrue(driver.getPageSource().contains("Person saved"));
        managePersonPage.manage();
        managePersonPage.findBySearch("Arystoteles");
        managePersonPage.fillInGivenName("Pitagoras");
        managePersonPage.save();
        assertTrue(driver.getPageSource().contains("Person saved"));
        managePersonPage.manage();
        managePersonPage.findBySearch("Pitagoras");
        managePersonPage.retire();
        assertTrue(driver.getPageSource().contains("Person deleted"));
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
