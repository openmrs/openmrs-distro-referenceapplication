package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.reference.helper.TestPatient;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.reference.page.RegistrationPage;
import org.openmrs.uitestframework.test.TestBase;

import java.awt.*;

import static org.junit.Assert.assertTrue;

/**
 * Created by tomasz on 27.07.15.
 */
public class RegistrationFormQuestionTest extends TestBase {

    private HeaderPage headerPage;
    private RegistrationPage registrationPage;
    private ClinicianFacingPatientDashboardPage patientDashboardPage;
    private HomePage homePage;
    private TestPatient patient;

    @Before
    public void setUp() throws AWTException {
        headerPage = new HeaderPage(driver);
        patient = new TestPatient();

        assertPage(page);
        
        homePage = new HomePage(page);
        registrationPage = new RegistrationPage(driver);
        patientDashboardPage = new ClinicianFacingPatientDashboardPage(page);
        assertPage(homePage);
    }



    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }

    //Test for RA-698
    @Test
    public void registrationFormQuestionTest() throws InterruptedException {

        homePage.openRegisterAPatientApp();
        assertTrue(driver.getPageSource().contains("What's the patient's name?"));
        patient.familyName = "Edison";
        patient.givenName = "Thomas";
        patient.gender = "Male";
        registrationPage.enterPatientGivenName(patient.givenName);
        registrationPage.enterPatientFamilyName(patient.familyName);
        registrationPage.clickOnGenderLink();
        assertTrue(driver.getPageSource().contains("What's the patient's gender?"));
        registrationPage.selectPatientGender(patient.gender);
        registrationPage.clickOnBirthDateLink();
        assertTrue(driver.getPageSource().contains("What's the patient's birth date?"));
    }
}

