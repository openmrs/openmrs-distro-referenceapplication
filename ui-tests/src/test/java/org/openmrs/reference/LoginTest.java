package org.openmrs.reference;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.LoginPage;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
    public void verifyAppsOnMainPage() throws Exception {
        loginPage.loginAsAdmin();
        assertThat(homePage.isRegisterPatientCustomizedForRefAppPresented(), is(true));
        assertThat(homePage.isPatientRegistrationAppPresented(), is(true));

        assertThat(homePage.isFindAPatientAppPresented(), is(true));

        assertThat(homePage.isActiveVisitsAppPresented(), is(true));
        assertThat(homePage.isStyleGuideAppPresented(), is(true));

        assertThat(homePage.isSystemAdministrationAppPresented(), is(true));
        assertThat(homePage.isLegacyFindPatientAppPresented(), is(true));

        headerPage.logOut();
    }

}
