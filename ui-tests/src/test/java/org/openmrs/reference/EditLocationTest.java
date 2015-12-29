package org.openmrs.reference;

/**
 * Created by nata on 15.07.15.
 */
import org.junit.*;
import static org.junit.Assert.*;

import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.LocationPage;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.uitestframework.test.TestBase;


public class EditLocationTest extends TestBase {
    private HomePage homePage;
    private ClinicianFacingPatientDashboardPage patientDashboardPage;
    private HeaderPage headerPage;
    private LocationPage locationPage;



    @Before
    public void setUp() throws Exception {

        loginPage.loginAsAdmin();
        homePage = new HomePage(driver);
        assertPage(homePage);
        patientDashboardPage = new ClinicianFacingPatientDashboardPage(driver);
        headerPage = new HeaderPage(driver);
        locationPage = new LocationPage(driver);
        homePage.goToAdministration();
    }

    @Test
    public void EditLocationTest() throws Exception {
        locationPage.clickOnManageLocation();
        if(locationPage.locationPresent()) {
            locationPage.checkLocation();
            locationPage.clickOnDelete();
        }
        locationPage.clickOnAddLocation();
        locationPage.enterName("psychiatric hospital");
        locationPage.chooseTags("Admission Location");
        locationPage.clickOnTags();
        locationPage.clickOnSaveLocation();
        locationPage.addedLocation();
        locationPage.clearName();
        locationPage.enterName("super psychiatric hospital");
        locationPage.chooseTags("Transfer Location");
        locationPage.chooseTags("Login Location");
        locationPage.chooseTags("Visit Location");
        locationPage.clickOnSaveLocation();
        locationPage.findLocation();
        locationPage.checkLocation();
        locationPage.clickOnDelete();

    }

    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeLink();
        headerPage.logOut();
    }

}
