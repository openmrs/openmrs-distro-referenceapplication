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

public class ProviderPage extends Page{

    private static final By IDENTIFIER_FIELD = By.cssSelector("#content input[name=\"identifier\"]");
    private static final By PERSON_FIELD = By.id("person_id_selection");
    private static final By SAVE = By.cssSelector("#content input[type=\"submit\"]");
    private static final By CANCEL = By.cssSelector("#content input[type=\"button\"]:nth-child(5)");
    private static final By DELETE_FOREVER = By.cssSelector("#content input[name='purgeProviderButton']");
    private static final By FIRST_AUTOCOMPLETE_RESULT = By.cssSelector("body > ul");
    private static final By RETIRE_REASON = By.id("retire");
    private static final By RETIRE = By.cssSelector("input[name='retireProviderButton']");

    public ProviderPage(Page parent) {
        super(parent);
    }

    public void setIdentifier(String identifier){
        setTextToFieldNoEnter(IDENTIFIER_FIELD, identifier);
    }

    public void setPerson(String person){
        findElement(PERSON_FIELD).sendKeys(person);
        waitForElement(FIRST_AUTOCOMPLETE_RESULT);
        clickOn(FIRST_AUTOCOMPLETE_RESULT);
    }

    public ManageProviderPage clickOnSave(){
        clickOn(SAVE);
        return new ManageProviderPage(this);
    }

    public ManageProviderPage clickOnCancel(){
        clickOn(CANCEL);
        return new ManageProviderPage(this);
    }

    @Override
    public String getPageUrl() {
        return "/admin/provider/provider.form";
    }

    public ManageProviderPage deleteForever() {
        clickOn(DELETE_FOREVER);
        acceptAlert();
        return new ManageProviderPage(this);
    }

    public void setRetireReason(String retireReason) {
        setTextToFieldNoEnter(RETIRE_REASON, retireReason);
    }

    public ManageProviderPage clickOnRetire() {
        clickOn(RETIRE);
        return new ManageProviderPage(this);
    }
}
