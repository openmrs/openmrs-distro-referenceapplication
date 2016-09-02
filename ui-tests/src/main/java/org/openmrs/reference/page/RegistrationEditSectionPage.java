package org.openmrs.reference.page;

import org.apache.commons.lang.StringUtils;
import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class RegistrationEditSectionPage extends Page {

    static final By CITY_VILLAGE = By.id("cityVillage");
    static final By STATE_PROVINCE = By.id("stateProvince");
    static final By COUNTRY = By.id("country");
    static final By POSTAL_CODE = By.id("postalCode");
    static final By PHONE_NUMBER = By.name("phoneNumber");
    static final By CONFIRM_EDIT = By.xpath("//ul[@id='formBreadcrumb']/li[2]/span");
    static final By CONFIRM = By.cssSelector("input[value='Confirm']");
    private static final By PHONE_NUMBER_EDIT = By.xpath("//ul[@id='formBreadcrumb']/li/ul/li[2]/span");

    public RegistrationEditSectionPage(Page parent) {
        super(parent);
    }

    @Override
    public String getPageUrl() {
        return "/registrationapp/editSection.page";
    }

    public void clearPhoneNumber(){
        findElement(PHONE_NUMBER).clear();
    }

    public void clearVillage(){
        findElement(CITY_VILLAGE).clear();
    }

    public void clearState(){
        findElement(STATE_PROVINCE).clear();
    }

    public void clearCountry(){
        findElement(COUNTRY).clear();
    }

    public void clearPostalCode(){
        findElement(POSTAL_CODE).clear();
    }

    public void clickOnPhoneNumberEdit(){
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

    public void clickOnConfirmEdit(){
        clickOn(CONFIRM_EDIT);
    }

    public void enterPhoneNumber(String phone) {
        setText(PHONE_NUMBER, phone);
    }

    public String getInvalidPhoneNumberNotification() {
        List<String> errors = getValidationErrors();
        String search = "valid phone number";
        for(String str: errors) {
            if(str.trim().contains(search))
                return str;
        }
        return null;
    }

    public ClinicianFacingPatientDashboardPage confirmPatient() throws InterruptedException{
        clickOn(CONFIRM);
        return new ClinicianFacingPatientDashboardPage(this);
    }
}
