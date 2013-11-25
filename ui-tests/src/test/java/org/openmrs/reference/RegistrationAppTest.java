package org.openmrs.reference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.dbunit.dataset.DataSetException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.reference.helper.PatientGenerator;
import org.openmrs.reference.helper.TestPatient;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.PatientDashboardPage;
import org.openmrs.reference.page.RegistrationPage;
import org.openmrs.uitestframework.test.TestBase;

//@Ignore("temporarily disable trying to figure out why bamboo is hanging")
public class RegistrationAppTest extends TestBase {
    private HeaderPage headerPage;
    private RegistrationPage registrationPage;
    private HomePage homePage;
	private PatientDashboardPage patientDashboardPage;
	private String registeredPatientId;

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
        TestPatient patient = PatientGenerator.generateTestPatient();
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
		registeredPatientId = patientIdFromUrl();	// remember just-registered patient id, so it can be removed.
		assertTrue(driver.getPageSource().contains(patient.familyName + ", " + patient.givenName));
    }
    
	@After
    public void tearDown() throws Exception {
		deletePatient(registeredPatientId);
		dbUnitTearDown();
        headerPage.logOut();
    }

}
