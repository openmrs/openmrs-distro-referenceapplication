package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.helper.TestPatient;
import org.openmrs.reference.page.CaptureVitalsPage;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.PatientCaptureVitalsPage;
import org.openmrs.reference.page.PatientDashboardPage;
import org.openmrs.reference.page.RegistrationPage;
import org.openmrs.uitestframework.test.TestBase;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created by tomasz on 25.05.15.
 */
public class CaptureVitalsTest  extends TestBase {


    private HeaderPage headerPage;
    private RegistrationPage registrationPage;
    private HomePage homePage;
    private CaptureVitalsPage captureVitalsPage;
    private PatientDashboardPage patientDashboardPage;
    private String registeredPatientId;
    private PatientCaptureVitalsPage patientCaptureVitalsPage;
    private TestPatient patient;
    @Before
    public void setUp() {
        headerPage = new HeaderPage(driver);
        homePage = new HomePage(driver);
        registrationPage = new RegistrationPage(driver);
        patientDashboardPage = new PatientDashboardPage(driver);
        captureVitalsPage = new CaptureVitalsPage(driver);
        patientCaptureVitalsPage = new PatientCaptureVitalsPage(driver);
        headerPage.clickOnHomeIcon();
        assertPage(loginPage);
        loginPage.loginAsAdmin();
        assertPage(homePage);
        registerAPatient();
        if(!patientDashboardPage.hasActiveVisit()) {
            patientDashboardPage.startVisit();
        }
        headerPage.clickOnHomeIcon();
        assertPage(homePage);
        homePage.captureVitals();
        //assertPage(captureVitalsPage);
        captureVitalsPage.search(registeredPatientId);


    }

    private void registerAPatient() {
        homePage.openRegisterAPatientApp();
        //assertPage(registrationPage);
        patient = new TestPatient();
        patient.familyName = "Smith";
        patient.givenName = "Bob";
        patient.gender = "Male";
        patient.birthYear = "1990";
        patient.birthMonth = "January";
        patient.birthDay = "1";
        patient.address1 = "Ouagadougou";
        patient.address2 = "Burkina Faso";
        patient.city = "Ouagadougou";
        patient.country = "Burkina Faso";
        patient.postalCode = "90-911";
        patient.state = "Unknown";
        patient.phone = "888587794";
        //assertPage(registrationPage);
        registrationPage.enterPatient(patient);
        registrationPage.confirmPatient();
        registeredPatientId = patientIdFromUrl();
    }
    @After
    public void tearDown() throws Exception {
        if(registeredPatientId != null) {
            deletePatient(registeredPatientId);
        }
        Thread.sleep(5000);	// a bit of a hack, wait for "created patient" popup to disappear
        headerPage.logOut();
    }

