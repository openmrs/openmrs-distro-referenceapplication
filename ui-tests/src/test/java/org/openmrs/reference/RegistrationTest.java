package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.helper.PatientGenerator;
import org.openmrs.reference.helper.TestPatient;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.PatientDashboardPage;
import org.openmrs.reference.page.RegistrationPage;
import org.openmrs.uitestframework.test.TestBase;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by tomasz on 25.06.15.
 */
public class RegistrationTest extends TestBase {

    private HeaderPage headerPage;
    private RegistrationPage registrationPage;
    private PatientDashboardPage patientDashboardPage;
    private HomePage homePage;
    private TestPatient patient1;

    @Before
    public void setUp() {
        headerPage = new HeaderPage(driver);
        homePage = new HomePage(driver);
        registrationPage = new RegistrationPage(driver);
        patientDashboardPage = new PatientDashboardPage(driver);
        patient1 = new TestPatient();

        assertPage(loginPage);
        loginPage.loginAsAdmin();
        assertPage(homePage);
    }

    private void registerAPatient(TestPatient patient) throws InterruptedException{

        homePage.openRegisterAPatientApp();
        patient.familyName = "Edison";
        patient.givenName = "Thomas";
        patient.gender = "Male";
        registrationPage.enterPatientGivenName(patient.givenName);
        registrationPage.enterPatientFamilyName(patient.familyName);
        registrationPage.clickOnGenderLink();
        registrationPage.selectPatientGender(patient.gender);
        registrationPage.clickOnBirthDateLink();
        patient.birthMonth = "January";
        patient.birthDay = "-1";
        registrationPage.setText(registrationPage.BIRTHDAY_DAY, patient.birthDay);
        assertTrue(driver.getPageSource().contains("Minimum: 1"));
        patient.birthDay = "45";
        registrationPage.setText(registrationPage.BIRTHDAY_DAY, patient.birthDay);
        assertTrue(driver.getPageSource().contains("Maximum: 31"));
        patient.birthDay = "31";
        registrationPage.setText(registrationPage.BIRTHDAY_DAY, patient.birthDay);
        registrationPage.selectFrom(registrationPage.BIRTHDAY_MONTH, patient.birthMonth);
        patient.birthYear = "1120";
        registrationPage.setText(registrationPage.BIRTHDAY_YEAR, patient.birthYear);
        registrationPage.clickOnContactInfo();
        assertTrue(driver.getPageSource().contains("Minimum: 1895"));
        patient.birthYear = "2050";
        registrationPage.setText(registrationPage.BIRTHDAY_YEAR, patient.birthYear);
        registrationPage.clickOnContactInfo();
        assertTrue(driver.getPageSource().contains("Maximum: 2015"));
        patient.birthYear = "2005";
        registrationPage.setText(registrationPage.BIRTHDAY_YEAR, patient.birthYear);
        registrationPage.clickOnContactInfo();
        registrationPage.clickOnPhoneNumber();
        assertTrue(driver.getPageSource().contains("You need to provide a value for at least one field"));
        patient.address1 = "address";
        patient.address2 = "address";
        registrationPage.clickOnContactInfo();
        registrationPage.enterPatientAddress(patient);
        registrationPage.clickOnPhoneNumber();
        patient.phone = "ror";
        registrationPage.enterPhoneNumber(patient.phone);
        assertTrue(driver.getPageSource().contains("Must be a valid phone number (with +, -, numbers or parentheses)"));
        patient.phone = "123";
        registrationPage.enterPhoneNumber(patient.phone);
        registrationPage.clickOnConfirmSection();
    }


    @After
    public void tearDown() throws Exception {
        registrationPage.exitReview();
        headerPage.clickOnHomeIcon();
        deletePatient(patient1.Uuid);
//        waitForPatientDeletion(patient1.Uuid);
        headerPage.logOut();
    }

    // Test for RA-711
    @Test
    public void RegisterTest() throws InterruptedException {

        registerAPatient(patient1);
        registrationPage.confirmPatient();
        assertNotNull(patientDashboardPage.visitLink());
        patient1.Uuid = patientIdFromUrl();
    }
}
