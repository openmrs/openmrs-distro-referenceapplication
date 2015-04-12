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
import org.openmrs.uitestframework.test.TestBase;


public class SwitchLocationTest extends TestBase {

    private HomePage homePage;
	
	@Before
	public void before() {
        homePage = new HomePage(driver);
    	assertPage(loginPage);
        loginPage.loginAsAdmin();   
	}
	
	@After
	public void after() {
		currentPage().gotoPage("/logout");
		assertPage(loginPage);
	}


	@Test
	public void testSwitchInpatient() {
		homePage.switchToInpatient();
		currentPage().gotoPage("/referenceapplication/home.page");
		Assert.assertTrue(homePage.getLocationText().contains("Inpatient Ward"));
	}
	
	@Test
	public void testSwitchIsolation() {
		homePage.switchToIsolation();
		currentPage().gotoPage("/referenceapplication/home.page");
		Assert.assertTrue(homePage.getLocationText().contains("Isolation Ward"));
	}
	
	@Test
	public void testSwitchLab() {
		homePage.switchToLab();
		currentPage().gotoPage("/referenceapplication/home.page");
		Assert.assertTrue(homePage.getLocationText().contains("Laboratory"));
	}
	
	@Test
	public void testSwitchOutpatient() {
		homePage.switchToOutpatient();
		currentPage().gotoPage("/referenceapplication/home.page");
		Assert.assertTrue(homePage.getLocationText().contains("Outpatient Clinic"));
	}
	
	@Test
	public void testSwitchPharmacy() {
		homePage.switchToPharmacy();
		currentPage().gotoPage("/referenceapplication/home.page");
		Assert.assertTrue(homePage.getLocationText().contains("Pharmacy"));
	}
	
	@Test
	public void testSwitchRegistration() {
		homePage.switchToRegistration();
		currentPage().gotoPage("/referenceapplication/home.page");
		Assert.assertTrue(homePage.getLocationText().contains("Registration Desk"));
	}
	
	@Test
	public void testSwitchUnknown() {
		homePage.switchToUnknown();
		currentPage().gotoPage("/referenceapplication/home.page");
		Assert.assertTrue(homePage.getLocationText().contains("Unknown Location"));
	}
}
