package org.openmrs.reference;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.uitestframework.test.TestBase;
import org.openmrs.uitestframework.test.TestData.PatientInfo;


public class VisitTest extends TestBase {

    private HomePage homePage;
    private HeaderPage headerPage;
	private ClinicianFacingPatientDashboardPage patientDashboardPage;
	private PatientInfo patient;

	@Before
	public void before() {
        patient = createTestPatient();
		homePage = new HomePage(page);
		patientDashboardPage = new ClinicianFacingPatientDashboardPage(page);
	}

	@After
    public void tearDown() throws Exception {
		deletePatient(patient.uuid);
        waitForPatientDeletion(patient.uuid);
    }

	/**
	 * Beginnings of visit test. For starters, we just start a visit and verify it started.
	 */
	@Test
	@Category(org.openmrs.reference.groups.BuildTests.class)
	@Ignore
	public void testStartVisit() {
//		System.out.println("test patient uuid: " + patientUuid);
		// go to patient dashboard for the just-created test patient
		patientDashboardPage.go(patient.uuid);
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
		Assert.assertEquals("Today", patientDashboardPage.findLinkToVisit().getText().trim());
	}
}
