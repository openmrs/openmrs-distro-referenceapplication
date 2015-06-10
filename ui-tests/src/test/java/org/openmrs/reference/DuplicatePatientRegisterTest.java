package org.openmrs.reference;

import org.openmrs.reference.helper.TestPatient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.RegistrationPage;
import org.openmrs.uitestframework.test.TestBase;

import static org.junit.Assert.assertTrue;


public class DuplicatePatientRegisterTest  extends TestBase {
    private HeaderPage headerPage;
    private RegistrationPage registrationPage;
    private HomePage homePage;
    private String registeredPatientId;
    private TestPatient patient;

    @Before
    public void setUp() {
        headerPage = new HeaderPage(driver);
        homePage = new HomePage(driver);
        registrationPage = new RegistrationPage(driver);
        headerPage.clickOnHomeIcon();
        assertPage(loginPage);
        loginPage.loginAsAdmin();
        assertPage(homePage);
    }

    private void registerAPatient() {
        homePage.openRegisterAPatientApp();
        patient = new TestPatient();
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
        if(registeredPatientId != null) {
            deletePatient(registeredPatientId);
        }
        registrationPage.waitForDeletePatient();
        headerPage.logOut();
    }

    // Test for RA-714
    @Test
    public void DuplicateRegisterTest() {

        registerAPatient();
        registrationPage.confirmPatient();
        registeredPatientId = patientIdFromUrl();
        headerPage.clickOnHomeIcon();
        assertPage(homePage);
        registerAPatient();
        assertTrue(driver.getPageSource().contains("There seems to be a patient in the database that exactly matches this one. Please review before confirming:"));
        assertTrue(registrationPage.clickOnReviewButton());
    }
}
