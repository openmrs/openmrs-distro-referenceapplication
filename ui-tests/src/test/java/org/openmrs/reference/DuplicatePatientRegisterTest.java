package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
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
        patient1 = PatientGenerator.generateTestPatient();

        assertPage(page);
        
        homePage = new HomePage(page);
        registrationPage = new RegistrationPage(page);
        assertPage(homePage);
    }

    private void registerAPatient(TestPatient patient) throws InterruptedException {

        homePage.goToRegisterPatientApp();
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
        deletePatient(patient1.uuid);
        waitForPatientDeletion(patient1.uuid);
        headerPage.logOut();
    }

    // Test for RA-714
    @Test
    public void duplicateRegisterTest() throws InterruptedException {

        registerAPatient(patient1);
        registrationPage.confirmPatient();
        patient1.uuid = patientIdFromUrl();
        headerPage.clickOnHomeIcon();
//        assertPage(homePage);
        registerAPatient(patient1);
        assertTrue(driver.getPageSource().contains("There seems to be a patient in the database that exactly matches this one. Please review before confirming:"));
        assertTrue(registrationPage.clickOnReviewButton());
    }
}
