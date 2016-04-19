package org.openmrs.reference;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.AWTException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.helper.TestPatient;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.RegistrationPage;
import org.openmrs.uitestframework.test.TestBase;

/**
 * Created by tomasz on 25.06.15.
 */
public class RegistrationTest extends TestBase {

    private RegistrationPage registrationPage;
    private ClinicianFacingPatientDashboardPage patientDashboardPage;
    private HomePage homePage;
    private TestPatient patient1;

    @Before
    public void setUp() throws AWTException {
        patient1 = new TestPatient();

        assertPage(page);
        
        homePage = new HomePage(page);
        registrationPage = new RegistrationPage(driver);
        patientDashboardPage = new ClinicianFacingPatientDashboardPage(page);
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
        deletePatient(patient1.Uuid);
//        waitForPatientDeletion(patient1.Uuid);
    }

    // Test for RA-711
    @Test
    public void registerTest() throws InterruptedException {

        registerAPatient(patient1);
        registrationPage.confirmPatient();
        assertNotNull(patientDashboardPage.visitLink());
        patient1.Uuid = patientIdFromUrl();
    }
}
