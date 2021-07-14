package org.openmrs.reference.page;

import java.util.List;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

public class RegistrationEditSectionPage extends Page {

    private static final By PHONE_NUMBER = By.name("phoneNumber");
    private static final By CONFIRM_EDIT = By.xpath("//ul[@id='formBreadcrumb']/li[2]/span");
    private static final By CONFIRM = By.id("registration-submit");
    private static final By PHONE_NUMBER_EDIT = By.xpath("//ul[@id='formBreadcrumb']/li/ul/li[2]/span");
    private static final By GIVEN_NAME_FIELD = By.cssSelector("#demographics-name input[name='givenName']");
    private static final By MIDDLE_NAME_FIELD = By.cssSelector("#demographics-name input[name='middleName']");
    private static final By FAMILY_NAME_FIELD = By.cssSelector("#demographics-name input[name='familyName']");
    private static final By GENDER_SELECT = By.cssSelector("#demographics-gender select[name='gender']");
    private static final By BIRTHDAY_MONTH = By.cssSelector("#demographics-birthdate select[name='birthdateMonth']");
    private static final By BIRTHDAY_YEAR = By.id("birthdateYear-field");
    private static final By BIRTHDAY_DAY = By.id("birthdateDay-field");
    private static final By BIRTHDATE_LABEL = By.id("birthdateLabel");
    private static final By CITY_VILLAGE = By.id("cityVillage");
    private static final By STATE_PROVINCE = By.id("stateProvince");
    private static final By COUNTRY = By.id("country");
    private static final By POSTAL_CODE = By.id("postalCode");
    private static final String str = "Must be a valid phone number (with +, -, numbers or parentheses))";

    public RegistrationEditSectionPage(Page parent) {
        super(parent);
    }

    public void clickOnBirthdateLabel() {
        clickOn(BIRTHDATE_LABEL);
    }

    public void enterGivenName(String name) {
        setText(GIVEN_NAME_FIELD, name);
    }

    public void enterMiddleName(String name) {
        setText(MIDDLE_NAME_FIELD, name);
    }

    public void enterFamilyName(String name) {
        setText(FAMILY_NAME_FIELD, name);
    }

    public void selectPatientGender(String gender) {
        selectFrom(GENDER_SELECT, gender);
    }

    public void selectBirthMonth(String bitrthMonth) {
        selectFrom(BIRTHDAY_MONTH, bitrthMonth);
    }

    public void enterBirthDay(String birthday) {
        setText(BIRTHDAY_DAY, birthday);
    }

    public void enterBirthYear(String bitrthYear) {
        setText(BIRTHDAY_YEAR, bitrthYear);
    }

    @Override
    public String getPageUrl() {
        return "/registrationapp/editSection.page";
    }

    public void clearPhoneNumber() {
        findElement(PHONE_NUMBER).clear();
    }

    public void clearVillage() {
        findElement(CITY_VILLAGE).clear();
    }

    public void clearState() {
        findElement(STATE_PROVINCE).clear();
    }

    public void clearCountry() {
        findElement(COUNTRY).clear();
    }

    public void clearPostalCode() {
        findElement(POSTAL_CODE).clear();
    }

    public void clickOnPhoneNumberEdit() {
        clickOn(PHONE_NUMBER_EDIT);
    }

    public void enterVillage(String familyName) {
        setText(CITY_VILLAGE, familyName);
    }

    public void enterState(String familyName) {
        setText(STATE_PROVINCE, familyName);
    }

    public void enterPostalCode(String familyName) {
        setText(POSTAL_CODE, familyName);
    }

    public void enterCountry(String familyName) {
        setText(COUNTRY, familyName);
    }

    public void clickOnConfirmEdit() {
        clickOn(CONFIRM_EDIT);
    }

    public void enterPhoneNumber(String phone) {
        setText(PHONE_NUMBER, phone);
    }

    public String getInvalidPhoneNumberNotification() {
        List<String> errors = getValidationErrors();
        String search = "valid phone number";
        if (errors != null) {
        for (String str : errors) {
            if (str.trim().contains(search))
                return str;
        }
        return str;
        }
        return null;
    }

    public ClinicianFacingPatientDashboardPage confirmPatient() throws InterruptedException {
        clickOn(CONFIRM);
        return new ClinicianFacingPatientDashboardPage(this);
    }
}
