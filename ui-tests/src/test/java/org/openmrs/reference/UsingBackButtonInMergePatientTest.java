package org.openmrs.reference;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.helper.TestPatient;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.reference.page.DataManagementPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.RegistrationPage;
import org.openmrs.uitestframework.test.TestBase;
import org.openmrs.uitestframework.test.TestData;

/**
 * Created by nata on 24.07.15.
 */

public class UsingBackButtonInMergePatientTest extends TestBase {

    private HomePage homePage;
    private TestPatient patient;
    private TestPatient patient1;
    private RegistrationPage registrationPage;
    private ClinicianFacingPatientDashboardPage patientDashboardPage;
    private DataManagementPage dataManagementPage;
    private String id;
    private String id2;

    @Before
    public void setUp() throws Exception {
        homePage = new HomePage(page);
        assertPage(homePage.waitForPage());
        registrationPage = new RegistrationPage(page);
        patientDashboardPage = new ClinicianFacingPatientDashboardPage(page);
        dataManagementPage = new DataManagementPage(page);
        patient = new TestPatient();
        patient1 = new TestPatient();
    }

    @Test
    @Category(BuildTests.class)
    public void usingBackButtonInMergePatientTest() throws Exception {
        homePage.goToRegisterPatientApp().waitForPage();
        patient.familyName = "Potter";
        patient.givenName = "John";
        patient.gender = "Male";
        patient.estimatedYears = "45";
        patient.address1 = "address";
        registrationPage.enterMergePatient(patient);
        id = patientDashboardPage.findPatientId();
        patient.uuid =  patientDashboardPage.getPatientUuidFromUrl();
        homePage.go();
        homePage.goToRegisterPatientApp();
        patient1.familyName = "Smith";
        patient1.givenName = "Jane";
        patient1.gender = "Female";
        patient1.estimatedYears = "25";
        patient1.address1 = "address";
        registrationPage.enterMergePatient(patient1);
        id2 = patientDashboardPage.findPatientId();
        homePage.go();
        homePage.goToDataManagement();
        dataManagementPage.goToMergePatient();
        dataManagementPage.enterPatient1(id);
        dataManagementPage.enterPatient2(id2);
        dataManagementPage.searchId(id);
        dataManagementPage.clickOnContinue();
        dataManagementPage.clickOnNo();
        dataManagementPage.enterPatient1(id);
        assertNotNull(dataManagementPage.CONTINUE);
    }

    @After
    public void tearDown() throws Exception {
    	  homePage.go();
        TestData.PatientInfo p = new TestData.PatientInfo();
        p.uuid = patient.uuid;
        deletePatient(p);
        waitForPatientDeletion(patient.uuid);
    }
}
