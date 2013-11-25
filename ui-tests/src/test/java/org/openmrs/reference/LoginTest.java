package org.openmrs.reference;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.uitestframework.test.TestBase;

//@Ignore("temporarily disable trying to figure out why bamboo is hanging")
public class LoginTest extends TestBase {
    private HeaderPage headerPage;
    private HomePage homePage;

    @Before
    public void setUp() {
        headerPage = new HeaderPage(driver);
        homePage = new HomePage(driver);
    }
    
    @Test
    public void verifyModulesAvailableOnHomePage() throws Exception {
    	login();
        assertPage(homePage);
        assertFalse(homePage.isDispensingMedicationAppPresent());
        assertTrue(homePage.isRegisterPatientCustomizedForRefAppPresent());
        assertTrue(homePage.isFindAPatientAppPresent());
        assertTrue(homePage.isActiveVisitsAppPresent());
        assertTrue(homePage.isStyleGuideAppPresent());
        assertTrue(homePage.isSystemAdministrationAppPresent());
    	assertTrue(homePage.isCaptureVitalsAppPresent());
        headerPage.logOut();
        assertPage(loginPage);
    }

    @Test
    public void verifyClerkModulesAvailableOnHomePage() throws Exception {
    	assertPage(loginPage);
    	loginPage.loginAsClerk();
    	assertPage(homePage);
    	assertTrue(homePage.isActiveVisitsAppPresent());
    	assertTrue(homePage.isRegisterPatientCustomizedForRefAppPresent());
    	assertFalse(homePage.isDispensingMedicationAppPresent());
    	assertFalse(homePage.isFindAPatientAppPresent());
    	assertFalse(homePage.isStyleGuideAppPresent());
    	assertFalse(homePage.isSystemAdministrationAppPresent());
    	assertFalse(homePage.isCaptureVitalsAppPresent());
    	headerPage.logOut();
    	assertPage(loginPage);
    }
    
    @Test
    public void verifyDoctorModulesAvailableOnHomePage() throws Exception {
    	assertPage(loginPage);
    	loginPage.login("doctor", "Doctor123");
    	assertPage(homePage);
    	assertTrue(homePage.isActiveVisitsAppPresent());
    	assertTrue(homePage.isFindAPatientAppPresent());
    	assertFalse(homePage.isRegisterPatientCustomizedForRefAppPresent());
    	assertFalse(homePage.isDispensingMedicationAppPresent());
    	assertFalse(homePage.isStyleGuideAppPresent());
    	assertFalse(homePage.isSystemAdministrationAppPresent());
    	assertFalse(homePage.isCaptureVitalsAppPresent());
    	headerPage.logOut();
    	assertPage(loginPage);
    }
    
    @Test
    public void verifyNurseModulesAvailableOnHomePage() throws Exception {
    	assertPage(loginPage);
    	loginPage.loginAsNurse();
    	assertPage(homePage);
    	assertTrue(homePage.isFindAPatientAppPresent());
    	assertTrue(homePage.isActiveVisitsAppPresent());
    	assertTrue(homePage.isCaptureVitalsAppPresent());
    	assertFalse(homePage.isRegisterPatientCustomizedForRefAppPresent());
    	assertFalse(homePage.isDispensingMedicationAppPresent());
    	assertFalse(homePage.isStyleGuideAppPresent());
    	assertFalse(homePage.isSystemAdministrationAppPresent());
    	headerPage.logOut();
    	assertPage(loginPage);
    }
    
}
