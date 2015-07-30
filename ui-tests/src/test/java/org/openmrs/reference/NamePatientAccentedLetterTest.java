package org.openmrs.reference;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.helper.TestPatient;
import org.openmrs.reference.page.*;
import org.openmrs.uitestframework.test.TestBase;
import org.junit.*;
import static org.junit.Assert.assertTrue;


/**
 * Created by nata on 24.07.15.
 */

public class NamePatientAccentedLetterTest extends TestBase {
    private HomePage homePage;
    private HeaderPage headerPage;
    private TestPatient patient;
    private RegistrationPage registrationPage;
    private PatientDashboardPage patientDashboardPage;

    @Before
    public void setUp() throws Exception {
        homePage = new HomePage(driver);
        loginPage.loginAsAdmin();
        assertPage(homePage);
        headerPage = new HeaderPage(driver);
        registrationPage = new RegistrationPage(driver);
        patientDashboardPage = new PatientDashboardPage(driver);
        patient = new TestPatient();
    }

    @Ignore
    @Test
    public void namePatientAccentedLetterTest() throws Exception {
        homePage.openRegisterAPatientApp();
        patient.familyName = "Kłoczkowski";
        patient.givenName = "Mike";
        patient.gender = "Male";
        patient.estimatedYears = "25";
        patient.address1 = "5109 Jumillá";
        registrationPage.enterMegrePatient(patient);
        patientDashboardPage.waitForVisitLink();
        assertTrue(patientDashboardPage.visitLink().getText().contains("Created Patient Record: " + patient.givenName +" " + patient.familyName));
        patient.Uuid =  patientIdFromUrl();
    }



    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        deletePatient(patient.Uuid);
        waitForPatientDeletion(patient.Uuid);
        headerPage.logOut();
    }

}
