package org.openmrs.reference;

import org.dbunit.dataset.DataSetException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
	private PatientInfo patient;

    @Before
    public void setUp() throws DataSetException, SQLException, Exception {
        headerPage = new HeaderPage(driver);
        homePage = new HomePage(driver);
        registrationPage = new RegistrationPage(driver);
        patientDashboardPage = new PatientDashboardPage(driver);
        headerPage.clickOnHomeIcon();
    	assertPage(loginPage);
        loginPage.loginAsClerk();
        assertPage(homePage);
    }

    // Test for Story RA-71
    @Test
    public void registerAPatient() {
        homePage.openRegisterAPatientApp();
        patient = createTestPatient();
        if(patient.gender == "M") {
            patient.gender = "Male";
        } else {
            patient.gender = "Female";
        }
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
        assertPage(patientDashboardPage);
		assertTrue(driver.getPageSource().contains(patient.givenName + " " + patient.familyName));
    }
    
	@After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        deletePatient(patient);
        registrationPage.waitForDeletePatient();
        headerPage.logOut();
    }

    // Test for RA-472
    @Test
    public void registerUnidentifiedPatient() {
        homePage.openRegisterAPatientApp();
        patient = createTestPatient();
        if(patient.gender == "M") {
            patient.gender = "Male";
        } else {
            patient.gender = "Female";
        }
        registrationPage.enterUnidentifiedPatient(patient);

        assertEquals("--", registrationPage.getNameInConfirmationPage());
        assertEquals(patient.gender, registrationPage.getGenderInConfirmationPage());

        registrationPage.confirmPatient();
        assertPage(patientDashboardPage);	// remember just-registered patient id, so it can be removed.
        assertTrue(driver.getPageSource().contains("UNKNOWN UNKNOWN"));
    }
}
