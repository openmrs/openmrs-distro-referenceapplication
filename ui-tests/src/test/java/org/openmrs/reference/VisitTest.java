package org.openmrs.reference;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.PatientDashboardPage;
import org.openmrs.uitestframework.page.LoginPage;
import org.openmrs.uitestframework.test.TestBase;


public class VisitTest extends TestBase {

    private LoginPage loginPage;
    private HomePage homePage;
	private PatientDashboardPage patientDashboardPage;
	private String patientUuid;
	
	@Before
	public void before() {
		patientUuid = createTestPatient();
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        patientDashboardPage = new PatientDashboardPage(driver);
    	assertPage(loginPage);
        loginPage.loginAsNurse();
        assertPage(homePage);
	}
	
	@After
    public void tearDown() throws Exception {
		deletePatientUuid(patientUuid);
		dbUnitTearDown();
    }

	/**
	 * Beginnings of visit test. For starters, we just start a visit and verify it started.
	 */
	@Test
	public void testStartVisit() {
//		System.out.println("test patient uuid: " + patientUuid);
		currentPage().gotoPage(PatientDashboardPage.URL_PATH + "?patientId=" + patientUuid);
		assertPage(patientDashboardPage);
		patientDashboardPage.startVisit();
		Assert.assertTrue(patientDashboardPage.hasActiveVisit());
		Assert.assertNotNull(patientDashboardPage.endVisitLink());
	}
}
