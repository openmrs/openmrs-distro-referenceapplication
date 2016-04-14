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

import java.util.HashMap;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.page.AdministrationPage;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.ManageUserPage;
import org.openmrs.uitestframework.test.TestBase;

/**
 * Created by tomasz on 10.07.15.
 */
public class AddRolesToUserTest extends TestBase {

    private static final String NURSE_PASSWORD = "Nurse321";
	private static final String NURSE_USERNAME = "newNurse" + new Random().nextInt(1024);
	private HomePage homePage;
    private HeaderPage headerPage;
    private AdministrationPage administrationPage;
    private ManageUserPage manageUserPage;

    @Before
    public void setUp() throws Exception {
        homePage = new HomePage(page);
        assertPage(homePage);
        headerPage = new HeaderPage(driver);
        administrationPage = new AdministrationPage(driver);
        manageUserPage = new ManageUserPage(driver);
    }


    private void reLoginAsUser() throws InterruptedException {
        goToLoginPage().login(NURSE_USERNAME, NURSE_PASSWORD);
    }

    private void reLoginAsAdmin() throws InterruptedException {
        goToLoginPage().loginAsAdmin();
    }

    @Test
    @Category(org.openmrs.reference.groups.BuildTests.class)
    public void addRolesToUserTest() throws InterruptedException {
        homePage.goToAdministration();
        administrationPage.clickOnManageUsers();

        Map<String,Integer> roleModules = new HashMap();

        fillInRoleModules(roleModules);
        if(!manageUserPage.userExists(NURSE_USERNAME)) {
            manageUserPage.clickOnAddUser();
            manageUserPage.createNewPerson();
            manageUserPage.fillInPersonName("Super", "Nurse", NURSE_USERNAME, NURSE_PASSWORD);
        }

        String oldRole = null;
        for(Entry<String, Integer> role : roleModules.entrySet()) {
            manageUserPage.assignRolesToUser(oldRole,role.getKey(),NURSE_USERNAME);
            reLoginAsUser();
            if(homePage.numberOfAppsPresent() != role.getValue()) {
                throw new AssertionError("role "+role+" doesn't have matching number of accessible applications: should be:"+role.getValue()+"is:"+homePage.numberOfAppsPresent());
            }
            reLoginAsAdmin();
            oldRole = role.getKey();
            homePage.goToAdministration();
            administrationPage.clickOnManageUsers();
        }

    }

    private void fillInRoleModules(Map<String, Integer> roleModules) {
        roleModules.put("roleStrings.Anonymous",0);
        roleModules.put("roleStrings.Authenticated",0);
        roleModules.put("roleStrings.Organizational:SystemAdministrator",2);
        roleModules.put("roleStrings.SystemDeveloper",10);
        roleModules.put("roleStrings.Application:AdministersSystem",1);
        roleModules.put("roleStrings.Organizational:Doctor", 3);
        roleModules.put("roleStrings.Organizational:Nurse", 4);
        roleModules.put("roleStrings.Organizational:RegistrationClerk", 4);
    }

    @After
    public void tearDown() throws Exception {
        manageUserPage.removeUser(NURSE_USERNAME);
        manageUserPage.clickOnHomeLink();
        headerPage.logOut();
    }
}
