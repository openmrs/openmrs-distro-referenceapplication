package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.reference.page.ActiveVisitsPage;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.uitestframework.test.TestBase;

/**
 * Created by nata on 23.07.15.
 */
public class ComeBackToApplicationAfterLogoutTest extends TestBase {

    private HomePage homePage;
    private HeaderPage headerPage;


    @Before
    public void setUp() throws Exception {
        homePage = new HomePage(driver);
        loginPage.loginAsAdmin();
        assertPage(homePage);
        headerPage = new HeaderPage(driver);
    }
    @Ignore
    @Test
    public void comeBackToApplicationAfterLogoutTest() throws Exception {
        homePage.goToActiveVisitsSearch();
        headerPage.logOut();
        loginPage.loginAsAdmin();
        assertPage(homePage);
    }

    @After
    public void tearDown ()throws Exception {
        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }
}
