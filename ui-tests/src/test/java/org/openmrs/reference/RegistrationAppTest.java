package org.openmrs.reference;

import org.dbunit.dataset.DataSetException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.helper.PatientGenerator;
import org.openmrs.reference.helper.TestPatient;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.PatientDashboardPage;
import org.openmrs.reference.page.RegistrationPage;
import org.openmrs.uitestframework.test.TestBase;
import org.openmrs.uitestframework.test.TestData.PatientInfo;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RegistrationAppTest extends TestBase {
    private HeaderPage headerPage;
    private RegistrationPage registrationPage;
    private HomePage homePage;
	private PatientDashboardPage patientDashboardPage;
	private TestPatient patient;

    @Before
    public void setUp() throws DataSetException, SQLException, Exception {
        headerPage = new HeaderPage(driver);
        homePage = new HomePage(driver);
        registrationPage = new RegistrationPage(driver);
        patientDashboardPage = new PatientDashboardPage(driver);
    	assertPage(loginPage);
        loginPage.loginAsClerk();
        assertPage(homePage);
    }

    // Test for Story RA-71
    @Test
    public void registerAPatient() {
        homePage.openRegisterAPatientApp();
        patient = PatientGenerator.generateTestPatient();
        registrationPage.enterPatient(patient);

        String address = patient.address1 + " " + 
        		patient.address2 + " " + 
        		patient.city + " " + 
        		patient.state + " " + 
        		patient.country + " " + 
        		patient.postalCode;

        assertEquals(patient.givenName + " " + patient.familyName, registrationPage.getNameInConfirmationPage());
        assertEquals(patient.gender, registrationPage.getGenderInConfirmationPage());
        assertEquals(patient.birthDay + " " + patient.birthMonth + " " + patient.birthYear, registrationPage.getBirthdateInConfirmationPage());
        assertEquals(address, registrationPage.getAddressInConfirmationPage());
        assertEquals(patient.phone, registrationPage.getPhoneInConfirmationPage());
        
        registrationPage.confirmPatient();
        patient.Uuid = patientIdFromUrl();
        assertPage(patientDashboardPage);
		assertTrue(driver.getPageSource().contains(patient.givenName + " " + patient.familyName));
    }
    
	@After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        deletePatientUuid(patient.Uuid);
        registrationPage.waitForDeletePatient();
        headerPage.logOut();
    }

    // Test for RA-472
    @Test
    public void registerUnidentifiedPatient() {
        homePage.openRegisterAPatientApp();
        patient = PatientGenerator.generateTestPatient();
        registrationPage.enterUnidentifiedPatient(patient);

        assertEquals("--", registrationPage.getNameInConfirmationPage());
        assertEquals(patient.gender, registrationPage.getGenderInConfirmationPage());

        registrationPage.confirmPatient();
        patient.Uuid = patientIdFromUrl();
        assertPage(patientDashboardPage);	// remember just-registered patient id, so it can be removed.
        assertTrue(driver.getPageSource().contains("UNKNOWN UNKNOWN"));
    }
}
