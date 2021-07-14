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

public class ManageUserAccountPage extends Page {

    public static String URL_PATH = "/adminui/systemadmin/accounts/manageAccounts.page";
    private static final By SEARCH = By.cssSelector("#list-accounts_filter input[type=text]");
    private static final By ADD_NEW_ACCOUNT = By.cssSelector("#content a[href='/openmrs/adminui/systemadmin/accounts/account.page']");

    public ManageUserAccountPage(Page page) {
        super(page);
    }

    @Override
    public String getPageUrl() {
        return URL_PATH;
    }

    public AddNewAccountPage clickOnAddUser() {
        clickOn(ADD_NEW_ACCOUNT);
        return new AddNewAccountPage(this);
    }

    public void searchUser(String userName) {
        setTextToFieldNoEnter(SEARCH, userName);
    }
}
