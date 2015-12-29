package org.openmrs.reference;

import static org.junit.Assert.assertTrue;

/**
 * Created by nata on 15.07.15.
 */
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.LocationPage;
import org.openmrs.uitestframework.test.TestBase;

@Ignore
public class AddLocationTest extends TestBase {
    private HomePage homePage;
    private ClinicianFacingPatientDashboardPage patientDashboardPage;
    private HeaderPage headerPage;
    private LocationPage locationPage;



    @Before
    public void setUp() throws Exception {
        homePage = new HomePage(page);
        assertPage(homePage);
        patientDashboardPage = new ClinicianFacingPatientDashboardPage(page);
        headerPage = new HeaderPage(driver);
        locationPage = new LocationPage(driver);
        homePage.goToAdministration();
    }

    @Test
    @Ignore //ignored due to unreachable browser exception
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
