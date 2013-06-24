package org.openmrs.reference;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.LoginPage;
import org.openmrs.reference.page.RegistrationPage;


public class RegistrationAppTest extends TestBase{
    private HeaderPage headerPage;
    private LoginPage loginPage;
    private RegistrationPage registrationPage;
    private HomePage homePage;

    @Before
    public void setUp() {
        loginPage = new LoginPage(driver);
        headerPage = new HeaderPage(driver);
        homePage = new HomePage(driver);
        registrationPage = new RegistrationPage(driver);
    }

    @Test
    public void verifyAppsOnMainPage()  {
        loginPage.loginAsAdmin();
        homePage.OpenRegisterAPatientApp();
        registrationPage.registerPatient();

        //todo - Verification of the Patient Registration once  RA-72 is completed
    }

}
