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

import java.util.Random;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.LocationPage;
import org.openmrs.reference.page.UserOptionsPage;
import org.openmrs.uitestframework.test.TestBase;
import org.openqa.selenium.JavascriptExecutor;

public class ChangeLoginInfoTest extends TestBase {
	private HeaderPage headerPage;
	private HomePage homePage;
	private LocationPage locationPage;
	private UserOptionsPage userOptionsPage;
	private Random rand;
	
	private static final String ADMIN_PASSWORD = "Admin123";
	private static final String NEW_ADMIN_PASSWORD = "123Admin_1";
	
	private static final String[] USER_NAMES = { "Alexandre", "Achint", "Brittany", "Burke", "Cesar", "Cosmin",
        "Daniel", "Darius", "David", "Ellen", "Émerson", "Evan", "Fernando", "Gabou", "Glauber", "Hamish", "Louise",
        "Mário", "Mark", "Natália", "Neil", "Neissi", "Nelice", "Rafal", "Renee", "Wyclif" };
	
	@Before
	public void before() {
		headerPage = new HeaderPage(driver);
        homePage = new HomePage(driver);
        locationPage = new LocationPage(driver);
        userOptionsPage = new UserOptionsPage(driver);
        rand = new Random();
	}
	
	@Test
	public void changeUsername() {
		login();
		assertPage(homePage);
		
		// Go to User Options page
		currentPage().gotoPage(UserOptionsPage.URL_PAGE);
		assertPage(userOptionsPage);
		
		int trIndex = 0;
		
		// Choose name
		int index = rand.nextInt(USER_NAMES.length);
		String newName = USER_NAMES[index];
		
		// Change username
		userOptionsPage.pressChangeLoginInfo();
		String lastOption = userOptionsPage.getUserOption(trIndex);
		userOptionsPage.pasteNewUserOption(trIndex, newName);
		userOptionsPage.saveOptions();
		
		// Go to home page
		((JavascriptExecutor) driver).executeScript("location.href='/openmrs';");
		
		// Check "Logged as" string with new option
		String loggedAs = homePage.getLoggedAsString();
		assertTrue(loggedAs.contains(newName));
		
		// Go to User Options page
		currentPage().gotoPage(UserOptionsPage.URL_PAGE);
		assertPage(userOptionsPage);
		
		// Backup username
		userOptionsPage.pressChangeLoginInfo();
		userOptionsPage.pasteNewUserOption(trIndex, lastOption);
		userOptionsPage.saveOptions();
		
		// Go to home page
		((JavascriptExecutor) driver).executeScript("location.href='/openmrs';");
		
		// Check "Logged as" string with old option
		loggedAs = homePage.getLoggedAsString();
		assertTrue(loggedAs.contains(lastOption));
		
		// Logout
		homePage.go();
    	assertPage(homePage);
		headerPage.logOut();
        assertPage(loginPage);
	}
	
	@Test
	public void changeGivenName() {
		login();
		assertPage(homePage);
		
		// Go to User Options page
		currentPage().gotoPage(UserOptionsPage.URL_PAGE);
		assertPage(userOptionsPage);
		
		int trIndex = 1;
		
		// Choose name
		int index = rand.nextInt(USER_NAMES.length);
		String newName = USER_NAMES[index];
		
		// Change given name
		userOptionsPage.pressChangeLoginInfo();
		String lastOption = userOptionsPage.getUserOption(trIndex);
		userOptionsPage.pasteNewUserOption(trIndex, newName);
		userOptionsPage.saveOptions();
		
		// Go to home page
		((JavascriptExecutor) driver).executeScript("location.href='/openmrs';");
		
		// Check "Logged as" string with new option
		String loggedAs = homePage.getLoggedAsString();
		assertTrue(loggedAs.contains(newName));
		
		// Go to User Options page
		currentPage().gotoPage(UserOptionsPage.URL_PAGE);
		assertPage(userOptionsPage);
		
		// Backup given name
		userOptionsPage.pressChangeLoginInfo();
		userOptionsPage.pasteNewUserOption(trIndex, lastOption);
		userOptionsPage.saveOptions();
		
		// Go to home page
		((JavascriptExecutor) driver).executeScript("location.href='/openmrs';");
		
		// Check "Logged as" string with old option
		loggedAs = homePage.getLoggedAsString();
		assertTrue(loggedAs.contains(lastOption));
		
		// Logout
		homePage.go();
    	assertPage(homePage);
		headerPage.logOut();
        assertPage(loginPage);
	}
	
	@Test
	public void changeMiddleName() {
		login();
		assertPage(homePage);
		
		// Go to User Options page
		currentPage().gotoPage(UserOptionsPage.URL_PAGE);
		assertPage(userOptionsPage);
		
		int trIndex = 2;
		
		// Choose name
		int index = rand.nextInt(USER_NAMES.length);
		String newName = USER_NAMES[index];
		
		// Change middle name
		userOptionsPage.pressChangeLoginInfo();
		String lastOption = userOptionsPage.getUserOption(trIndex);
		userOptionsPage.pasteNewUserOption(trIndex, newName);
		userOptionsPage.saveOptions();
		
		// Go to home page
		((JavascriptExecutor) driver).executeScript("location.href='/openmrs';");
		
		// Check "Logged as" string with new option
		String loggedAs = homePage.getLoggedAsString();
		assertTrue(loggedAs.contains(newName));
		
		// Go to User Options page
		currentPage().gotoPage(UserOptionsPage.URL_PAGE);
		assertPage(userOptionsPage);
		
		// Backup middle name
		userOptionsPage.pressChangeLoginInfo();
		userOptionsPage.pasteNewUserOption(trIndex, lastOption);
		userOptionsPage.saveOptions();
		
		// Go to home page
		((JavascriptExecutor) driver).executeScript("location.href='/openmrs';");
		
		// Check "Logged as" string with old option
		loggedAs = homePage.getLoggedAsString();
		assertTrue(loggedAs.contains(lastOption));
		
		// Logout
		homePage.go();
    	assertPage(homePage);
		headerPage.logOut();
        assertPage(loginPage);
	}
	
