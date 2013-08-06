package org.openmrs.reference.page;

import org.openmrs.reference.helper.TestPatient;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * The register-a-new-patient page.
 */
public class RegistrationPage extends AbstractBasePage {

    public RegistrationPage(WebDriver driver) {
        super(driver);
    }

    static final String CONTACT_INFO_SECTION_ID = "contactInfo_label";
    static final String CONFIRM_SECTION_ID = "confirmation_label";

    static final String GENDER_ID = "genderLabel";
    static final String BIRTHDATE_ID = "birthdateLabel";
    static final String PHONE_NUMBER_ID = "phoneNumberLabel";

    static final String GIVEN_NAME_TEXTBOX_ID = "givenName";
    static final String FAMILY_NAME_TEXTBOX_ID = "familyName";
    static final String GENDER_RADIO_BUTTON_ID = "input[value='XX']";
    static final String BIRTHDAY_DAY_TEXTBOX_ID = "birthdateDay-field";
    static final String BIRTHDAY_MONTH_DROPDOWN_ID = "birthdateMonth-field";
    static final String BIRTHDAY_YEAR_TEXTBOX_ID = "birthdateYear-field";
    static final String ADDRESS1_TEXTBOX_ID = "address1";
    static final String ADDRESS2_TEXTBOX_ID = "address2";
    static final String CITY_VILLAGE_TEXTBOX_ID = "cityVillage";
    static final String STATE_PROVINCE_TEXTBOX_ID = "stateProvince";
    static final String COUNTRY_TEXTBOX_ID = "country";
    static final String POSTAL_CODE_TEXTBOX_ID = "postalCode";
    static final String LATITUDE_TEXTBOX_ID = "latitude";
    static final String LONGITUDE_TEXTBOX_ID = "longitude";
    static final String PHONE_NUMBER_TEXTBOX_ID = "phoneNumber";
    
    static final String CONFIRMATION_DIV = "//div[@id='confirmation']";
	static final String NAME_CONFIRM = CONFIRMATION_DIV + "//li[1]/strong";             //Once we get an id, this xpath should be replaced
	static final String GENDER_CONFIRM = CONFIRMATION_DIV + "//li[2]/strong";             //Once we get an id, this xpath should be replaced
	static final String BIRTHDATE_CONFIRM = CONFIRMATION_DIV + "//li[3]/strong";             //Once we get an id, this xpath should be replaced
	static final String ADDRESS_CONFIRM = CONFIRMATION_DIV + "//li[4]/strong";             //Once we get an id, this xpath should be replaced
	static final String PHONE_CONFIRM = CONFIRMATION_DIV + "//li[5]/strong";             //Once we get an id, this xpath should be replaced


    public void enterPatientAddress(TestPatient patient) {
        setTextToField(By.id(ADDRESS1_TEXTBOX_ID), patient.address1);
        setTextToField(By.id(ADDRESS2_TEXTBOX_ID), patient.address2);
        setTextToField(By.id(CITY_VILLAGE_TEXTBOX_ID), patient.city);
        setTextToField(By.id(STATE_PROVINCE_TEXTBOX_ID), patient.state);
        setTextToField(By.id(COUNTRY_TEXTBOX_ID), patient.country);
        setTextToField(By.id(POSTAL_CODE_TEXTBOX_ID), patient.postalCode);
        setTextToField(By.id(LATITUDE_TEXTBOX_ID), patient.latitude);
        setTextToField(By.id(LONGITUDE_TEXTBOX_ID), patient.longitude);
    }

    public void enterPatientBirthDate(TestPatient patient) {
        setTextToField(By.id(BIRTHDAY_DAY_TEXTBOX_ID), patient.birthDay);
        selectFrom(By.id(BIRTHDAY_MONTH_DROPDOWN_ID), patient.birthMonth);
        setTextToField(By.id(BIRTHDAY_YEAR_TEXTBOX_ID), patient.birthYear);
    }

    public void selectPatientGender(String gender) {
        clickOn(By.cssSelector(GENDER_RADIO_BUTTON_ID.replace("XX", gender)));
    }

    public void enterPatientFamilyName(String familyName) {
        setTextToField(By.name(FAMILY_NAME_TEXTBOX_ID), familyName);
    }

    public void enterPatientGivenName(String givenName) {
    	// TODO Temporary experiment - just wait 1 second for page to finish js scripts
    	try {
	        Thread.sleep(1000);
        }
        catch (InterruptedException e) {
        }
    	By by = By.name(GIVEN_NAME_TEXTBOX_ID);
    	// TODO Temporary experiment to see if it helps with intermittent failures.
//    	WebDriverWait wait = new WebDriverWait(driver, 10);
//    	WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(by));
//		setText(element, givenName);
		setTextToField(by, givenName);
    }

    public void enterPatientGivenNameForAutoSuggestFn(String name){
        setTextToField(By.name(GIVEN_NAME_TEXTBOX_ID), name);
    }
    public void enterPatientFamilyNameForAutoSuggestFn(String name){
        setTextToField(By.name(FAMILY_NAME_TEXTBOX_ID), name);
    }

    public void clickOnContactInfo(){
        clickOn(By.id(CONTACT_INFO_SECTION_ID));
    }

    public void clickOnPhoneNumber() {
        clickOn(By.id(PHONE_NUMBER_ID));
    }

	public void enterPhoneNumber(String phone) {
        setTextToField(By.name(PHONE_NUMBER_TEXTBOX_ID), phone);
    }

    public void clickOnConfirm() {
        clickOn(By.id(CONFIRM_SECTION_ID));
    }

    public void clickOnGenderLink(){
        clickOn(By.id(GENDER_ID));
    }

    public void clickOnBirthDateLink(){
        clickOn(By.id(BIRTHDATE_ID));
    }

    public String getNameInConfirmationPage() {
        return getText(By.xpath(NAME_CONFIRM)) ;
    }

    public String getGenderInConfirmationPage() {
    	return getText(By.xpath(GENDER_CONFIRM)) ;
    }
    
    public String getBirthdateInConfirmationPage() {
    	return getText(By.xpath(BIRTHDATE_CONFIRM)) ;
    }
    
    public String getAddressInConfirmationPage() {
    	return getText(By.xpath(ADDRESS_CONFIRM)) ;
    }
    
    public String getPhoneInConfirmationPage() {
    	return getText(By.xpath(PHONE_CONFIRM)) ;
    }
    
	@Override
    public String expectedUrlPath() {
	    return OPENMRS_PATH + "/registrationapp/registerPatient.page?appId=referenceapplication.registrationapp.registerPatient";
    }

}
