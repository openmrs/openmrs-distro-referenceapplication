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

public class MergePatientByNameTest extends TestBase {
    private HomePage homePage;
    private HeaderPage headerPage;
    private FindPatientPage findPatientPage;
    private TestPatient patient;
    private TestPatient patient1;
    private RegistrationPage registrationPage;
    private PatientDashboardPage patientDashboardPage;
    private DataManagementPage dataManagementPage;
    private String id;
    private String id2;


    @Before
    public void setUp() throws Exception {
        loginPage.loginAsAdmin();
        homePage = new HomePage(driver);
        assertPage(homePage);
        headerPage = new HeaderPage(driver);
        findPatientPage = new FindPatientPage(driver);
        registrationPage = new RegistrationPage(driver);
        patientDashboardPage = new PatientDashboardPage(driver);
        dataManagementPage = new DataManagementPage(driver);
        patient = new TestPatient();
        patient1 = new TestPatient();


    }

    @Test
    @Ignore
    public void mergePatientByNameTest() throws Exception {
        homePage.openRegisterAPatientApp();
//       Register first patient
        patient.familyName = "Mike";
        patient.givenName = "Smith";
        patient.gender = "Male";
        patient.estimatedYears = "25";
        patient.address1 = "address";
        registrationPage.enterMegrePatient(patient);
        id = patientDashboardPage.findPatientId();
        patient.Uuid =  patientIdFromUrl();
        headerPage.clickOnHomeIcon();
//     Register second patient
        homePage.openRegisterAPatientApp();
        patient1.familyName = "Mike";
        patient1.givenName = "Kowalski";
        patient1.gender = "Male";
        patient1.estimatedYears = "25";
        patient1.address1 = "address";
        registrationPage.enterMegrePatient(patient1);
        id2 = patientDashboardPage.findPatientId();
        patient.Uuid =  patientIdFromUrl();
        headerPage.clickOnHomeIcon();
        homePage.goToDataMagament();
        dataManagementPage.goToMegrePatient();
        dataManagementPage.enterPatient1(id);
        dataManagementPage.enterPatient2(id2);
        dataManagementPage.searchId(id);
        dataManagementPage.clickOnContinue();
        dataManagementPage.clickOnMergePatient();
        dataManagementPage.clickOnContinue();
        headerPage.clickOnHomeLink();
        assertTrue(patientDashboardPage.visitLink().getText().contains("Records merged! Viewing preferred patient."));
        homePage.clickOnFindPatientRecord();
        findPatientPage.enterPatient(patient.givenName);
        assertTrue(driver.getPageSource().contains(patient1.givenName));
        findPatientPage.enterPatient(patient1.givenName);
        assertTrue(driver.getPageSource().contains(patient.givenName));
    }



    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        deletePatient(patient.Uuid);
        waitForPatientDeletion(patient.Uuid);
        headerPage.logOut();
    }

}
