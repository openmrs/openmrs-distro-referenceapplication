package org.openmrs.reference.page;

import org.openmrs.reference.helper.TestPatient;
import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openmrs.uitestframework.test.TestData.PatientInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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
    static final By MIDDLE_NAME = By.name("middleName");
    static final By GENDER_LABEL = By.id("genderLabel");
    static final String GENDER_FIELD_ID = ("gender-field");
    static final By GENDER = By.id(GENDER_FIELD_ID);
    static final By GENDER_SELECT = By.name("gender");
    static final String BIRTHDAY_DAY_TEXTBOX_ID = "birthdateDay-field";
    public static final By BIRTHDAY_DAY = By.id(BIRTHDAY_DAY_TEXTBOX_ID);
    public static final By BIRTHDAY_MONTH = By.id("birthdateMonth-field");
    public static final By BIRTHDAY_YEAR = By.id("birthdateYear-field");
	static final By ADDRESS1 = By.id("address1");
	static final By ADDRESS2 = By.id("address2");
	static final By CITY_VILLAGE = By.id("cityVillage");
    static final By STATE_PROVINCE = By.id("stateProvince");
    static final By COUNTRY = By.id("country");
    static final By POSTAL_CODE = By.id("postalCode");
    static final By PHONE_NUMBER = By.name("phoneNumber");
    static final By UNKNOWN_PATIENT = By.id("checkbox-unknown-patient");
    public static final By ESTIMATED_YEARS = By.id("birthdateYears-field");
    
    // These xpath expressions should be replaced by id's or cssSelectors if possible.
    static final By CONFIRM_EDIT = By.xpath("//ul[@id='formBreadcrumb']/li[2]/span");
    static final String CONFIRMATION_DIV = "//div[@id='confirmation']";
	public static final By NAME_CONFIRM = By.xpath(CONFIRMATION_DIV + "//li[1]/strong");
	public static final By GENDER_CONFIRM = By.xpath(CONFIRMATION_DIV + "//li[2]/strong");
	public static final By BIRTHDATE_CONFIRM = By.xpath(CONFIRMATION_DIV + "//li[3]/strong");
	static final By ADDRESS_CONFIRM = By.xpath(CONFIRMATION_DIV + "//li[4]/strong");
	static final By PHONE_CONFIRM = By.xpath(CONFIRMATION_DIV + "//li[5]/strong");

	static final By PATIENT_HEADER = By.className("patient-header");
	static final By CONFIRM = By.cssSelector("input[value='Confirm']");
    static final By REVIEW = By.id("reviewSimilarPatientsButton");
    static final By CANCEL = By.id("reviewSimilarPatients-button-cancel");
    public static final By FIELD_ERROR = By.id("field-error");
    static By AUTO_LIST;

	public void enterPatient(TestPatient patient) throws InterruptedException{
        enterPatientGivenName(patient.givenName);
        enterPatientMiddleName(patient.middleName);  // no middle name
        enterPatientFamilyName(patient.familyName);
        clickOnGenderLink();
        selectPatientGender(patient.gender);
        clickOnBirthDateLink();
        enterPatientBirthDate(patient);
        clickOnContactInfo();
        enterPatientAddress(patient);
        clickOnPhoneNumber();
        if(patient.phone != null && !patient.phone.isEmpty()) {
            enterPhoneNumber(patient.phone);
        }
        clickOnConfirmSection();
    }

    public void enterUnidentifiedPatient(TestPatient patient) throws InterruptedException {
        selectUnidentifiedPatient();
        clickOnGenderLink();
        selectPatientGender(patient.gender);
        clickOnConfirmSection();
    }

	public void enterPatientAddress(TestPatient patient) {
        setText(ADDRESS1, patient.address1);
        setText(ADDRESS2, patient.address2);
        if(patient.city != null && !patient.city.isEmpty()) {
            setText(CITY_VILLAGE, patient.city);
        }
        if(patient.state != null && !patient.state.isEmpty()) {
            setText(STATE_PROVINCE, patient.state);
        }
        if(patient.country != null && !patient.country.isEmpty()) {
            setText(COUNTRY, patient.country);
        }
        if(patient.postalCode != null && !patient.postalCode.isEmpty()) {
            setText(POSTAL_CODE, patient.postalCode);
        }
    }

    public void enterPatientBirthDate(TestPatient patient) {
        setText(BIRTHDAY_DAY, patient.birthDay);
        selectFrom(BIRTHDAY_MONTH, patient.birthMonth);
        setText(BIRTHDAY_YEAR, patient.birthYear);
    }

    public void enterEstimatedYears(String estimatedYears) {
        setText(ESTIMATED_YEARS, estimatedYears);
    }

    public void enterAddress1(String address1) {
        setText(ADDRESS1, address1);
    }


    public void selectUnidentifiedPatient() {
        clickOn(UNKNOWN_PATIENT);
    }

    public void selectPatientGender(String gender) { selectFrom(GENDER_SELECT, gender);}

    public void selectBirthMonth(String bitrthmonth) {
        selectFrom(BIRTHDAY_MONTH, bitrthmonth);
    }

    public void enterPatientFamilyName(String familyName) {
        setText(FAMILY_NAME, familyName);
    }

    public void enterPatientGivenName(String givenName) {
		setText(GIVEN_NAME, givenName);
    }

    public void enterPatientMiddleName(String middleName) {
        setText(MIDDLE_NAME, middleName);
    }

    public void enterBirthDay(String birthday) {
        setText(BIRTHDAY_DAY, birthday);
    }

    public void enterBirthYear(String bitrthyear){ setText(BIRTHDAY_YEAR, bitrthyear);}

    public void clickOnContactInfo() {
        clickOn(CONTACT_INFO_SECTION);
    }

    public void clickOnPhoneNumber() throws InterruptedException {
        clickWhenVisible(PHONE_NUMBER_LABEL);
    }

	public void enterPhoneNumber(String phone) {
        setText(PHONE_NUMBER, phone);
    }

    public void clickOnConfirmSection() throws InterruptedException{
        clickWhenVisible(CONFIRM_SECTION);
    }

    public void clickOnGenderLink() {clickOn(GENDER_LABEL);}

    public void clickOnBirthDateLink() throws InterruptedException{
        clickWhenVisible(BIRTHDATE_LABEL);
        waitForFocusById(BIRTHDAY_DAY_TEXTBOX_ID);
    }

    public boolean clickOnReviewButton() {
        try {
            clickOn(REVIEW);
            return true;
        } catch(Exception e) {
            return false;
        }
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

    public void clearName(){
        findElement(GIVEN_NAME).clear();
    }

    public void clearMiddleName(){
        findElement(MIDDLE_NAME).clear();
    }

    public void clearFamilyName(){
        findElement(FAMILY_NAME);
    }

    public void clearBirthDay(){
        findElement(BIRTHDAY_DAY);
    }

    public void clearBirthdateYear(){
        findElement(BIRTHDAY_YEAR);
    }

    public void clickOnBirthdateLabel(){
        clickOn(BIRTHDATE_LABEL);
    }

    public void clickOnConfirmEdit(){
        clickOn(CONFIRM_EDIT);
    }


	@Override
    public String expectedUrlPath() {
	    return URL_ROOT + "/registrationapp/registerPatient.page?appId=referenceapplication.registrationapp.registerPatient";
    }

	public void confirmPatient() throws InterruptedException{
		clickWhenVisible(CONFIRM);
		waitForElement(PATIENT_HEADER);
    }

    public void waitForDeletePatient() {
        waitForElementToBeHidden(PATIENT_HEADER);
    }

    public void exitReview() {
        try {
            clickOn(CANCEL);
        } catch(Exception e) {

        }
    }
//  Edit  Contact Info
    public void clearVillage(){findElement(CITY_VILLAGE).clear();}
    public void clearState(){findElement(STATE_PROVINCE).clear();}
    public void clearCountry(){findElement(COUNTRY).clear();}
    public void clearPostalCode(){findElement(POSTAL_CODE).clear();}
    public void clearPhoneNumber(){findElement(PHONE_NUMBER).clear();}
    private static final By PHONE_NUMBER_EDIT = By.xpath("//ul[@id='formBreadcrumb']/li/ul/li[2]/span");

    public void clickOnPhoneNumberEdit(){clickOn(PHONE_NUMBER_EDIT);}
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
//  AutocompleteTest
    public void enterAndWaitFamilyName(String family){
        setTextToFieldNoEnter(FAMILY_NAME, family);
        AUTO_LIST = By.xpath("//ul[4]/li/a");
        waitForElement(AUTO_LIST);
    }
    public void enterAndWaitGivenName(String given) {
        setTextToFieldNoEnter(GIVEN_NAME, given);
        waitForElement(By.className("ui-autocomplete"));
    }
}

