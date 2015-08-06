package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.ActiveVisitsPage;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.PatientDashboardPage;
import org.openmrs.uitestframework.test.TestBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by tomasz on 09.07.15.
 */
public class ActiveVisitsSearchPatientTest extends TestBase {

    private HomePage homePage;
    private PatientDashboardPage patientDashboardPage;
    private HeaderPage headerPage;
    private ActiveVisitsPage activeVisitsPage;


    @Before
    public void setUp() throws Exception {
        loginPage.loginAsAdmin();
        homePage = new HomePage(driver);
        assertPage(homePage);
        activeVisitsPage = new ActiveVisitsPage(driver);
        headerPage = new HeaderPage(driver);
    }
    @Test
    public void searchPatientTest() throws Exception {
        homePage.goToActiveVisitsSearch();
//        assertPage(activeVisitsPage);
        String patientName = activeVisitsPage.getPatientName();
        activeVisitsPage.search(patientName);
        assertTrue(activeVisitsPage.getPatientName().contains(patientName));
        String patientId = activeVisitsPage.getPatientId();
        activeVisitsPage.search(patientId);
        assertTrue(activeVisitsPage.getPatientId().contains(patientId));
        String lastSeen = activeVisitsPage.getPatientLastSeen();
        activeVisitsPage.search(lastSeen);
        assertTrue(activeVisitsPage.getPatientLastSeen().contains(lastSeen));
    }

    @After
    public void tearDown ()throws Exception {
        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }
}
