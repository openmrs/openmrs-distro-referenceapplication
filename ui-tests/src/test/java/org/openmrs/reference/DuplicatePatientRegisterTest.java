package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
    private PatientInfo patient;

    @Before
    public void setUp() {
        headerPage = new HeaderPage(driver);
        homePage = new HomePage(driver);
        registrationPage = new RegistrationPage(driver);
        headerPage.clickOnHomeIcon();
        patient = createTestPatient();

        assertPage(loginPage);
        loginPage.loginAsAdmin();
        assertPage(homePage);
    }

    private void registerAPatient() {

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

    private void waitForPatientDeletion(String uuid) throws Exception {
        registrationPage.waitForDeletePatient();
        Long startTime = System.currentTimeMillis();
        while(checkIfPatientExists(uuid)) {
            Thread.sleep(200);
            if(System.currentTimeMillis() - startTime > 30000) {
                throw new TimeoutException("Patient not deleted in expected time");
            }
        }
    }

    @After
    public void tearDown() throws Exception {
        registrationPage.exitReview();
        headerPage.clickOnHomeIcon();
        deletePatient(patient);
        waitForPatientDeletion(patient.uuid);

        headerPage.logOut();
    }

    // Test for RA-714
    @Test
    public void DuplicateRegisterTest() {

        registerAPatient();
        registrationPage.confirmPatient();
        headerPage.clickOnHomeIcon();
        assertPage(homePage);
        registerAPatient();
        assertTrue(driver.getPageSource().contains("There seems to be a patient in the database that exactly matches this one. Please review before confirming:"));
        assertTrue(registrationPage.clickOnReviewButton());
    }
}
