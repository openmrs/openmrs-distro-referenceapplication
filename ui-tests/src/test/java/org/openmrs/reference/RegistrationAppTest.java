package org.openmrs.reference;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.reference.helper.TestPatient;
import org.openmrs.reference.page.AdminPage;
import org.openmrs.reference.page.AdvancedSettingsPage;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.LoginPage;
import org.openmrs.reference.page.RegistrationPage;


public class RegistrationAppTest extends TestBase{
    private HeaderPage headerPage;
    private LoginPage loginPage;
    private RegistrationPage registrationPage;
    private HomePage homePage;
    private AdminPage adminPage;
    private AdvancedSettingsPage advancedSettingsPage;

    @Before
    public void setUp() {
        loginPage = new LoginPage(driver);
        headerPage = new HeaderPage(driver);
        homePage = new HomePage(driver);
        registrationPage = new RegistrationPage(driver);
        adminPage = new AdminPage(driver);
        advancedSettingsPage = new AdvancedSettingsPage(driver);
        loginPage.loginAsAdmin();
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
    public void verifyAddressValuesDisplayedInConfirmationPage() {
        homePage.openRegisterAPatientApp();
        TestPatient patient = PatientGenerator.generateTestPatient();
        registrationPage.enterPatientGivenName(patient.givenName);
        registrationPage.enterPatientFamilyName(patient.familyName);
        registrationPage.clickOnGenderLink();
        registrationPage.selectPatientGender(patient.gender);
        registrationPage.clickOnContactInfo();
        registrationPage.clickOnAddressLink();
        registrationPage.enterPatientAddress(patient);
        // TODO clickOnPhoneNumber is not working. Something wrong with RegistrationPage.PHONE_NUMBER_LINK 
//        registrationPage.clickOnPhoneNumber();
//        registrationPage.enterPhoneNumber(patient.phone);
        registrationPage.clickOnConfirm();

        String address = patient.address1 + " " + 
        		patient.address2 + " " + 
        		patient.city + " " + 
        		patient.state + " " + 
        		patient.country + " " + 
        		patient.postalCode + " " + 
        		patient.latitude + " " + 
        		patient.longitude + " " + 
        		patient.startDate + " " + 
        		patient.endDate;
        System.out.println(address);

        assertEquals(address, registrationPage.getAddressValueInConfirmationPage());

    }


    // Test for Story RA-77
    @Ignore
    public void verifyAutoSuggestionWorksForNameFields(){
       homePage.openLegacyAdministrationApp();
       adminPage.clickOnAdvancedSettings();
            // WIP
//       advancedSettingsPage.setGivenNameAutoSuggestList();
//       advancedSettingsPage.setFamilyNameAutoSuggestList();
//       advancedSettingsPage.saveChanges();
//       headerPage.clickOnHomeLink();
//       homePage.openRegisterAPatientApp();
//       registrationPage.enterPatientGivenNameForAutoSuggestFn("a");
    }

    @After
    public void tearDown(){
        headerPage.logOut();
    }


}
