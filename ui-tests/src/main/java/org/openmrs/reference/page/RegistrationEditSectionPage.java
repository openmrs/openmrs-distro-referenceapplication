package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

public class RegistrationEditSectionPage extends Page {

    private static final By PHONE_NUMBER = By.name("phoneNumber");
    private static final By CONFIRM_EDIT = By.xpath("//ul[@id='formBreadcrumb']/li[2]/span");
    private static final By CONFIRM = By.cssSelector("input[value='Confirm']");
    private static final By PHONE_NUMBER_EDIT = By.xpath("//ul[@id='formBreadcrumb']/li/ul/li[2]/span");
    private static final By GIVEN_NAME_FIELD = By.cssSelector("#demographics-name input[name='givenName']");
    private static final By MIDDLE_NAME_FIELD = By.cssSelector("#demographics-name input[name='middleName']");
    private static final By FAMILY_NAME_FIELD = By.cssSelector("#demographics-name input[name='familyName']");
    private static final By GENDER_SELECT = By.cssSelector("#demographics-gender select[name='gender']");
    private static final By BIRTHDAY_MONTH = By.cssSelector("#demographics-birthdate select[name='birthdateMonth']");
    private static final By BIRTHDAY_YEAR = By.id("birthdateYear-field");
    private static final By BIRTHDAY_DAY = By.id("birthdateDay-field");
    private static final By BIRTHDATE_LABEL = By.id("birthdateLabel");

    public RegistrationEditSectionPage(Page parent) {
        super(parent);
    }

    public void clickOnBirthdateLabel(){
        clickOn(BIRTHDATE_LABEL);
    }

    public void enterGivenName(String name){
        setText(GIVEN_NAME_FIELD, name);
    }

    public void enterMiddleName(String name){
        setText(MIDDLE_NAME_FIELD, name);
    }

    public void enterFamilyName(String name){
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

    public void enterBirthYear(String bitrthYear){
        setText(BIRTHDAY_YEAR, bitrthYear);
    }

    public void clickOnPhoneNumberEdit(){clickOn(PHONE_NUMBER_EDIT);}

    public void clearPhoneNumber(){findElement(PHONE_NUMBER).clear();}

    public void clickOnConfirmEdit(){
        clickOn(CONFIRM_EDIT);
    }

    public void enterPhoneNumber(String phone) {
        setText(PHONE_NUMBER, phone);
    }

    public ClinicianFacingPatientDashboardPage confirmPatient() throws InterruptedException{
        clickOn(CONFIRM);
        return new ClinicianFacingPatientDashboardPage(this);
    }

    @Override
    public String getPageUrl() {
        return "/registrationapp/editSection.page";
    }
}
