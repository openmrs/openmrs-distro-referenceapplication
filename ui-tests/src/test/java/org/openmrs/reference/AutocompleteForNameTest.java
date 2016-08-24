package org.openmrs.reference;

/**
 * Created by nata on 16.07.15.
 */
import org.junit.*;
import static org.junit.Assert.*;
import org.openmrs.reference.helper.TestPatient;
import org.openmrs.reference.page.*;
import org.openmrs.uitestframework.test.TestBase;



public class AutocompleteForNameTest extends TestBase {
    private HomePage homePage;
    private ClinicianFacingPatientDashboardPage patientDashboardPage;
    private HeaderPage headerPage;
    private SettingPage settingPage;
    private TestPatient patient;
    private RegistrationPage registrationPage;


    @Before
    public void setUp() throws Exception {

        
        homePage = new HomePage(page);
        assertPage(homePage);
        patientDashboardPage = new ClinicianFacingPatientDashboardPage(page);
        headerPage = new HeaderPage(driver);
        settingPage = new SettingPage(driver);
        patient = new TestPatient();
        registrationPage = new RegistrationPage(page);
        homePage.goToAdministration();
    }

    @Ignore//ignored due to possible application logout
    @Test
    public void autocompleteForNameTest() throws Exception {
        settingPage.clickOnSetting();
        settingPage.clickOnRegistrationcore();
        settingPage.enterFamilyList("kowalski");
        settingPage.enterGivenList("samantha");
        settingPage.saveList();
        headerPage.clickOnHomeLink();
        homePage.goToRegisterPatientApp();
        patient.givenName = "sa";
        registrationPage.enterAndWaitGivenName(patient.givenName);
        assertTrue(settingPage.list().getText().contains(settingPage.GIVEN));
        patient.familyName = "ko";
        registrationPage.enterAndWaitFamilyName(patient.familyName);
        assertTrue(driver.getPageSource().contains(settingPage.FAMILY));
        headerPage.clickOnHomeIcon();
        homePage.goToAdministration();
        settingPage.clickOnSetting();
        settingPage.clickOnRegistrationcore();
        settingPage.clearFamilyList();
        settingPage.clearGivenList();
        settingPage.saveList();

    }

    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }

}
