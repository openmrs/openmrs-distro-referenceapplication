package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.helper.TestPatient;
import org.openmrs.reference.page.*;
import org.openmrs.uitestframework.test.TestBase;
import org.openmrs.uitestframework.test.TestData;

import static org.junit.Assert.assertFalse;


/**
 * Created by tomasz on 09.07.15.
 */

public class RecordMergeIssueTest extends TestBase {
    private HomePage homePage;
    private HeaderPage headerPage;
    private FindPatientPage findPatientPage;
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
        headerPage = new HeaderPage(driver);
        findPatientPage = new FindPatientPage(page);
        registrationPage = new RegistrationPage(page);
        patientDashboardPage = new ClinicianFacingPatientDashboardPage(page);
        dataManagementPage = new DataManagementPage(page);
        patient = new TestPatient();
        patient1 = new TestPatient();
    }

    @Test
    @Category(BuildTests.class)
    public void recordMergeIssueTest() throws Exception {
        homePage.goToRegisterPatientApp().waitForPage();
//       Register first patient
        patient.familyName = "Mike";
        patient.givenName = "Smith";
        patient.gender = "Male";
        patient.estimatedYears = "25";
        patient.address1 = "address";
        registrationPage.enterMergePatient(patient);
        id = patientDashboardPage.findPatientId();
        patient.uuid = patientDashboardPage.getPatientUuidFromUrl();
        headerPage.clickOnHomeIcon();
//     Register second patient
        homePage.goToRegisterPatientApp();
        patient1.familyName = "Mike";
        patient1.givenName = "Kowalski";
        patient1.gender = "Male";
        patient1.estimatedYears = "25";
        patient1.address1 = "address";
        registrationPage.enterMergePatient(patient1);
        id2 = patientDashboardPage.findPatientId();
        headerPage.clickOnHomeIcon();
        homePage.goToDataManagement();
        dataManagementPage.goToMergePatient();
        dataManagementPage.enterPatient1(id);
        dataManagementPage.enterPatient2(id2);
        dataManagementPage.clickOnContinue();
        assertFalse(driver.getPageSource().contains("java.lang.NullPointerException"));
    }


    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        TestData.PatientInfo p = new TestData.PatientInfo();
        p.uuid = patient.uuid;
        deletePatient(p);
        waitForPatientDeletion(patient.uuid);
    }
}
