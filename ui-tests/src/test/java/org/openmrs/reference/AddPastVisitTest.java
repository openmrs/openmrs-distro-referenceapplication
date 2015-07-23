

package org.openmrs.reference;

/**
 * Created by nata on 22.06.15.
 */
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.PatientDashboardPage;
import org.openmrs.uitestframework.test.TestBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class AddPastVisitTest extends TestBase {
    private HomePage homePage;
    private PatientDashboardPage patientDashboardPage;
    private HeaderPage headerPage;


    @Before
    public void setUp() throws Exception {
        homePage = new HomePage(driver);
        loginPage.loginAsAdmin();
        assertPage(homePage);
        patientDashboardPage = new PatientDashboardPage(driver);
        headerPage = new HeaderPage(driver);
    }
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

