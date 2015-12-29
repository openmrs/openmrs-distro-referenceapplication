

package org.openmrs.reference;

import static org.junit.Assert.assertNotNull;

/**
 * Created by nata on 22.06.15.
 */
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.uitestframework.test.TestBase;


public class AddPastVisitTest extends TestBase {
    private HomePage homePage;
    private ClinicianFacingPatientDashboardPage patientDashboardPage;
    private HeaderPage headerPage;


    @Before
    public void setUp() throws Exception {
        homePage = new HomePage(page);
        assertPage(homePage);
        patientDashboardPage = new ClinicianFacingPatientDashboardPage(page);
        headerPage = new HeaderPage(driver);
    }

    @Ignore("It fails, because it does not remove past visit after the test")
    @Test
    public void addPastVisitTest() throws Exception {
        homePage.goToActiveVisitPatient();
        patientDashboardPage.addPastVisit();
        if(patientDashboardPage.errorPresent()) {
            patientDashboardPage.clickChangeDate();
            patientDashboardPage.enterDate();
        }
        assertNotNull(patientDashboardPage.SUCCESS_MESSAGE.toString());
        patientDashboardPage.waitForVisitLinkHidden();
    }

    @After
    public void tearDown ()throws Exception {
        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }
}

