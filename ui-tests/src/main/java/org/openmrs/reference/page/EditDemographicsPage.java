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

public class EditDemographicsPage extends Page {

    static final By GIVEN_NAME = By.name("givenName");
    static final By MIDDLE_NAME = By.name("middleName");
    static final By FAMILY_NAME = By.name("familyName");
    static final By GENDER_SELECT = By.name("gender");

    static final By BIRTHDATE_DAY = By.name("birthdateDay");
    static final By BIRTHDATE_MONTH = By.name("birthdateMonth");
    static final By BIRTHDATE_YEAR = By.name("birthdateYear");

    static final By GENDER_LABEL = By.id("genderLabel");
    static final By BIRTHDATE_LABEL = By.id("birthdateLabel");

    static final By CONFIRM_EDIT = By.xpath("//ul[@id='formBreadcrumb']/li/span[text()='Confirm']");
    static final By CONFIRM = By.cssSelector("input[value='Confirm']");

    public EditDemographicsPage(Page page) {
        super(page);
    }

    @Override
    public String getPageUrl() {
        return "registrationapp/editSection.page";
    }

    public void enterPatientGivenName(String givenName) {
        setText(GIVEN_NAME, givenName);
    }

    public void enterPatientMiddleName(String givenName) {
        setText(MIDDLE_NAME, givenName);
    }

    public void enterPatientFamilyName(String givenName) {
        setText(FAMILY_NAME, givenName);
    }

    public void clickOnGenderLabel() throws InterruptedException {
        clickOn(GENDER_LABEL);
    }

    public void selectPatientGender(String gender) {
        selectFrom(GENDER_SELECT, gender);
    }

    public void clickOnBirthdateLabel() throws InterruptedException {
        clickOn(BIRTHDATE_LABEL);
    }

    public void enterBirthDay(String birthDay) {
        setText(BIRTHDATE_DAY, birthDay);
    }

    public void selectBirthMonth(String birthMonth) {
        selectFrom(BIRTHDATE_MONTH, birthMonth);
    }

    public void enterBirthYear(String birthYear) {
        setText(BIRTHDATE_YEAR, birthYear);
    }

    public void clickOnConfirmEdit() {
        clickOn(CONFIRM_EDIT);
    }

    public void confirmPatient() throws InterruptedException {
        clickOn(CONFIRM);
    }

}
