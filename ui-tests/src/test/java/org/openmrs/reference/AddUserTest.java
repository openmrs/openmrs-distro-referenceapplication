/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.reference;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.reference.page.AddEditUserPage;
import org.openmrs.reference.page.AdministrationPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.ManageUserPage;
import org.openmrs.reference.page.SystemAdministrationPage;

import java.util.List;

public class AddUserTest extends ReferenceApplicationTestBase {

    @Ignore
    public void addUserTest() throws Exception {
        SystemAdministrationPage systemAdministrationPage = homePage.goToSystemAdministrationPage();
        AdministrationPage administrationPage = systemAdministrationPage.goToAdvancedAdministration();
        ManageUserPage manageUserPage = administrationPage.clickOnManageUsers();

        if (manageUserPage.userExists("super_nurse")) {
            manageUserPage.removeUser("super_nurse");
        }
        else {
            AddEditUserPage addEditUserPage = manageUserPage.clickOnAddUser();
            addEditUserPage.createNewPerson();

            addEditUserPage.saveUser();
            List<String> validationErrors = addEditUserPage.getValidationErrors();
            assertTrue(validationErrors.contains("You must define at least one name"));
            assertTrue(validationErrors.contains("Cannot be empty or null"));
            assertFalse(addEditUserPage.isDataCorrect(validationErrors));

            addEditUserPage.enterGivenFamily("Super", "Nurse");
            addEditUserPage.saveUser();
            validationErrors = addEditUserPage.getValidationErrors();
            assertFalse(addEditUserPage.isDataCorrect(validationErrors));

            addEditUserPage.clickOnFemale();
            addEditUserPage.enterUsernamePassword("super_nurse", "supernurse", "supernurse123");
            addEditUserPage.saveUser();
            assertFalse(addEditUserPage.isDataCorrect(validationErrors));
            addEditUserPage.enterUsernamePassword("super_nurse", "Nurse123", "Nurse123");
            addEditUserPage.saveUser();
            assertFalse(addEditUserPage.isDataCorrect(validationErrors));

            manageUserPage.waitForPage();
            assertTrue(manageUserPage.getUserSavedNotification().contains("User Saved"));

            homePage = new HomePage(goToLoginPage().login("super_nurse", "Nurse123"));
            homePage.getLoggedUsername().contains("super_nurse");
            goToLoginPage().loginAsAdmin();
            homePage.goToAdministration();
            administrationPage.clickOnManageUsers();
            manageUserPage.removeUser("super_nurse");
            manageUserPage.waitForPage();
            assertTrue(manageUserPage.getUserSavedNotification().contains("Successfully deleted user."));
        }
    }
}
