package org.openmrs.reference;

/**
 * Created by nata on 15.07.15.
 */
import org.junit.*;
import static org.junit.Assert.*;

import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.LocationPage;
import org.openmrs.reference.page.PatientDashboardPage;
import org.openmrs.uitestframework.test.TestBase;


public class AddLocationTest extends TestBase {
    private HomePage homePage;
    private PatientDashboardPage patientDashboardPage;
    private HeaderPage headerPage;
    private LocationPage locationPage;



    @Before
    public void setUp() throws Exception {

        homePage = new HomePage(driver);
        loginPage.loginAsAdmin();
        assertPage(homePage);
        patientDashboardPage = new PatientDashboardPage(driver);
        headerPage = new HeaderPage(driver);
        locationPage = new LocationPage(driver);
        homePage.goToAdministration();
    }
    
    @Test
    public void addLocationTest() throws Exception {
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
        locationPage.checkLocation();
        locationPage.clickOnDelete();

    }

    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeLink();
        headerPage.logOut();
    }

}
