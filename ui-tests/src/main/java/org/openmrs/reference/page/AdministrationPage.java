/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AdministrationPage extends Page {

    public static String URL_PATH = "/admin/index.htm";
    private static By MANAGE_USERS = By.cssSelector("#content  table:nth-child(4)  div:nth-child(1) li:nth-child(2)  a");
    private final static By MANAGE_VISIT_TYPES = By.cssSelector("#content  table:nth-child(4)  div:nth-child(4) li:nth-child(2)  a");
    private static By MANAGE_PROVIDERS = By.cssSelector("#content a[href='/openmrs/admin/provider/index.htm']");

    public AdministrationPage(WebDriver driver) {
        super(driver);
    }

    public AdministrationPage(Page parent){
        super(parent);
    }


    @Override
    public String getPageUrl() {
        return URL_PATH;
    }

    public ManageUserPage clickOnManageUsers() {
        findElement(MANAGE_USERS).click();
        return new ManageUserPage(driver);
    }

    public ManageProviderPage clickOnManageProviders() {
        clickOn(MANAGE_PROVIDERS);
        return new ManageProviderPage(this);
    }

    public VisitTypeListPage goToVisitTypePage() {
        findElement(MANAGE_VISIT_TYPES).click();
        return new VisitTypeListPage(this);
    }
}
