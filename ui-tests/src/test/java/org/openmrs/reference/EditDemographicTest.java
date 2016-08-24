package org.openmrs.reference;

/**
 * Created by nata on 22.06.15.
 */

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
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class EditDemographicTest extends TestBase {
    private StringBuffer verificationErrors = new StringBuffer();
    private HomePage homePage;
    private HeaderPage headerPage;
    private RegistrationPage registrationPage;
    private ClinicianFacingPatientDashboardPage patientDashboardPage;


    @Before
    public void setUp() throws Exception {

        
        homePage = new HomePage(page);
        assertPage(homePage);
        headerPage = new HeaderPage(driver);
        registrationPage = new RegistrationPage(page);
        patientDashboardPage = new ClinicianFacingPatientDashboardPage(page);

    }

    @Test
    public void editDemographicTest() throws Exception {
        homePage.goToActiveVisitPatient();
        patientDashboardPage.clickOnEditPatient();
        registrationPage.clearName();
        registrationPage.enterPatientGivenName("John");
        registrationPage.clearMiddleName();
        registrationPage.enterPatientMiddleName("Bob");
        registrationPage.clearFamilyName();
        registrationPage.enterPatientFamilyName("Smith");
        registrationPage.clickOnGenderLink();
        registrationPage.selectPatientGender("Male");
        registrationPage.clickOnBirthdateLabel();
        registrationPage.clearBirthDay();
        registrationPage.enterBirthDay("02");
        registrationPage.selectBirthMonth("April");
        registrationPage.clearBirthdateYear();
        registrationPage.enterBirthYear("1985");
        registrationPage.clickOnConfirmEdit();
        registrationPage.confirmPatient();
        assertTrue(driver.getPageSource().contains("Saved changes in contact info for: John Bob Smith"));
    }

    @After
    public void tearDown() throws Exception {

        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }


}
