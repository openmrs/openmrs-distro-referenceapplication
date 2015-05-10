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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.ActiveVisitsPage;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.LocationPage;
import org.openmrs.reference.page.ManageLocationsPage;
import org.openmrs.reference.page.PatientDashboardPage;
import org.openmrs.reference.page.VisitNotesPage;
import org.openmrs.uitestframework.test.TestBase;

public class ManageLocationsTest extends TestBase {
	
	private HeaderPage headerPage;
	private HomePage homePage;
	private ManageLocationsPage manageLocationsPage;
	private ActiveVisitsPage activeVisitsPage;
	private PatientDashboardPage patientDashboardPage;
	private VisitNotesPage visitNotesPage;
	private LocationPage locationPage;
	
	private String TEST_LOCATION_NAME = "Test Location";
	private String RENAMED_LOCATION_NAME = "Renamed Location";
	
	@Before
	public void before() {
		headerPage = new HeaderPage(driver);
        homePage = new HomePage(driver);
        manageLocationsPage = new ManageLocationsPage(driver);
        activeVisitsPage = new ActiveVisitsPage(driver);
        patientDashboardPage = new PatientDashboardPage(driver);
        visitNotesPage = new VisitNotesPage(driver);
        locationPage = new LocationPage(driver);
        
        // Login
        login();
        assertPage(homePage);

        // Go to manage page
        manageLocationsPage.go();
        assertPage(manageLocationsPage);
	}
	
	@After
	public void after() {
		// Delete location
		manageLocationsPage.go();
        assertPage(manageLocationsPage);
        manageLocationsPage.deleteLocation(TEST_LOCATION_NAME);
		
        // Logout
		homePage.go();
		assertPage(homePage);
		
		headerPage.logOut();
		assertPage(loginPage);
	}
	
	@Test
	public void newLocationWithoutTagsTest() {
		// Add location
		manageLocationsPage.addLocation();
		manageLocationsPage.enterName(TEST_LOCATION_NAME);
		manageLocationsPage.saveLocation();
		
		// Logout
		homePage.go();
		assertPage(homePage);
		
		headerPage.logOut();
		assertPage(loginPage);
		
		// Check new location
		assertFalse(manageLocationsPage.getLocationsFromLoginPage().contains(TEST_LOCATION_NAME));
	
		// Login
		login();
        assertPage(homePage);
	}
	
	@Test
	public void newLocationAdmissionTest() {
		// Add location
		manageLocationsPage.addLocation();
		manageLocationsPage.enterName(TEST_LOCATION_NAME);
		manageLocationsPage.selectTag(0);
		manageLocationsPage.saveLocation();
		
		// Go to patient visit
		currentPage().gotoPage("/coreapps/activeVisits.page?app=coreapps.activeVisits");
		assertPage(activeVisitsPage);
		
		activeVisitsPage.clickFirstPatient();
		assertPage(patientDashboardPage);
		
		patientDashboardPage.editVisits(driver.getCurrentUrl());
		visitNotesPage.editLastVisitNote();
		
		// Check new location
		assertTrue(visitNotesPage.getLocations().contains(TEST_LOCATION_NAME));
	}
	
	@Test
	public void newLocationLoginTest() {
		// Add location
		manageLocationsPage.addLocation();
		manageLocationsPage.enterName(TEST_LOCATION_NAME);
		manageLocationsPage.selectTag(1);
		manageLocationsPage.saveLocation();
		
		// Logout
		homePage.go();
		assertPage(homePage);
		
		headerPage.logOut();
		assertPage(loginPage);
		
		// Check new location
		assertTrue(manageLocationsPage.getLocationsFromLoginPage().contains(TEST_LOCATION_NAME));
	
		// Login
		login();
        assertPage(homePage);
	}
	
	@Test
	public void newLocationTransferTest() {
		// Add location
		manageLocationsPage.addLocation();
		manageLocationsPage.enterName(TEST_LOCATION_NAME);
		manageLocationsPage.selectTag(2);
		manageLocationsPage.saveLocation();
		
		// Go to patient visit
		currentPage().gotoPage("/coreapps/activeVisits.page?app=coreapps.activeVisits");
		assertPage(activeVisitsPage);
		
		activeVisitsPage.clickFirstPatient();
		assertPage(patientDashboardPage);
		
		patientDashboardPage.clickVisitAction(3);
		
		// Check new location
		assertTrue(visitNotesPage.getLocations().contains(TEST_LOCATION_NAME));
	}
	
	@Test
	public void newLocationVisitTest() {
		// By default all locations marked as "Visit Location", so we check unmarked location
		// Add location
		manageLocationsPage.addLocation();
		manageLocationsPage.selectTag(1);
		manageLocationsPage.enterName(TEST_LOCATION_NAME);
		manageLocationsPage.saveLocation();
		
		// Logout
		homePage.go();
		assertPage(homePage);
		
		headerPage.logOut();
		assertPage(loginPage);
				
		// Login
		locationPage.clickOnLocation(TEST_LOCATION_NAME);
		locationPage.loginWithoutChooseLocation();
		
		// Go to patient visit
		currentPage().gotoPage("/coreapps/activeVisits.page?app=coreapps.activeVisits");
		assertFalse(activeVisitsPage.isLoaded());
	}
	
	@Test
	public void renameLocationTest() {
		// Add temp location
		manageLocationsPage.addLocation();
		manageLocationsPage.enterName(TEST_LOCATION_NAME);
		manageLocationsPage.saveLocation();
		
		// Edit location
		String oldName = "Laboratory";
		manageLocationsPage.editLocation(oldName);
		manageLocationsPage.enterName(RENAMED_LOCATION_NAME);
		manageLocationsPage.saveLocation();
		
		// Logout
		homePage.go();
		assertPage(homePage);
		
		headerPage.logOut();
		assertPage(loginPage);
		
		// Check new location
		assertTrue(manageLocationsPage.getLocationsFromLoginPage().contains(RENAMED_LOCATION_NAME));

		// Login
		login();
        assertPage(homePage);
		
		// Go to manage page
		manageLocationsPage.go();
        assertPage(manageLocationsPage);
		
		// Rename location to old
		manageLocationsPage.editLocation(RENAMED_LOCATION_NAME);
		manageLocationsPage.enterName(oldName);
		manageLocationsPage.saveLocation();
	}
}
