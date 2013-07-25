package org.openmrs.reference;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
    public void verifyModulesAvailableOnHomePage() throws Exception {
    	assertPage(loginPage);
        loginPage.loginAsAdmin();
        assertPage(homePage);
        assertThat(homePage.isDispensingMedicationAppPresent(),is(true));
        assertThat(homePage.isRegisterPatientCustomizedForRefAppPresent(), is(true));
        assertThat(homePage.isFindAPatientAppPresent(), is(true));
        assertThat(homePage.isActiveVisitsAppPresent(), is(true));
        assertThat(homePage.isStyleGuideAppPresent(), is(true));
        assertThat(homePage.isSystemAdministrationAppPresent(), is(true));
        headerPage.logOut();
        assertPage(loginPage);
    }

}
