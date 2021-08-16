package org.openmrs.reference;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.page.FindPatientPage;
import org.openmrs.reference.page.MarkPatientDeceasedPage;
import org.openmrs.uitestframework.test.TestData;

public class MarkPatientDeceasedTest extends ReferenceApplicationTestBase {
	private TestData.PatientInfo patient;
	
	@Before
	public void setUp() throws Exception {
		patient = createTestPatient();
		createTestVisit();
	}
	@Test
	@Category(BuildTests.class)
	public void markPatientDeceasedTest() {
		homePage.goToAdministration();
		MarkPatientDeceasedPage markPatientDeceased = new MarkPatientDeceasedPage(page);
		markPatientDeceased.getConcept();
		FindPatientPage findPatientPage = homePage.goToFindPatientRecord();
		findPatientPage.enterPatient(patient.identifier);
		findPatientPage.waitForPageToLoad();
		findPatientPage.clickOnFirstPatient();
		markPatientDeceased.clickOnMarkPatientDead();
		assertTrue(markPatientDeceased.confirmDeadMessage().contains("The patient is deceased"));
		homePage.go();
		homePage.goToAdministration();
		markPatientDeceased.deleteConcept();
	}
	@After
	public void tearDown() throws Exception {
		deletePatient(patient);
	}
	private void createTestVisit() {
		new TestData.TestVisit(patient.uuid, TestData.getAVisitType(), getLocationUuid(homePage)).create();
	}
}
