package org.openmrs.reference;

/**
 * Created by nata on 16.07.15.
 */
import org.junit.*;
import static org.junit.Assert.*;

import org.openmrs.reference.page.*;
import org.openmrs.uitestframework.test.TestBase;


public class RetireLocationTest extends TestBase {
    private HomePage homePage;
    private ClinicianFacingPatientDashboardPage patientDashboardPage;
    private HeaderPage headerPage;
    private LocationPage locationPage;
    private SettingPage settingPage;


    @Before
    public void setUp() throws Exception {

        loginPage.loginAsAdmin();
        homePage = new HomePage(driver);
        assertPage(homePage);
        patientDashboardPage = new ClinicianFacingPatientDashboardPage(driver);
        headerPage = new HeaderPage(driver);
        locationPage = new LocationPage(driver);
        settingPage = new SettingPage(driver);
        homePage.goToAdministration();
    }

    @Test
    @Ignore
    public void retireLocationTest() throws Exception {
        locationPage.clickOnManageLocation();
        if(locationPage.locationPresent()) {
            locationPage.checkLocation();
            locationPage.clickOnDelete();
        }
        locationPage.clickOnAddLocation();
        locationPage.clickOnSaveLocation();
        assertTrue(driver.getPageSource().contains("Please fix all errors and try again."));
        locationPage.enterName("psychiatric hospital");
        locationPage.chooseTags("Admission Location");
        locationPage.clickOnTags();
        locationPage.clickOnSaveLocation();
        locationPage.addedLocation();
        locationPage.enterRetireReason("atomic bomb");
        settingPage.waitForMessage();
        assertTrue(driver.getPageSource().contains("Location retired successfully"));

    }

    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeLink();
        headerPage.logOut();
    }

}
