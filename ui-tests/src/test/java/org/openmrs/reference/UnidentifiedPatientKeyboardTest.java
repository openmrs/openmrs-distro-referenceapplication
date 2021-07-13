package org.openmrs.reference;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.helper.PatientGenerator;
import org.openmrs.reference.helper.TestPatient;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.RegistrationPage;
import org.openmrs.uitestframework.test.TestBase;
import org.openmrs.uitestframework.test.TestData;

/**
 * Created by tomasz on 22.07.15.
 */
public class UnidentifiedPatientKeyboardTest extends TestBase {
    private HeaderPage headerPage;
    private RegistrationPage registrationPage;
    private HomePage homePage;
    private ClinicianFacingPatientDashboardPage patientDashboardPage;
    private TestPatient patient;

    @Before
    public void setUp() throws Exception {
        homePage = new HomePage(page);
        registrationPage = new RegistrationPage(page);
        patientDashboardPage = new ClinicianFacingPatientDashboardPage(page);
        assertPage(homePage.waitForPage());
    }

    @After
    public void tearDown() throws Exception {
        TestData.PatientInfo p = new TestData.PatientInfo();
        p.uuid = patient.uuid;
        deletePatient(p);
        waitForPatientDeletion(patient.uuid);
    }

    @Test
    @Category(BuildTests.class)
    public void unidentifiedPatientKeyboardTest() throws InterruptedException {
        homePage.goToRegisterPatientApp();
        patient = PatientGenerator.generateTestPatient();
        registrationPage.enterUnidentifiedPatient(patient);

        assertTrue(registrationPage.getNameInConfirmationPage().contains("--"));
        assertTrue(registrationPage.getGenderInConfirmationPage().contains(patient.gender));

        patientDashboardPage = registrationPage.confirmPatient();
        patientDashboardPage.waitForPage();
        
        patient.uuid = patientDashboardPage.getPatientUuidFromUrl();
        assertPage(patientDashboardPage.waitForPage()); // remember just-registered patient id, so it can be removed.
        assertTrue(driver.getPageSource().contains("UNKNOWN UNKNOWN"));
    }
}
