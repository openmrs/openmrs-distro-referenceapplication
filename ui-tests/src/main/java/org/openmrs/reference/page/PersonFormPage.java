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

public class PersonFormPage extends Page{

    private static final By FAMILY_NAME_FIELD = By.cssSelector("#namePortlet > div > table > tbody > tr:nth-child(4) > td:nth-child(2) > input[type=\"text\"]");
    private static final By SAVE_PERSON = By.id("saveButton");
    private static final By OPENMRS_MSG = By.id("openmrs_msg");
    private static final By DELETE_PERSON_FOREVER = By.id("deletePersonButton");
    private static final By DELETE_PERSON_CONFIRM = By.cssSelector("#deletePersonDiv > div > input[type=\"submit\"]:nth-child(1)");
    private static final By RETIRE_REASON_FIELD = By.cssSelector("#content input[name=\"voidReason\"]");
    private static final By RETIRE_PERSON = By.cssSelector("#content > form:nth-child(10) > fieldset:nth-child(1) > input:nth-child(6)");

    public PersonFormPage(Page parent) {
        super(parent);
    }

    public PersonFormPage savePerson(){
        clickOn(SAVE_PERSON);
        return new PersonFormPage(this);
    }

    public ManagePersonPage deletePersonForever(){
        clickOn(DELETE_PERSON_FOREVER);
        clickOn(DELETE_PERSON_CONFIRM);
        acceptAlert();
        return new ManagePersonPage(this);
    }

    public void setFamilyNameField(String familyName){
        findElement(FAMILY_NAME_FIELD).clear();
        findElement(FAMILY_NAME_FIELD).sendKeys(familyName);
    }

    public String getActionMessage(){
        return findElement(OPENMRS_MSG).getText();
    }

    public String getFamilyName(){
        return findElement(FAMILY_NAME_FIELD).getAttribute("value");
    }

    @Override
    public String getPageUrl() {
        return "/admin/person/person.form";
    }

    public void setRetireReason(String reason) {
        findElement(RETIRE_REASON_FIELD).clear();
        findElement(RETIRE_REASON_FIELD).sendKeys(reason);
    }

    public PersonFormPage retirePerson() {
        clickOn(RETIRE_PERSON);
        return new PersonFormPage(this);
    }
}
