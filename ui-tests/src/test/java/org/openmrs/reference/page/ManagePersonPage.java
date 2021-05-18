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
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ManagePersonPage extends Page {

    private static final By CREATE_PERSON = By.cssSelector("#content a:nth-child(9)");
    private static final By PERSON_NAME_FIELD = By.id("inputNode");
    private static final By OPENMRS_MSG = By.id("openmrs_msg");
    private static final By FIRST_FOUND_PERSON = By.cssSelector("#openmrsSearchTable tbody tr td:nth-child(1)");

    public ManagePersonPage(Page parent) {
        super(parent);
    }

    public AddPersonPage createPerson() {
        clickOn(CREATE_PERSON);
        return new AddPersonPage(this);
    }

    public void setPersonName(String personName) {
        findElement(PERSON_NAME_FIELD).sendKeys(personName);
    }

    public PersonFormPage clickFirstFoundPerson() {
        clickOn(FIRST_FOUND_PERSON);
        return new PersonFormPage(this);
    }

    public void waitForPersonToBeDeleted() {
        waitForTextToBePresentInElement(OPENMRS_MSG, "Person deleted forever");
    }

    @Override
    public String getPageUrl() {
        return "/admin/person/index.htm";
    }
}
