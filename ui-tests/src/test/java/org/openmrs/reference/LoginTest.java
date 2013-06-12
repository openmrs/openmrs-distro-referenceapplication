package org.openmrs.reference;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.LoginPage;

public class LoginTest extends TestBase {
    private HeaderPage headerPage;
    private LoginPage loginPage;

    @Before
    public void setUp() {
        loginPage = new LoginPage(driver);
        headerPage = new HeaderPage(driver);
    }

    @Test
    public void loginAndLogout() throws Exception {
        loginPage.loginAsAdmin();
        //TODO: verify the apps on the screen
        headerPage.logOut();
    }
}
