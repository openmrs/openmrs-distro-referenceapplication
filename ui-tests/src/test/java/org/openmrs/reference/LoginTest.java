package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.uitestframework.test.TestBase;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class LoginTest extends TestBase {
    private HeaderPage headerPage;
    private HomePage homePage;

    @Before
    public void setUp() {
        headerPage = new HeaderPage(driver);
    }
    
    @After
    public void logout() throws InterruptedException{
    	headerPage.logOut();
    }
    @Test
    @Category(org.openmrs.reference.groups.BuildTests.class)
    public void verifyModulesAvailableOnHomePage() throws Exception {
    	loginPage.loginAsAdmin();
    	homePage = new HomePage(driver);
        assertPage(homePage);
        assertTrue(homePage.isFindAPatientAppPresent());
        assertTrue(homePage.isActiveVisitsAppPresent());
        assertTrue(homePage.isRegisterPatientCustomizedForRefAppPresent());
        assertTrue(homePage.isCaptureVitalsAppPresent());
//        assertTrue(homePage.isConfigureMetadataAppPresent());
        assertTrue(homePage.isSystemAdministrationAppPresent());
        assertThat(homePage.numberOfAppsPresent(), is(10));
    }
    @Test
    public void verifyClerkModulesAvailableOnHomePage() throws Exception {
    	loginPage.loginAsClerk();
        homePage = new HomePage(driver);
    	assertPage(homePage);
    	assertTrue(homePage.isActiveVisitsAppPresent());
    	assertTrue(homePage.isRegisterPatientCustomizedForRefAppPresent());
        assertThat(homePage.numberOfAppsPresent(), is(4));
    }
    @Test
    public void verifyDoctorModulesAvailableOnHomePage() throws Exception {
    	loginPage.login("doctor", "Doctor123");
        homePage = new HomePage(driver);
    	assertPage(homePage);
        assertTrue(homePage.isFindAPatientAppPresent());
        assertTrue(homePage.isActiveVisitsAppPresent());
        assertThat(homePage.numberOfAppsPresent(), is(3));
    }
    @Test
    public void verifyNurseModulesAvailableOnHomePage() throws Exception {
    	loginPage.loginAsNurse();
        homePage = new HomePage(driver);
    	assertPage(homePage);
    	assertTrue(homePage.isFindAPatientAppPresent());
    	assertTrue(homePage.isActiveVisitsAppPresent());
    	assertTrue(homePage.isCaptureVitalsAppPresent());
        assertThat(homePage.numberOfAppsPresent(), is(4));
    }
    @Test
    public void verifySysadminModulesAvailableOnHomePage() throws Exception {
        loginPage.loginAsSysadmin();
        homePage = new HomePage(driver);
        assertPage(homePage);
//        assertTrue(homePage.isConfigureMetadataAppPresent());
        assertTrue(homePage.isSystemAdministrationAppPresent());
        assertThat(homePage.numberOfAppsPresent(), is(2));
    }

}