	@Test
	public void changeFamilyName() {
		login();
		assertPage(homePage);
		
		// Go to User Options page
		currentPage().gotoPage(UserOptionsPage.URL_PAGE);
		assertPage(userOptionsPage);
		
		int trIndex = 3;
		
		// Choose name
		int index = rand.nextInt(USER_NAMES.length);
		String newName = USER_NAMES[index];
		
		// Change middle name
		userOptionsPage.pressChangeLoginInfo();
		String lastOption = userOptionsPage.getUserOption(trIndex);
		userOptionsPage.pasteNewUserOption(trIndex, newName);
		userOptionsPage.saveOptions();
		
		// Go to home page
		((JavascriptExecutor) driver).executeScript("location.href='/openmrs';");
		
		// Check "Logged as" string with new option
		String loggedAs = homePage.getLoggedAsString();
		assertTrue(loggedAs.contains(newName));
		
		// Go to User Options page
		currentPage().gotoPage(UserOptionsPage.URL_PAGE);
		assertPage(userOptionsPage);
		
		// Backup middle name
		userOptionsPage.pressChangeLoginInfo();
		userOptionsPage.pasteNewUserOption(trIndex, lastOption);
		userOptionsPage.saveOptions();
		
		// Go to home page
		((JavascriptExecutor) driver).executeScript("location.href='/openmrs';");
		
		// Check "Logged as" string with old option
		loggedAs = homePage.getLoggedAsString();
		assertTrue(loggedAs.contains(lastOption));
		
		// Logout
		homePage.go();
    	assertPage(homePage);
		headerPage.logOut();
        assertPage(loginPage);
	}
	
	@Test
	public void changePassword() {
		login();
		assertPage(homePage);
		
		// Go to User Options page
		currentPage().gotoPage(UserOptionsPage.URL_PAGE);
		assertPage(userOptionsPage);
		
		// Change password
		userOptionsPage.pressChangeLoginInfo();
		userOptionsPage.pasteNewUserOption(4, ADMIN_PASSWORD);
		userOptionsPage.pasteNewUserOption(5, NEW_ADMIN_PASSWORD);
		userOptionsPage.pasteNewUserOption(6, NEW_ADMIN_PASSWORD);
		userOptionsPage.saveOptions();
		
		// Go to home page
		((JavascriptExecutor) driver).executeScript("location.href='/openmrs';");
		
		// Logout
		homePage.go();
		assertPage(homePage);
		headerPage.logOut();
		assertPage(loginPage);
		
		// Login with new password
		locationPage.loginWithNewPassword(NEW_ADMIN_PASSWORD);
		assertPage(homePage);
		
		// Go to User Options page
		currentPage().gotoPage(UserOptionsPage.URL_PAGE);
		assertPage(userOptionsPage);
		
		// Backup password
		userOptionsPage.pressChangeLoginInfo();
		userOptionsPage.pasteNewUserOption(4, NEW_ADMIN_PASSWORD);
		userOptionsPage.pasteNewUserOption(5, ADMIN_PASSWORD);
		userOptionsPage.pasteNewUserOption(6, ADMIN_PASSWORD);
		userOptionsPage.saveOptions();
		
		// Logout
		homePage.go();
    	assertPage(homePage);
		headerPage.logOut();
        assertPage(loginPage);
        
        // Login with old password
        login();
		assertPage(homePage);
		
		// Logout
		homePage.go();
    	assertPage(homePage);
		headerPage.logOut();
        assertPage(loginPage);
	}
	
	@Ignore
	@Test
	public void changeSecretQuestion() throws InterruptedException {
		login();
		assertPage(homePage);
		
		// Go to User Options page
		currentPage().gotoPage(UserOptionsPage.URL_PAGE);
		assertPage(userOptionsPage);
		
		// Change secret question
		userOptionsPage.pressChangeLoginInfo();
		userOptionsPage.pasteNewUserOption(7, ADMIN_PASSWORD);
		userOptionsPage.pasteNewUserOption(8, "How do you do?");
		userOptionsPage.pasteNewUserOption(9, "all_right");
		userOptionsPage.pasteNewUserOption(10, "all_right");
		userOptionsPage.saveOptions();
		
		// Go to home page
		((JavascriptExecutor) driver).executeScript("location.href='/openmrs';");
		
		// Logout
		homePage.go();
		assertPage(homePage);
		headerPage.logOut();
		assertPage(loginPage);
		
		// Press "Can't log in?"
		homePage.pressCantLogin();
		
		// We need to check our secret question, but it can not cause
		// TODO: resolve the issue with this test: remove test or realize secret questions
		Thread.sleep(3000);
		
		// Logout
		homePage.go();
    	assertPage(homePage);
		headerPage.logOut();
        assertPage(loginPage);
	}
}
