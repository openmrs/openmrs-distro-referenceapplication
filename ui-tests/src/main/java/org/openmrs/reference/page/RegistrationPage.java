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

    private String demographicsSectionLink = "//ul[@id='formBreadcrumb']/li[1]/span";        //Once we get an id, this xpath should be replaced
    private String contactInfoSectionLink = "//ul[@id='formBreadcrumb']/li[2]/span";       //Once we get an id, this xpath should be replaced
    private String confirmSectionLink = "//ul[@id='formBreadcrumb']/li[3]/span";            //Once we get an id, this xpath should be replaced

    private String nameLink = "ul#formBreadcrumb li:nth-of-type(1) li:nth-of-type(1)";        //Once we get an id, this css-selector should be replaced
    private String genderLink = "ul#formBreadcrumb li:nth-of-type(1) li:nth-of-type(2)";       //Once we get an id, this css-selector should be replaced
    private String birthDateLink = "ul#formBreadcrumb li:nth-of-type(1) li:nth-of-type(3)";    //Once we get an id, this css-selector should be replaced
    private String addressLink = "ul#formBreadcrumb li:nth-of-type(2) li:nth-of-type(2)";      //Once we get an id, this css-selector should be replaced
    private String phoneNumberLink = "ul#formBreadcrumb li:nth-of-type(2) li:nth-of-type(2)";  //Once we get an id, this css-selector should be replaced

    private String givenNameTxtBox = "givenName";
    private String familyNameTxtBox = "familyName";
    private String genderRadioBtn = "input[value='XX']";                  //Once we get an id, this css-selector should be replaced
    private String birthDayTxtBox = "birthdateDay-field";
    private String birthMonthDropDown = "birthdateMonth-field";
    private String birthYearTxtBox = "birthdateYear-field";
    private String address1TxtBox = "address1";
    private String address2TxtBox = "address2";
    private String cityVillageTxtBox = "cityVillage";
    private String stateProvinceTxtBox = "stateProvince";
    private String countryTxtBox = "country";
    private String postalCodeTxtBox = "postalCode";
    private String latitudeTxtBox = "latitude";
    private String longitudeTxtBox = "longitude";
    private String phoneNumberTxtBox = "phoneNumber";
    
    static final String CONFIRMATION_DIV = "//div[@id='confirmation']";
	static final String NAME_CONFIRM = CONFIRMATION_DIV + "//li[1]/strong";             //Once we get an id, this xpath should be replaced
	static final String GENDER_CONFIRM = CONFIRMATION_DIV + "//li[2]/strong";             //Once we get an id, this xpath should be replaced
	static final String BIRTHDATE_CONFIRM = CONFIRMATION_DIV + "//li[3]/strong";             //Once we get an id, this xpath should be replaced
	static final String ADDRESS_CONFIRM = CONFIRMATION_DIV + "//li[4]/strong";             //Once we get an id, this xpath should be replaced
	static final String PHONE_CONFIRM = CONFIRMATION_DIV + "//li[5]/strong";             //Once we get an id, this xpath should be replaced


    public void clickOnDemographics() {
        clickOn(By.xpath(demographicsSectionLink));
    }

    public void enterPatientAddress(TestPatient patient) {
        setTextToField(By.id(address1TxtBox), patient.address1);
        setTextToField(By.id(address2TxtBox), patient.address2);
        setTextToField(By.id(cityVillageTxtBox), patient.city);
        setTextToField(By.id(stateProvinceTxtBox), patient.state);
        setTextToField(By.id(countryTxtBox), patient.country);
        setTextToField(By.id(postalCodeTxtBox), patient.postalCode);
        setTextToField(By.id(latitudeTxtBox), patient.latitude);
        setTextToField(By.id(longitudeTxtBox), patient.longitude);
    }

    public void enterPatientBirthDate(TestPatient patient) {
        setTextToField(By.id(birthDayTxtBox), patient.birthDay);
        selectFrom(By.id(birthMonthDropDown), patient.birthMonth);
        setTextToField(By.id(birthYearTxtBox), patient.birthYear);
    }

    public void selectPatientGender(String gender) {
        clickOn(By.cssSelector(genderRadioBtn.replace("XX", gender)));
    }

    public void enterPatientFamilyName(String familyName) {
        setTextToField(By.name(familyNameTxtBox), familyName);
    }

    public void enterPatientGivenName(String givenName) {
        setTextToField(By.name(givenNameTxtBox), givenName);
    }

    public void enterPatientGivenNameForAutoSuggestFn(String name){
        setTextToField(By.name(givenNameTxtBox), name);
    }
    public void enterPatientFamilyNameForAutoSuggestFn(String name){
        setTextToField(By.name(familyNameTxtBox), name);
    }

    public void clickOnContactInfo(){
        clickOn(By.xpath(contactInfoSectionLink));
    }

    public void clickOnPhoneNumber() {
        clickOn(By.cssSelector(phoneNumberLink));
    }

	public void enterPhoneNumber(String phone) {
        setTextToField(By.name(phoneNumberTxtBox), phone);
    }

    public void clickOnConfirm() {
        clickOn(By.xpath(confirmSectionLink));
    }

    public void clickOnNameLink(){
        clickOn(By.cssSelector(nameLink));
    }

    public void clickOnGenderLink(){
        clickOn(By.cssSelector(genderLink));
    }

    public void clickOnBirthDateLink(){
        clickOn(By.cssSelector(birthDateLink));
    }

    public void clickOnAddressLink(){
        clickOn(By.cssSelector(addressLink));
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
	    return "/openmrs/registrationapp/registerPatient.page?appId=referenceapplication.registrationapp.registerPatient";
    }


}
