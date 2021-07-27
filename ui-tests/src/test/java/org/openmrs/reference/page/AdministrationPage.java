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

public class AdministrationPage extends Page {

    private static final String URL = "/admin/index.htm";
    private static final By MANAGE_USERS = By.cssSelector("#content a[href='/openmrs/admin/users/users.list']");
    private static final By MANAGE_MODULES = By.cssSelector("#legacyui-manageModules a");
    private final static By MANAGE_VISIT_TYPES = By.cssSelector("#legacyui-manageVisitTypes a");
    private static final By MANAGE_PROVIDERS = By.cssSelector("#content a[href='/openmrs/admin/provider/index.htm']");
    private static final By MANAGE_PERSONS = By.cssSelector("#content a[href=\"/openmrs/admin/person/index.htm\"]");
    private static final By MANAGE_ROLES = By.cssSelector("#legacyui-manageRoles a");
    
    public AdministrationPage(Page page) {
        super(page);
    }

    @Override
    public String getPageUrl() {
        return URL;
    }

    public ManageUserPage clickOnManageUsers() {
        findElement(MANAGE_USERS).click();
        return new ManageUserPage(this);
    }

    public ManageProviderPage clickOnManageProviders() {
        clickOn(MANAGE_PROVIDERS);
        return new ManageProviderPage(this);
    }

    public VisitTypeListPage goToVisitTypePage() {
        findElement(MANAGE_VISIT_TYPES).click();
        return new VisitTypeListPage(this);
    }

    public ModulesPage goToManageModulesPage() {
        findElement(MANAGE_MODULES).click();
        return new ModulesPage(this);
    }
    
    public ManageRolesPage goToManageRolesPage() {
    	findElement(MANAGE_ROLES).click();
    	return new ManageRolesPage(this);
    }

    public ManagePersonPage clickOnManagePersons() {
        findElement(MANAGE_PERSONS).click();
        return new ManagePersonPage(this);
    }
}
