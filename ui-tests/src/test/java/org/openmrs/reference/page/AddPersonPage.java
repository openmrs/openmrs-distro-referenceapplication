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

public class AddPersonPage extends Page {

    private static final By PERSON_NAME = By.id("personName");
    private static final By BIRTHDATE = By.id("birthdate");
    private static final By AGE = By.id("age");
    private static final By GENDER_MALE = By.id("gender-M");
    private static final By CREATE_PERSON = By.cssSelector("#createPatient input[type=\"submit\"]");

    public AddPersonPage(Page parent) {
        super(parent);
    }

    public PersonFormPage createPerson() {
        clickOn(CREATE_PERSON);
        return new PersonFormPage(this);
    }

    public void setPersonName(String personName) {
        setTextField(personName, PERSON_NAME);
    }

    public void setBirthdate(String birthdate) {
        setTextField(birthdate, BIRTHDATE);
    }

    public void setAge(String age) {
        setTextField(age, AGE);
    }

    public void clickGenderMale() {
        clickOn(GENDER_MALE);
    }

    @Override
    public String getPageUrl() {
        return "/admin/person/addPerson.htm";
    }

    private void setTextField(String text, By by) {
        findElement(by).clear();
        findElement(by).sendKeys(text);
    }
}