    @Test
    public void captureVital() {
        patientCaptureVitalsPage.checkIfRightPatient();
        assertPage(patientCaptureVitalsPage);
        patientCaptureVitalsPage.setHeightField("0");
        assertTrue(driver.getPageSource().contains("Minimum:10"));
        patientCaptureVitalsPage.setHeightField("400");
        assertTrue(driver.getPageSource().contains("Maximum:272"));
        patientCaptureVitalsPage.setHeightField("185");
        patientCaptureVitalsPage.setWeightField("-1");
        assertTrue(driver.getPageSource().contains("Minimum:0"));
        patientCaptureVitalsPage.setWeightField("450");
        assertTrue(driver.getPageSource().contains("Maximum:250"));
        patientCaptureVitalsPage.setWeightField("78");
        patientCaptureVitalsPage.setTemperatureField("1");
        assertTrue(driver.getPageSource().contains("Minimum:25"));
        patientCaptureVitalsPage.setTemperatureField("50");
        assertTrue(driver.getPageSource().contains("Maximum:43"));
        patientCaptureVitalsPage.setTemperatureField("36.6");
        patientCaptureVitalsPage.setPulseField("-1");
        assertTrue(driver.getPageSource().contains("Minimum:0"));
        patientCaptureVitalsPage.setPulseField("500");
        assertTrue(driver.getPageSource().contains("Maximum:230"));
        patientCaptureVitalsPage.setPulseField("120");
        patientCaptureVitalsPage.setRespiratoryField("-1");
        assertTrue(driver.getPageSource().contains("Minimum:0"));
        patientCaptureVitalsPage.setRespiratoryField("999999999");
        assertTrue(driver.getPageSource().contains("Maximum"));
        patientCaptureVitalsPage.setRespiratoryField("110");
        patientCaptureVitalsPage.setBloodPressureFields("10", "70");
        assertTrue(driver.getPageSource().contains("Minimum:50"));
        patientCaptureVitalsPage.setBloodPressureFields("800", "70");
        assertTrue(driver.getPageSource().contains("Maximum:250"));
        patientCaptureVitalsPage.setBloodPressureFields("120", "10");
        assertTrue(driver.getPageSource().contains("Minimum:30"));
        patientCaptureVitalsPage.setBloodPressureFields("120", "800");
        assertTrue(driver.getPageSource().contains("Maximum:150"));
        patientCaptureVitalsPage.setBloodPressureFields("120", "70");
        patientCaptureVitalsPage.setBloodOxygenSaturationField("-1");
        assertTrue(driver.getPageSource().contains("Minimum:0"));
        patientCaptureVitalsPage.setBloodOxygenSaturationField("800");
        assertTrue(driver.getPageSource().contains("Maximum:100"));
        patientCaptureVitalsPage.setBloodOxygenSaturationField("50");
        patientCaptureVitalsPage.confirm();
        assertTrue(driver.getPageSource().contains("Confirm submission?"));
    }

    /*@Test
    public void captureNoNumberVital() {
        patientCaptureVitalsPage.checkIfRightPatient();
        patientCaptureVitalsPage.setHeightField("abc");
        patientCaptureVitalsPage.setWeightField("abc");
        patientCaptureVitalsPage.setTemperatureField("abc");
        patientCaptureVitalsPage.setPulseField("abc");
        patientCaptureVitalsPage.setRespiratoryField("abc");
        patientCaptureVitalsPage.setBloodPressureFields("abc", "abc");
        patientCaptureVitalsPage.setBloodOxygenSaturationField("abc");
        patientCaptureVitalsPage.confirm();
        assertFalse(driver.getPageSource().contains("Confirm submission?"));
        assertTrue(driver.getPageSource().contains("Must be a number"));
    }

    @Test
    public void captureNoNumberVital() {
        patientCaptureVitalsPage.checkIfRightPatient();
        patientCaptureVitalsPage.setHeightField("185");
        patientCaptureVitalsPage.setWeightField("-1");
        assertFalse(driver.getPageSource().contains("Confirm submission?"));
        assertTrue(driver.getPageSource().contains("Must be a number"));
    }

    @Test
    public void capture0HeightVital() {
        patientCaptureVitalsPage.checkIfRightPatient();
        patientCaptureVitalsPage.setHeightField("0");
        assertFalse(driver.getPageSource().contains("Confirm submission?"));
        assertTrue(driver.getPageSource().contains("Minimum:10"));
    }

    @Test
    public void capture400HeightVital() {
        patientCaptureVitalsPage.checkIfRightPatient();
        patientCaptureVitalsPage.setHeightField("400");
        assertFalse(driver.getPageSource().contains("Confirm submission?"));
        assertTrue(driver.getPageSource().contains("Maximum:272"));
    }

    @Test
    public void captureOverMaxRepositoryVital() {
        patientCaptureVitalsPage.checkIfRightPatient();
        patientCaptureVitalsPage.setHeightField("185");
        patientCaptureVitalsPage.setWeightField("78");
        patientCaptureVitalsPage.setTemperatureField("36.6");
        patientCaptureVitalsPage.setPulseField("120");
        patientCaptureVitalsPage.setRespiratoryField("999999999");
        patientCaptureVitalsPage.setBloodPressureFields("120", "70");
        patientCaptureVitalsPage.setBloodOxygenSaturationField("50");
        patientCaptureVitalsPage.confirm();
        assertFalse(driver.getPageSource().contains("Confirm submission?"));

    }*/
}
