package org.openmrs.reference;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.LoginPage;

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
    	assertPage(loginPage);
        loginPage.loginAsAdmin();
        assertPage(homePage);
        assertThat(homePage.isDispensingMedicationAppPresented(),is(true));
        assertThat(homePage.isRegisterPatientCustomizedForRefAppPresented(), is(true));

        assertThat(homePage.isFindAPatientAppPresented(), is(true));

        assertThat(homePage.isActiveVisitsAppPresented(), is(true));
        assertThat(homePage.isStyleGuideAppPresented(), is(true));

        assertThat(homePage.isSystemAdministrationAppPresented(), is(true));
        assertThat(homePage.isLegacyFindPatientAppPresented(), is(true));

        headerPage.logOut();
        assertPage(loginPage);
    }

}
