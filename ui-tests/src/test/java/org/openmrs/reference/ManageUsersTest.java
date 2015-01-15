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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.ManageUsersPage;
import org.openmrs.uitestframework.test.TestBase;

public class ManageUsersTest extends TestBase {

	private HeaderPage headerPage;
	private HomePage homePage;
	private ManageUsersPage manageUsersPage;
	private Random rand;
	
	private static final String[] PERSON_NAMES = { "Alexandre", "Achint", "Brittany", "Burke", "Cesar", "Cosmin",
        "Daniel", "Darius", "David", "Ellen", "Émerson", "Evan", "Fernando", "Gabou", "Glauber", "Hamish", "Louise",
        "Mário", "Mark", "Natália", "Neil", "Neissi", "Nelice", "Rafal", "Renee", "Wyclif" };
	private static final String USER_NAME = "test";
	private static final String USER_PASSWORD = "123Qwerty";
	
	private int givenName;
	private int familyName;
	
	@Before
	public void before() {
		headerPage = new HeaderPage(driver);
        homePage = new HomePage(driver);
        manageUsersPage = new ManageUsersPage(driver);
        rand = new Random();
        
        // Login
        login();
        assertPage(homePage);
        
        // Go to user manage page
        currentPage().gotoPage("/admin/users/users.list");
        assertPage(manageUsersPage);
        
        // Go to create user page
        currentPage().gotoPage("/admin/users/user.form?createNewPerson=true");
        givenName = rand.nextInt(PERSON_NAMES.length);
        familyName = rand.nextInt(PERSON_NAMES.length);
        manageUsersPage.enterNewUserGivenName(PERSON_NAMES[givenName]);
        manageUsersPage.enterNewUserFamilyName(PERSON_NAMES[familyName]);
        manageUsersPage.setNewUserMaleGender();
        manageUsersPage.enterNewUserUsername(USER_NAME);
        manageUsersPage.enterNewPasswordWithConfirm(USER_PASSWORD);
	}
	
	@After
	public void after() {
		// Logout and login as admin
        headerPage.logOut();
		assertPage(loginPage);
		login();
		
		// Go to new user page and delete user
		currentPage().gotoPage("/admin/users/users.list");
        assertPage(manageUsersPage);
        manageUsersPage.findPageSearchAndChooseUser(USER_NAME);
        manageUsersPage.clickDeleteUser();
        
        // Logout
        homePage.go();
		assertPage(homePage);
		
		headerPage.logOut();
		assertPage(loginPage);
	}
	
	@Test
	public void clerkRoleTest() {
		chooseUserRoleAndBecome("roleStrings.Organizational:RegistrationClerk");

		assertTrue(isCorrectLoggedAsString(givenName, familyName));
		
		assertTrue(homePage.isActiveVisitsAppPresent());
    	assertTrue(homePage.isRegisterPatientCustomizedForRefAppPresent());
    	assertThat(homePage.numberOfAppsPresent(), is(2));
	}
	
	@Test
	public void doctorRoleTest() {
		chooseUserRoleAndBecome("roleStrings.Organizational:Doctor");

		assertTrue(isCorrectLoggedAsString(givenName, familyName));
		
		assertTrue(homePage.isFindAPatientAppPresent());
        assertTrue(homePage.isActiveVisitsAppPresent());
        assertThat(homePage.numberOfAppsPresent(), is(2));
	}
	
	@Test
	public void nurseRoleTest() {
		chooseUserRoleAndBecome("roleStrings.Organizational:Nurse");

		assertTrue(isCorrectLoggedAsString(givenName, familyName));
		
		assertTrue(homePage.isFindAPatientAppPresent());
    	assertTrue(homePage.isActiveVisitsAppPresent());
    	assertTrue(homePage.isCaptureVitalsAppPresent());
    	assertThat(homePage.numberOfAppsPresent(), is(3));
	}
	
	@Test
	public void sysadminRoleTest() {
		chooseUserRoleAndBecome("roleStrings.Organizational:SystemAdministrator");
		
		assertTrue(isCorrectLoggedAsString(givenName, familyName));
		
		assertTrue(homePage.isConfigureMetadataAppPresent());
        assertTrue(homePage.isSystemAdministrationAppPresent());
        assertThat(homePage.numberOfAppsPresent(), is(2));
	}
	
	@Test
	public void renameUserTest() {
		// Check old name
		chooseUserRoleAndBecome("roleStrings.Organizational:SystemAdministrator");
		assertTrue(isCorrectLoggedAsString(givenName, familyName));
		
		// Logout and login as admin
        headerPage.logOut();
		assertPage(loginPage);
		login();
		
		// Go to new user page and rename user and check
		currentPage().gotoPage("/admin/users/users.list");
        assertPage(manageUsersPage);
        manageUsersPage.findPageSearchAndChooseUser(USER_NAME);

		givenName = rand.nextInt(PERSON_NAMES.length);
        familyName = rand.nextInt(PERSON_NAMES.length);
        manageUsersPage.enterNewUserGivenName(PERSON_NAMES[givenName]);
        manageUsersPage.enterNewUserFamilyName(PERSON_NAMES[familyName]);
        
        saveAndBecome();
		assertTrue(isCorrectLoggedAsString(givenName, familyName));
	}
	
	private void chooseUserRoleAndBecome(String role) {
		// Choose role
		manageUsersPage.chooseNewUserRole(role);
		saveAndBecome();
	}
	
	private void saveAndBecome() {
		// Save user
		manageUsersPage.saveUser();
						
		// Check user role
		manageUsersPage.findPageSearchAndChooseUser(USER_NAME);
		manageUsersPage.clickBecomeUser();
	}
	
	private boolean isCorrectLoggedAsString(int givenName, int familyName) {
		String loggedAs = homePage.getLoggedAsString();
		
		if(!loggedAs.contains(PERSON_NAMES[givenName]))
			return false;
		
		if(!loggedAs.contains(PERSON_NAMES[familyName]))
			return false;
		
		if(!loggedAs.contains(USER_NAME))
			return false;
		
		return true;
	}
}
