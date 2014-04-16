package org.openmrs.reference;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.PatientDashboardPage;
import org.openmrs.uitestframework.test.TestBase;
import org.openmrs.uitestframework.test.TestData.PatientInfo;


public class VisitTest extends TestBase {

    private HomePage homePage;
	private PatientDashboardPage patientDashboardPage;
	private PatientInfo patient;
	
	@Before
	public void before() {
		patient = createTestPatient();
        homePage = new HomePage(driver);
        patientDashboardPage = new PatientDashboardPage(driver);
    	assertPage(loginPage);
        loginPage.loginAsDoctor();
        assertPage(homePage);
	}
	
	@After
    public void tearDown() throws Exception {
		deletePatient(patient);
    }

	/**
	 * Beginnings of visit test. For starters, we just start a visit and verify it started.
	 */
	@Test
	public void testStartVisit() {
//		System.out.println("test patient uuid: " + patientUuid);
		// go to patient dashboard for the just-created test patient
		currentPage().gotoPage(PatientDashboardPage.URL_PATH + "?patientId=" + patient.uuid);
		assertPage(patientDashboardPage);
		// start visit
		patientDashboardPage.startVisit();
		// verify visit was started
		Assert.assertTrue(patientDashboardPage.hasActiveVisit());
		Assert.assertNotNull(patientDashboardPage.endVisitLink());
		
		// visit note
		patientDashboardPage.visitNote();
		patientDashboardPage.enterDiagnosis("Pneumonia");
		Assert.assertEquals("Pneumonia", patientDashboardPage.primaryDiagnosis());
		patientDashboardPage.enterNote("this is a note");
		patientDashboardPage.save();
		Assert.assertEquals("Today", patientDashboardPage.visitLink().getText().trim());
	}
}
