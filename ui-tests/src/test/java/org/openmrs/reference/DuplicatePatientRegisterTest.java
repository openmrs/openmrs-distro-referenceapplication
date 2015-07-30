package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.reference.helper.PatientGenerator;
import org.openmrs.reference.helper.TestPatient;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.RegistrationPage;
import org.openmrs.uitestframework.test.TestBase;

import java.awt.*;

import static org.junit.Assert.assertTrue;


public class DuplicatePatientRegisterTest  extends TestBase {
    private HeaderPage headerPage;
    private RegistrationPage registrationPage;
    private HomePage homePage;
    private TestPatient patient1;

    @Before
    public void setUp() throws AWTException {
        headerPage = new HeaderPage(driver);
        homePage = new HomePage(driver);
        registrationPage = new RegistrationPage(driver);
        patient1 = PatientGenerator.generateTestPatient();

        assertPage(loginPage);
        loginPage.loginAsAdmin();
        assertPage(homePage);
    }

    private void registerAPatient(TestPatient patient) throws InterruptedException {

        homePage.openRegisterAPatientApp();
        patient.familyName = "Smith";
        patient.middleName = "";
        patient.givenName = "Bob";
        patient.gender = "Male";
        patient.birthYear = "1999";
        patient.birthMonth = "January";
        patient.birthDay = "1";
        patient.address1 = "address";
        patient.address2 = "address";
        registrationPage.enterPatient(patient);
    }


    @After
    public void tearDown() throws Exception {
        registrationPage.exitReview();
        headerPage.clickOnHomeIcon();
        deletePatient(patient1.Uuid);
        waitForPatientDeletion(patient1.Uuid);
        headerPage.logOut();
    }

    // Test for RA-714
    @Ignore
    @Test
    public void duplicateRegisterTest() throws InterruptedException {

        registerAPatient(patient1);
        registrationPage.confirmPatient();
        patient1.Uuid = patientIdFromUrl();
        headerPage.clickOnHomeIcon();
        assertPage(homePage);
        registerAPatient(patient1);
        assertTrue(driver.getPageSource().contains("There seems to be a patient in the database that exactly matches this one. Please review before confirming:"));
        assertTrue(registrationPage.clickOnReviewButton());
    }
}
