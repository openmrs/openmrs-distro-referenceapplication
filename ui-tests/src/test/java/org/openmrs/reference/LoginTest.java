package org.openmrs.reference;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.LoginPage;
import org.openqa.selenium.By;

public class LoginTest extends TestBase {
    private HeaderPage headerPage;
    private LoginPage loginPage;
    private HomePage homePage;

    @Before
    public void setUp() {
        loginPage = new LoginPage(driver);
        headerPage = new HeaderPage(driver);
        homePage = new HomePage(driver);
    }

    @Test
    public void loginAndLogout() throws Exception {
    	assertPage(loginPage);
        loginPage.loginAsAdmin();
        assertPage(homePage);
        assertNotNull(homePage.getText(By.id("apps")));
        assertNotNull(homePage.getElementById("registrationapp-basicRegisterPatient-homepageLink-registrationapp-basicRegisterPatient-homepageLink-extension"));	// nasty
        //TODO: verify the apps on the screen
        headerPage.logOut();
        assertPage(loginPage);
    }

}
