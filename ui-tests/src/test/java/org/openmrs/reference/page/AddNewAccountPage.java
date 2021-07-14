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

public class AddNewAccountPage extends Page {

    public static final String URL = "/adminui/systemadmin/accounts/account.page";
    private static final By FAMILY_NAME = By.cssSelector("#adminui-familyName-field");
    private static final By GIVEN_NAME = By.cssSelector("#adminui-givenName-field");
    private static final By GENDER_FEMALE = By.cssSelector("#adminui-gender-1-field");
    private static final By ADD_USER_ACCOUNT = By.id("adminui-addUserAccount");
    private static final By USER_NAME = By.cssSelector("#adminui-username-field");
    private static final By PRIVILEGE_LEVEL = By.cssSelector("#adminui-privilegeLevel-field");
    private static final By PASSWORD = By.cssSelector("#adminui-password-field");
    private static final By CONFIRM_PASSWORD = By.cssSelector("#adminui-confirmPassword-field");
    private static final By ADD_PROVIDER_ACCOUNT = By.id("adminui-addProviderAccount");
    private static final By IDENTIFIER = By.id("adminui-identifier-field");
    private static final By PROVIDER_ROLE = By.id("adminui-providerRole-field");
    private static final By SAVE_BUTTON = By.id("save-button");

    public AddNewAccountPage(Page parent) {
        super(parent);
    }

    @Override
    public String getPageUrl() {
        return URL;
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

    public ManageUserAccountPage saveUserAccount() {
        clickOn(SAVE_BUTTON);
        return new ManageUserAccountPage(this);
    }

    public void enterGivenFamily(String familyName, String givenName) {
        findElement(FAMILY_NAME).clear();
        findElement(FAMILY_NAME).sendKeys(familyName);
        findElement(GIVEN_NAME).clear();
        findElement(GIVEN_NAME).sendKeys(givenName);
    }

    public void clickOnFemale() {
        clickOn(GENDER_FEMALE);
    }

    public boolean isDataCorrect(List<String> validationErrors) {
        return !validationErrors.contains("Please fix all errors and try again.");
    }

    public void addUserAccount(String userName, String password, String confirm) {
        findElement(USER_NAME).clear();
        findElement(USER_NAME).sendKeys(userName);
        selectPrivilegeLevel(); 
        findElement(PASSWORD).clear();
        findElement(PASSWORD).sendKeys(password);
        findElement(CONFIRM_PASSWORD).clear();
        findElement(CONFIRM_PASSWORD).sendKeys(confirm);
    }
    
    public void selectPrivilegeLevel() {
        clickOn(PRIVILEGE_LEVEL);
    }
    
    public void setUserIdentifier(String identifier) {
        findElement(IDENTIFIER).clear();
        setText(IDENTIFIER, identifier);
    }
}
