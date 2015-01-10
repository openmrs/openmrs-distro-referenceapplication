/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.reference.page;

import org.openmrs.reference.helper.TestPatient;
import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

/**
 * The register-a-new-patient page.
 */
public class RegistrationPage extends AbstractBasePage {


	public RegistrationPage(WebDriver driver) {
        super(driver);
    }

	static final By CONTACT_INFO_SECTION = By.id("contactInfo_label");
	static final By CONFIRM_SECTION = By.id("confirmation_label");
	static final By BIRTHDATE_LABEL = By.id("birthdateLabel");
    static final By PHONE_NUMBER_LABEL = By.id("phoneNumberLabel");
	static final By GIVEN_NAME = By.name("givenName");
	static final By FAMILY_NAME = By.name("familyName");
	static final By GENDER_SELECT = By.id("gender-field");
    static final String GENDER_RADIO_BUTTON_ID = "input[value='XX']";
    static final String BIRTHDAY_DAY_TEXTBOX_ID = "birthdateDay-field";
    static final By BIRTHDAY_DAY = By.id(BIRTHDAY_DAY_TEXTBOX_ID);
    static final By BIRTHDAY_MONTH = By.id("birthdateMonth-field");
    static final By BIRTHDAY_YEAR = By.id("birthdateYear-field");
	static final By ADDRESS1 = By.id("address1");
	static final By ADDRESS2 = By.id("address2");
	static final By CITY_VILLAGE = By.id("cityVillage");
    static final By STATE_PROVINCE = By.id("stateProvince");
    static final By COUNTRY = By.id("country");
    static final By POSTAL_CODE = By.id("postalCode");
    static final By PHONE_NUMBER = By.name("phoneNumber");
    
    // These xpath expressions should be replaced by id's or cssSelectors if possible.
    static final String CONFIRMATION_DIV = "//div[@id='confirmation']";
	static final By NAME_CONFIRM = By.xpath(CONFIRMATION_DIV + "//li[1]/strong");
	static final By GENDER_CONFIRM = By.xpath(CONFIRMATION_DIV + "//li[2]/strong");
	static final By BIRTHDATE_CONFIRM = By.xpath(CONFIRMATION_DIV + "//li[3]/strong");
	static final By ADDRESS_CONFIRM = By.xpath(CONFIRMATION_DIV + "//li[4]/strong");
	static final By PHONE_CONFIRM = By.xpath(CONFIRMATION_DIV + "//li[5]/strong");

	static final By PATIENT_HEADER = By.className("patient-header");
	static final By CONFIRM = By.cssSelector("input[value='Confirm']");

	public void enterPatient(TestPatient patient) {
        enterPatientGivenName(patient.givenName);
        enterPatientFamilyName(patient.familyName);
        clickOnGenderLink();
        selectPatientGender(patient.gender);
        clickOnBirthDateLink();
        enterPatientBirthDate(patient);
        clickOnContactInfo();
        enterPatientAddress(patient);
        clickOnPhoneNumber();
        enterPhoneNumber(patient.phone);
        clickOnConfirmSection();
    }

	public void enterPatientAddress(TestPatient patient) {
        setText(ADDRESS1, patient.address1);
        setText(ADDRESS2, patient.address2);
        setText(CITY_VILLAGE, patient.city);
        setText(STATE_PROVINCE, patient.state);
        setText(COUNTRY, patient.country);
        setText(POSTAL_CODE, patient.postalCode);
    }

    public void enterPatientBirthDate(TestPatient patient) {
        setText(BIRTHDAY_DAY, patient.birthDay);
        selectFrom(BIRTHDAY_MONTH, patient.birthMonth);
        setText(BIRTHDAY_YEAR, patient.birthYear);
    }

    public void selectPatientGender(String gender) {
        clickOn(By.cssSelector(GENDER_RADIO_BUTTON_ID.replace("XX", gender)));
    }

    public void enterPatientFamilyName(String familyName) {
        setText(FAMILY_NAME, familyName);
    }

    public void enterPatientGivenName(String givenName) {
		setText(GIVEN_NAME, givenName);
    }
    
    public void enterPatientGender(int gender) {
    	Select select = new Select(driver.findElement(GENDER_SELECT));
    	select.selectByIndex(gender);
    }

    public void clickOnContactInfo() {
        clickOn(CONTACT_INFO_SECTION);
    }

    public void clickOnPhoneNumber() {
        clickOn(PHONE_NUMBER_LABEL);
    }

	public void enterPhoneNumber(String phone) {
        setText(PHONE_NUMBER, phone);
    }

    public void clickOnConfirmSection() {
        clickOn(CONFIRM_SECTION);
    }

    public void clickOnGenderLink() {
    	waitForFocusByCss("select", "id", "gender-field");
    }

    public void clickOnBirthDateLink() {
        clickOn(BIRTHDATE_LABEL);
        waitForFocusById(BIRTHDAY_DAY_TEXTBOX_ID);
    }

	public String getNameInConfirmationPage() {
        return getText(NAME_CONFIRM) ;
    }

    public String getGenderInConfirmationPage() {
    	return getText(GENDER_CONFIRM) ;
    }
    
    public String getBirthdateInConfirmationPage() {
    	return getText(BIRTHDATE_CONFIRM) ;
    }
    
    public String getAddressInConfirmationPage() {
    	return getText(ADDRESS_CONFIRM) ;
    }
    
    public String getPhoneInConfirmationPage() {
    	return getText(PHONE_CONFIRM) ;
    }
    
	@Override
    public String expectedUrlPath() {
	    return URL_ROOT + "/registrationapp/registerPatient.page?appId=referenceapplication.registrationapp.registerPatient";
    }

	public void confirmPatient() {
		clickOn(CONFIRM);
		waitForElement(PATIENT_HEADER);
    }

}
