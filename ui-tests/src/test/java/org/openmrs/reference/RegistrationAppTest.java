package org.openmrs.reference;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.reference.helper.PatientGenerator;
import org.openmrs.reference.helper.TestPatient;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.RegistrationPage;
import org.openmrs.uitestframework.page.LoginPage;
import org.openmrs.uitestframework.test.TestBase;

public class RegistrationAppTest extends TestBase {
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
    	assertPage(loginPage);
        loginPage.login("clerk", "Clerk123"); // TODO: loginPage.loginAsClerk();
        assertPage(homePage);
    }

    @Ignore
    public void verifyRegistrationSuccessful()  {
        homePage.openRegisterAPatientApp();
//        registrationPage.registerPatient();
// breeze - commented this test out as it is a WIP
        //todo - Verification of the Patient Registration once  RA-72 is completed
    }

    // Test for Story RA-71
    @Test
    public void registerAPatientAndVerifyConfirmationPage() {
        homePage.openRegisterAPatientApp();
        TestPatient patient = PatientGenerator.generateTestPatient();
        registrationPage.enterPatient(patient);

        String address = patient.address1 + " " + 
        		patient.address2 + " " + 
        		patient.city + " " + 
        		patient.state + " " + 
        		patient.country + " " + 
        		patient.postalCode;

        assertEquals(patient.givenName + " " + patient.familyName, registrationPage.getNameInConfirmationPage());
        assertEquals(patient.gender, registrationPage.getGenderInConfirmationPage());
        assertEquals(patient.birthDay + " " + patient.birthMonth + " " + patient.birthYear, registrationPage.getBirthdateInConfirmationPage());
        assertEquals(address, registrationPage.getAddressInConfirmationPage());
        assertEquals(patient.phone, registrationPage.getPhoneInConfirmationPage());
    }

    @After
    public void tearDown() {
        headerPage.logOut();
    }

}
