package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

import org.openmrs.reference.helper.PatientGenerator;
import org.openmrs.reference.page.*;


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
    public void verifyAddressValuesDisplayedInConfirmationPage(){
        homePage.openRegisterAPatientApp();
        registrationPage.enterPatientGivenName();
        registrationPage.enterPatientFamilyName();
        registrationPage.clickOnGenderLink();
        registrationPage.selectPatientGender();
        registrationPage.clickOnContactInfo();
        registrationPage.clickOnAddressLink();
        registrationPage.enterPatientAddress();
        registrationPage.clickOnConfirm();

        String address=PatientGenerator.getPatientAddress1()+" "+PatientGenerator.getPatientAddress2()+" "+PatientGenerator.getPatientCity()+" "+PatientGenerator.getPatientState()+" "+PatientGenerator.getPatientCountry()+" 345234 12 47 01-01-2000 01-01-2010";

        assertTrue(registrationPage.getAddressValueInConfirmationPage().equals(address));

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
