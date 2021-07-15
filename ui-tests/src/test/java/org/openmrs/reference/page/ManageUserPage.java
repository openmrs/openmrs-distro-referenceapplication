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

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

public class ManageUserPage extends Page {

    private static final By ADD_USER = By.linkText("Add User");
    private static final By SAVE_BUTTON = By.id("saveButton");
    private static final By HOME = By.id("homeNavLink");
    private static final By ACTION = By.name("action");
    private static final By USER_LINK = By.xpath("//table[@class='openmrsSearchTable']/tbody/tr/td/a");
    private static final By FIND_USER = By.name("name");
    private static final By USER_SAVED_NOTIFICATION = By.id("openmrs_msg");
    public static String URL_PATH = "/admin/users/users.list";
    private static final By ADMIN = By.cssSelector("#content > div.box > table > tbody > tr > td:nth-child(1) > a");

    public ManageUserPage(Page page) {
        super(page);
    }

    @Override
    public String getPageUrl() {
        return URL_PATH;
    }

    public AddEditUserPage clickOnAddUser() {
        clickOn(ADD_USER);
        return new AddEditUserPage(this);
    }

    public boolean userExists(String username) {
        clickOn(ACTION);
        return findElement(By.cssSelector(".openmrsSearchTable")).getText().contains(username);
    }

    public void assignRolesToUser(String roleToUnassign, String roleToAssign, String user) throws InterruptedException {
        setText(FIND_USER, user);
        clickOn(ACTION);
        clickOn(USER_LINK);
        AddEditUserPage editPage = new AddEditUserPage(this);
        if (roleToUnassign != null) {
            editPage.unassignRole(roleToUnassign);
        }
        editPage.clickOn(By.id(roleToAssign));
        editPage.clickOn(SAVE_BUTTON);
    }
    
    public void findUser(String findUser) {
    	  setText(FIND_USER, findUser);
    	  clickOn(ACTION);
    }
    
    public void clickOnAdminAfterSearch() {
    	clickOn(ADMIN);
    }
    
    public void removeUser(String user) {
        setText(FIND_USER, user);
        clickOn(ACTION);
        clickOn(USER_LINK);
        new AddEditUserPage(this).deleteUser();
    }

    public String getUserSavedNotification() {
        return findElement(USER_SAVED_NOTIFICATION).getText();
    }

    public void clickOnHomeLink() {
        clickOn(HOME);
    }
}
