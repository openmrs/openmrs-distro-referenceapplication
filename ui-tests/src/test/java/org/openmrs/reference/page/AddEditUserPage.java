/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.reference.page;

import java.util.List;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

public class AddEditUserPage extends Page {

    public static final String URL = "/admin/users/user.form";
    private static final By PERSON_GIVEN_NAME = By.name("person.names[0].givenName");
    private static final By PERSON_FAMILY_NAME = By.name("person.names[0].familyName");
    private static final By PASSWORD = By.name("userFormPassword");
    private static final By CONFIRM = By.name("confirm");
    private static final By SAVE_BUTTON = By.id("saveButton");
    private static final By GENDER_FEMALE = By.id("F");
    private static final By USERNAME = By.name("username");
    private static final By CREATE_NEW_PERSON = By.id("createNewPersonButton");
    private static final By DELETE_USER = By.xpath("(//input[@name='action'])[3]");

    public AddEditUserPage(Page parent) {
        super(parent);
    }

    @Override
    public String getPageUrl() {
        return URL;
    }

    public CreateNewUserPage createNewPerson() {
        findElement(CREATE_NEW_PERSON).click();
        return new CreateNewUserPage(this);
    }

    public void unassignRole(String roleToUnassign) {
        waitForElement(By.id(roleToUnassign));
        WebElement roleElement;
        Long startTime = System.currentTimeMillis();
        while (true) {
            if ((System.currentTimeMillis() - startTime) > 30000) {
                throw new TimeoutException("Couldn't uncheck a role in 30 seconds");
            }
            roleElement = findElement(By.id(roleToUnassign));
            if (roleElement.getAttribute("checked") != null && roleElement.getAttribute("checked").equals("true")) {
                roleElement.click();
            } else {
                break;
            }
        }
    }

    public void deleteUser() {
        clickOn(DELETE_USER);
    }

    public void saveUser() {
        clickOn(SAVE_BUTTON);
    }

    public void enterGivenFamily(String givenName, String familyName) {
        findElement(PERSON_GIVEN_NAME).clear();
        findElement(PERSON_GIVEN_NAME).sendKeys(givenName);
        findElement(PERSON_FAMILY_NAME).clear();
        findElement(PERSON_FAMILY_NAME).sendKeys(familyName);
    }

    public void clickOnFemale() {
        clickOn(GENDER_FEMALE);
    }

    public boolean isDataCorrect(List<String> validationErrors) {
        return !validationErrors.contains("Please fix all errors and try again.");
    }

    public void enterUsernamePassword(String username, String password, String confirm) {
        findElement(USERNAME).clear();
        findElement(USERNAME).sendKeys(username);
        findElement(PASSWORD).clear();
        findElement(PASSWORD).sendKeys(password);
        findElement(CONFIRM).clear();
        findElement(CONFIRM).sendKeys(confirm);
    }
}
