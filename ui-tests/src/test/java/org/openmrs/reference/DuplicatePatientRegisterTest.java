package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.helper.TestPatient;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.RegistrationPage;
import org.openmrs.uitestframework.test.TestBase;
import org.openmrs.uitestframework.test.TestData.PatientInfo;
import static org.openmrs.uitestframework.test.TestData.checkIfPatientExists;

import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertTrue;


public class DuplicatePatientRegisterTest  extends TestBase {
    private HeaderPage headerPage;
    private RegistrationPage registrationPage;
    private HomePage homePage;
    private TestPatient patient1;
    private TestPatient patient2;

    @Before
    public void setUp() {
        headerPage = new HeaderPage(driver);
        homePage = new HomePage(driver);
        registrationPage = new RegistrationPage(driver);
        headerPage.clickOnHomeIcon();
        patient1 = new TestPatient();
        patient2 = new TestPatient();

        assertPage(loginPage);
        loginPage.loginAsAdmin();
        assertPage(homePage);
    }

    private void registerAPatient(TestPatient patient) {

        homePage.openRegisterAPatientApp();
        patient.familyName = "Smith";
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
        deletePatientUuid(patient1.Uuid);
        waitForPatientDeletion(patient1.Uuid);
        headerPage.logOut();
    }

    // Test for RA-714
    @Test
    public void DuplicateRegisterTest() {

        registerAPatient(patient1);
        registrationPage.confirmPatient();
        patient1.Uuid = patientIdFromUrl();
        headerPage.clickOnHomeIcon();
        assertPage(homePage);
        registerAPatient(patient2);
        assertTrue(driver.getPageSource().contains("There seems to be a patient in the database that exactly matches this one. Please review before confirming:"));
        assertTrue(registrationPage.clickOnReviewButton());
    }
}
