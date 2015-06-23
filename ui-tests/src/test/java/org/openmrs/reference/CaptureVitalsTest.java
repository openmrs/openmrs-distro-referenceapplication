package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.CaptureVitalsPage;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.PatientCaptureVitalsPage;
import org.openmrs.reference.page.PatientDashboardPage;
import org.openmrs.reference.page.RegistrationPage;
import org.openmrs.uitestframework.test.TestBase;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.openmrs.uitestframework.test.TestData.PatientInfo;
import org.openqa.selenium.By;

import java.util.concurrent.TimeoutException;

import static org.openmrs.uitestframework.test.TestData.checkIfPatientExists;

/**
 * Created by tomasz on 25.05.15.
 */
public class CaptureVitalsTest  extends TestBase {


    private HeaderPage headerPage;
    private HomePage homePage;
    private PatientDashboardPage patientDashboardPage;
    private PatientCaptureVitalsPage patientCaptureVitalsPage;
    private PatientInfo patient;

    @Before
    public void setUp() {
        headerPage = new HeaderPage(driver);
        homePage = new HomePage(driver);
        patientDashboardPage = new PatientDashboardPage(driver);
        patientCaptureVitalsPage = new PatientCaptureVitalsPage(driver);
        headerPage.clickOnHomeIcon();
        assertPage(loginPage);
        loginPage.loginAsAdmin();
        assertPage(homePage);

    }

    private void registerAPatient() {
        homePage.openRegisterAPatientApp();
        patient = createTestPatient();
        currentPage().gotoPage(PatientDashboardPage.URL_PATH + "?patientId=" + patient.uuid);
    }

    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        deletePatient(patient);
        //waitForPatientDeletion(patient.uuid);
        headerPage.logOut();
    }

    @Test
    public void captureVital() {
        registerAPatient();
        if(!patientDashboardPage.hasActiveVisit()) {
            patientDashboardPage.startVisit();
            assertNotNull(patientDashboardPage.visitLink());
        }
        headerPage.clickOnHomeIcon();
        assertPage(homePage);
        homePage.captureVitals();
        currentPage().gotoPage(patientCaptureVitalsPage.URL_PATH + "?patientId="+patient.uuid);
        patientCaptureVitalsPage.checkIfRightPatient();
//        patientCaptureVitalsPage.setHeightField("0");
//        assertTrue(driver.findElement(By.id("w7")).getText().contains("Minimum: 10"));
//        patientCaptureVitalsPage.setHeightField("400");
//        assertTrue(driver.findElement(By.id("w7")).getText().contains("Maximum: 272"));
        patientCaptureVitalsPage.setHeightField("185");
//        patientCaptureVitalsPage.setWeightField("-1");
//        assertTrue(driver.findElement(By.id("w9")).getText().contains("Minimum: 0"));
//        patientCaptureVitalsPage.setWeightField("450");
//        assertTrue(driver.findElement(By.id("w9")).getText().contains("Maximum: 250"));
        patientCaptureVitalsPage.setWeightField("78");
//        patientCaptureVitalsPage.setTemperatureField("1");
//        assertTrue(driver.findElement(By.id("w11")).getText().contains("Minimum: 25"));
//        patientCaptureVitalsPage.setTemperatureField("50");
//        assertTrue(driver.findElement(By.id("w11")).getText().contains("Maximum: 43"));
        patientCaptureVitalsPage.setTemperatureField("36.6");
//        patientCaptureVitalsPage.setPulseField("-1");
//        assertTrue(driver.findElement(By.id("w13")).getText().contains("Minimum: 0"));
//        patientCaptureVitalsPage.setPulseField("500");
//        assertTrue(driver.findElement(By.id("w13")).getText().contains("Maximum: 230"));
        patientCaptureVitalsPage.setPulseField("120");
//        patientCaptureVitalsPage.setRespiratoryField("-1");
//        assertTrue(driver.findElement(By.id("w15")).getText().contains("Minimum: 0"));
//        patientCaptureVitalsPage.setRespiratoryField("999999999");
//        assertTrue(driver.findElement(By.id("w15")).getText().contains("Maximum"));
        patientCaptureVitalsPage.setRespiratoryField("110");
//        patientCaptureVitalsPage.setBloodPressureFields("10", "70");
//        assertTrue(driver.findElement(By.id("w17")).getText().contains("Minimum: 50"));
//        patientCaptureVitalsPage.setBloodPressureFields("800", "70");
//        assertTrue(driver.findElement(By.id("w17")).getText().contains("Maximum: 250"));
//        patientCaptureVitalsPage.setBloodPressureFields("120", "10");
//        assertTrue(driver.findElement(By.id("w19")).getText().contains("Minimum: 30"));
//        patientCaptureVitalsPage.setBloodPressureFields("120", "800");
//        assertTrue(driver.findElement(By.id("w19")).getText().contains("Maximum: 150"));
        patientCaptureVitalsPage.setBloodPressureFields("120", "70");
//        patientCaptureVitalsPage.setBloodOxygenSaturationField("-1");
//        assertTrue(driver.findElement(By.id("w21")).getText().contains("Minimum: 0"));
//        patientCaptureVitalsPage.setBloodOxygenSaturationField("800");
//        assertTrue(driver.findElement(By.id("w21")).getText().contains("Maximum: 100"));
        patientCaptureVitalsPage.setBloodOxygenSaturationField("50");
        patientCaptureVitalsPage.confirm();
        assertTrue(driver.getPageSource().contains("Confirm submission?"));
        assertTrue(patientCaptureVitalsPage.save());
    }


}
