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

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.LocationPage;
import org.openmrs.uitestframework.test.TestBase;

public class SessionLocationTest extends TestBase {
	private HeaderPage headerPage;
	private HomePage homePage;
	private LocationPage locationPage;
	
	@Before
	public void before() {
		headerPage = new HeaderPage(driver);
        homePage = new HomePage(driver);
        locationPage = new LocationPage(driver);
	}
	
	@Test
	public void chooseInpatientWard() {
		String locationName = locationPage.getLocationName(0);
		locationPage.clickOnLocation(0);

		locationPage.loginWithoutChooseLocation();
		assertPage(homePage);
		
		// Checking the existence of the location name in the text "Logged as"
		assertTrue(homePage.getLoggedAsString().indexOf(locationName) != -1);
		
		homePage.go();
    	assertPage(homePage);
    	
    	headerPage.logOut();
    	assertPage(loginPage);
	}
	
	@Test
	public void chooseIsolationWard() {
		String locationName = locationPage.getLocationName(1);
		locationPage.clickOnLocation(1);
		
		locationPage.loginWithoutChooseLocation();
		assertPage(homePage);
		
		// Checking the existence of the location name in the text "Logged as"
		assertTrue(homePage.getLoggedAsString().indexOf(locationName) != -1);
		
		homePage.go();
    	assertPage(homePage);
    	
    	headerPage.logOut();
    	assertPage(loginPage);
	}
	
	@Test
	public void chooseLaboratory() {
		String locationName = locationPage.getLocationName(2);
		locationPage.clickOnLocation(2);
		
		locationPage.loginWithoutChooseLocation();
		assertPage(homePage);
		
		// Checking the existence of the location name in the text "Logged as"
		assertTrue(homePage.getLoggedAsString().indexOf(locationName) != -1);
		
		homePage.go();
    	assertPage(homePage);
    	
    	headerPage.logOut();
    	assertPage(loginPage);
	}
	
	@Test
	public void chooseOutpatientClinic() {
		String locationName = locationPage.getLocationName(3);
		locationPage.clickOnLocation(3);
		
		locationPage.loginWithoutChooseLocation();
		assertPage(homePage);
		
		// Checking the existence of the location name in the text "Logged as"
		assertTrue(homePage.getLoggedAsString().indexOf(locationName) != -1);
		
		homePage.go();
    	assertPage(homePage);
    	
    	headerPage.logOut();
    	assertPage(loginPage);
	}
	
	@Test
	public void choosePharmacy() {
		String locationName = locationPage.getLocationName(4);
		locationPage.clickOnLocation(4);
		
		locationPage.loginWithoutChooseLocation();
		assertPage(homePage);
		
		// Checking the existence of the location name in the text "Logged as"
		assertTrue(homePage.getLoggedAsString().indexOf(locationName) != -1);
		
		homePage.go();
    	assertPage(homePage);
    	
    	headerPage.logOut();
    	assertPage(loginPage);
	}
	
	@Test
	public void chooseRegistrationDesk() {
		String locationName = locationPage.getLocationName(5);
		locationPage.clickOnLocation(5);
		
		locationPage.loginWithoutChooseLocation();
		assertPage(homePage);
		
		// Checking the existence of the location name in the text "Logged as"
		assertTrue(homePage.getLoggedAsString().indexOf(locationName) != -1);
		
		homePage.go();
    	assertPage(homePage);
    	
    	headerPage.logOut();
    	assertPage(loginPage);
	}
	
	@Test
	public void chooseUnknownLocation() {
		String locationName = locationPage.getLocationName(6);
		locationPage.clickOnLocation(6);
		
		locationPage.loginWithoutChooseLocation();
		assertPage(homePage);
		
		// Checking the existence of the location name in the text "Logged as"
		assertTrue(homePage.getLoggedAsString().indexOf(locationName) != -1);
		
		homePage.go();
    	assertPage(homePage);
    	
    	headerPage.logOut();
    	assertPage(loginPage);
	}
}
