/**
* The contents of this file are subject to the OpenMRS Public License
* Version 1.0 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://license.openmrs.org
*
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
*
* Copyright (C) OpenMRS, LLC.  All Rights Reserved.
*/
package org.openmrs.reference;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.PatientDashboardPage;
import org.openmrs.uitestframework.test.TestBase;
import org.openmrs.uitestframework.test.TestData.PatientInfo;

public class VisitActionsTest extends TestBase {

	private HomePage homePage;
	private PatientDashboardPage patientDashboardPage;
	private PatientInfo patient;

	@Before
	public void before() {
		patient = createTestPatient();
		homePage = new HomePage(driver);
		patientDashboardPage = new PatientDashboardPage(driver);
		assertPage(loginPage);
		loginPage.loginAsAdmin();
		assertPage(homePage);
		currentPage().gotoPage(
				PatientDashboardPage.URL_PATH + "?patientId=" + patient.uuid);
		assertPage(patientDashboardPage);
		patientDashboardPage.startVisit();
		patientDashboardPage.admitInpatient();
	}

	@After
	public void tearDown() throws Exception {
		deletePatient(patient);
		currentPage().gotoPage("/logout");
		assertPage(loginPage);
	}

	@Test
	public void testCaptureVitals() {
		currentPage().gotoPage(
				PatientDashboardPage.URL_PATH + "?patientId=" + patient.uuid);
		patientDashboardPage.captureVitals();
		Assert.assertTrue(patientDashboardPage.hasExitBtn());
	}

	@Test
	public void testTransfer() {
		currentPage().gotoPage(
				PatientDashboardPage.URL_PATH + "?patientId=" + patient.uuid);
		patientDashboardPage.transfer();
		Assert.assertTrue(patientDashboardPage.hasDischarge());
	}

	@Test
	public void testExit() {
		currentPage().gotoPage(
				PatientDashboardPage.URL_PATH + "?patientId=" + patient.uuid);

		patientDashboardPage.exitInpatient();
		Assert.assertTrue(patientDashboardPage.hasDropdown());
	}

	@Test
	public void testVisitNote() {
		currentPage().gotoPage(
				PatientDashboardPage.URL_PATH + "?patientId=" + patient.uuid);
		patientDashboardPage.visitNote();
		Assert.assertTrue(patientDashboardPage.hasSearchContainer());
	}

	@Test
	public void endVisit() {
		currentPage().gotoPage(
				PatientDashboardPage.URL_PATH + "?patientId=" + patient.uuid);
		patientDashboardPage.endVisit();
		Assert.assertTrue(patientDashboardPage.hasEndDialog());
	}
}