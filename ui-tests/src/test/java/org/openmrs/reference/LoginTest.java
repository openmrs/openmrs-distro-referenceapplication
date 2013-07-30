package org.openmrs.reference;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.uitestframework.page.LoginPage;
import org.openmrs.uitestframework.test.TestBase;

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
        assertTrue(homePage.isDispensingMedicationAppPresent());
        assertTrue(homePage.isRegisterPatientCustomizedForRefAppPresent());
        assertTrue(homePage.isFindAPatientAppPresent());
        assertTrue(homePage.isActiveVisitsAppPresent());
        assertTrue(homePage.isStyleGuideAppPresent());
        assertTrue(homePage.isSystemAdministrationAppPresent());
        headerPage.logOut();
        assertPage(loginPage);
    }

}
