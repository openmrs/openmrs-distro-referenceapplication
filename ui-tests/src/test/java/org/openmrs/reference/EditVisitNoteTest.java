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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.ActiveVisitsPage;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.PatientDashboardPage;
import org.openmrs.reference.page.VisitNotesPage;
import org.openmrs.uitestframework.test.TestBase;

public class EditVisitNoteTest extends TestBase {
	
	private HeaderPage headerPage;
	private HomePage homePage;
	private ActiveVisitsPage activeVisitsPage;
	private PatientDashboardPage patientDashboardPage;
	private VisitNotesPage visitNotesPage;
	private Random rand;
	
	private static final String DIAGNOSIS = "Pneumonia";
	private static final String NOTE = "This is a note";
	private static final String PROVIDER_NAME = "Super User";
	private static final String CONFIRMED = "Confirmed";
	private static final String PRESUMED = "Presumed";
	private static final String EDITED_DIAGNOSIS = "Psychosomatic problems";
	private static final String EDITED_NOTE = "This is an edited note";
	
	@Before
	public void before() {
		headerPage = new HeaderPage(driver);
        homePage = new HomePage(driver);
        activeVisitsPage = new ActiveVisitsPage(driver);
        patientDashboardPage = new PatientDashboardPage(driver);
        visitNotesPage = new VisitNotesPage(driver);
        rand = new Random();
        
        // Login
        login();
        assertPage(homePage);
        
        // Go to Active Visits page
        currentPage().gotoPage("/coreapps/activeVisits.page?app=coreapps.activeVisits");
    	assertPage(activeVisitsPage);
		
    	// Choose first patient
    	activeVisitsPage.clickFirstPatient();
    	assertPage(patientDashboardPage);
        
    	// Create template visit note
		patientDashboardPage.visitNote();
		patientDashboardPage.enterDiagnosis(DIAGNOSIS);
		Assert.assertEquals(DIAGNOSIS, patientDashboardPage.primaryDiagnosis());
		patientDashboardPage.enterNote(NOTE);
		patientDashboardPage.save();
		Assert.assertEquals("Today", patientDashboardPage.visitLink().getText().trim());
		
		// Go to edit visits page
		patientDashboardPage.editVisits(driver.getCurrentUrl());
	}
	
	@After
    public void after() {
		// Delete last visit
		patientDashboardPage.editVisits(driver.getCurrentUrl());
		visitNotesPage.deleteLastVisitNote();
		
		homePage.go();
		assertPage(homePage);
		
		headerPage.logOut();
		assertPage(loginPage);
    }
	
	@Test
	public void changeNoteTest() {
		// Edit note
        visitNotesPage.editLastVisitNote();
        patientDashboardPage.enterNote(EDITED_NOTE);
		patientDashboardPage.save();
		
		// Check edited note
		visitNotesPage.showDetails();
		assertEquals(visitNotesPage.getNote(), EDITED_NOTE);
		
		// Return old note
		visitNotesPage.editLastVisitNote();
        patientDashboardPage.enterNote(NOTE);
		patientDashboardPage.save();
		
		// Check old note
		visitNotesPage.showDetails();
		assertEquals(visitNotesPage.getNote(), NOTE);
	}
	
	@Test
	public void changeProviderTest() {
		// Edit provider
        visitNotesPage.editLastVisitNote();
        visitNotesPage.editChooseProvider(rand.nextInt(3) + 1);
        String providerName = visitNotesPage.editGetProviderName();
		patientDashboardPage.save();
		
		// Check new provider
		assertEquals(visitNotesPage.getProviderName(), providerName);
		
		// Return old provider
		visitNotesPage.editLastVisitNote();
        visitNotesPage.editChooseProvider(4);
		patientDashboardPage.save();
		
		// Check old provider
		assertEquals(visitNotesPage.getProviderName(), PROVIDER_NAME);
	}
	
	@Test
	public void changeSessionLocationTest() {
		// Edit location
        visitNotesPage.editLastVisitNote();
        String oldLocationName = visitNotesPage.editGetSessionLocation();
        visitNotesPage.editChooseSessionLocation(rand.nextInt(7));
        String newLocationName = visitNotesPage.editGetSessionLocation();
		patientDashboardPage.save();
		
		// Check new location
		assertEquals(visitNotesPage.getSessionLocation(), newLocationName);
		
		// Return old location
		visitNotesPage.editLastVisitNote();
		visitNotesPage.editChooseSessionLocation(oldLocationName);
		patientDashboardPage.save();
		
		// Check old location
		assertEquals(visitNotesPage.getSessionLocation(), oldLocationName);
	}
	
	@Test
	public void changeDiagnosisTest() {
		// Edit diagnosis
        visitNotesPage.editLastVisitNote();
        visitNotesPage.editDeleteDiagnosis();
        patientDashboardPage.enterDiagnosis(EDITED_DIAGNOSIS);
		assertEquals(EDITED_DIAGNOSIS, patientDashboardPage.primaryDiagnosis());
		patientDashboardPage.save();
		
		// Check new diagnosis
		visitNotesPage.showDetails();
		assertTrue(visitNotesPage.getDiagnosis().contains(EDITED_DIAGNOSIS));
		assertTrue(visitNotesPage.getAllDiagnosesString().contains(EDITED_DIAGNOSIS));
		patientDashboardPage.gotoDashboard(driver.getCurrentUrl());
		assertTrue(patientDashboardPage.containsInDiagnosis(EDITED_DIAGNOSIS));
		patientDashboardPage.editVisits(driver.getCurrentUrl());
		
		// Return old diagnosis
		visitNotesPage.editLastVisitNote();
		visitNotesPage.editDeleteDiagnosis();
		patientDashboardPage.enterDiagnosis(DIAGNOSIS);
		patientDashboardPage.save();
		
		// Check old diagnosis
		visitNotesPage.showDetails();
		assertTrue(visitNotesPage.getDiagnosis().contains(DIAGNOSIS));
		assertTrue(visitNotesPage.getAllDiagnosesString().contains(DIAGNOSIS));
		patientDashboardPage.gotoDashboard(driver.getCurrentUrl());
		assertTrue(patientDashboardPage.containsInDiagnosis(DIAGNOSIS));
		patientDashboardPage.editVisits(driver.getCurrentUrl());
	}
	
	@Test
	public void changeDiagnosisStateTest() {
		// Edit diagnosis state
        visitNotesPage.editLastVisitNote();
        visitNotesPage.checkDiagnosisState(true);
		patientDashboardPage.save();
		
		// Check new diagnosis state
		visitNotesPage.showDetails();
		assertTrue(visitNotesPage.getDiagnosis().contains(CONFIRMED));
		
		// Return old diagnosis state
		visitNotesPage.editLastVisitNote();
		visitNotesPage.checkDiagnosisState(false);
		patientDashboardPage.save();
		
		// Check old diagnosis state
		visitNotesPage.showDetails();
		assertTrue(visitNotesPage.getDiagnosis().contains(PRESUMED));
	}
}
