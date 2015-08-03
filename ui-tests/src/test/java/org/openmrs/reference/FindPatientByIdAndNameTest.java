package org.openmrs.reference;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.helper.TestPatient;
import org.openmrs.reference.page.*;
import org.openmrs.uitestframework.test.TestBase;
import org.junit.*;
import static org.junit.Assert.assertTrue;


/**
 * Created by nata on 09.07.15.
 */

public class FindPatientByIdAndNameTest extends TestBase {
    private HomePage homePage;
    private HeaderPage headerPage;
    private FindPatientPage findPatientPage;
    private TestPatient patient;
    private RegistrationPage registrationPage;
    private PatientDashboardPage patientDashboardPage;
    private String id;


    @Before
    public void setUp() throws Exception {
        homePage = new HomePage(driver);
        loginPage.loginAsAdmin();
        assertPage(homePage);
        headerPage = new HeaderPage(driver);
        findPatientPage = new FindPatientPage(driver);
        registrationPage = new RegistrationPage(driver);
        patientDashboardPage = new PatientDashboardPage(driver);
        patient = new TestPatient();


    }
    @Ignore
    @Test
    public void findPatientByIdAndNameTest() throws Exception {
        homePage.openRegisterAPatientApp();
        patient.familyName = "Bob";
        patient.givenName = "Smith";
        patient.gender = "Male";
        patient.estimatedYears = "25";
        registrationPage.enterPatientGivenName(patient.givenName);
        registrationPage.enterPatientFamilyName(patient.familyName);
        registrationPage.clickOnGenderLink();
        registrationPage.selectPatientGender(patient.gender);
        registrationPage.clickOnBirthDateLink();
        registrationPage.enterEstimatedYears(patient.estimatedYears);
        registrationPage.clickOnContactInfo();
        patient.address1 = "address";
        registrationPage.enterAddress1(patient.address1);
        registrationPage.clickOnConfirmSection();
        registrationPage.confirmPatient();
        id = patientDashboardPage.findPatientId();
        patient.Uuid =  patientIdFromUrl();
        headerPage.clickOnHomeIcon();
        homePage.clickOnFindPatientRecord();
        findPatientPage.enterPatient("Bob Smith");
        assertTrue(driver.getPageSource().contains(id));
        findPatientPage.enterPatient(id);
        assertTrue(driver.getPageSource().contains(id));
    }



    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        deletePatient(patient.Uuid);
        waitForPatientDeletion(patient.Uuid);
        headerPage.logOut();
    }

}
