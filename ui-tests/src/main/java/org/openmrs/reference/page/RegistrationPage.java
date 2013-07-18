package org.openmrs.reference.page;

import org.openmrs.reference.helper.PatientGenerator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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
    private String phoneNumberLink = "ul#formBreadcrumb li:nth-of-type(2) li:nth-of-type(1)";  //Once we get an id, this css-selector should be replaced

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
    private String startDateTxtBox = "startDate";          //Will be removed once the field is taken out from Registration Page
    private String endDateTxtBox = "endDate";              //Will be removed once the field is taken out from Registration Page
    private String phoneNumberTxtBox = "phoneNumber";
    private String addressInConfirmationPage = "//div[@id='dataCanvas']//li[4]/strong";             //Once we get an id, this xpath should be replaced


    public void clickOnDemographics() {
        clickOn(By.xpath(demographicsSectionLink));
    }

    public void registerPatient(){
        enterPatientGivenName();
        enterPatientFamilyName();
        clickOnGenderLink();
        selectPatientGender();

        clickOnBirthDateLink();
        enterPatientBirthDate();

        clickOnContactInfo();
        clickOnAddressLink();
        enterPatientAddress();

        clickOnPhoneNumber();
        setTextToField(By.name(phoneNumberTxtBox), PatientGenerator.getPhoneNumber());
//            Waiting for Confirm page to be fixed
//        clickOnConfirm();
    }

    public void enterPatientAddress() {
        setTextToField(By.id(address1TxtBox), PatientGenerator.getPatientAddress1());
        setTextToField(By.id(address2TxtBox), PatientGenerator.getPatientAddress2());
        setTextToField(By.id(cityVillageTxtBox), PatientGenerator.getPatientCity());
        setTextToField(By.id(stateProvinceTxtBox), PatientGenerator.getPatientState());
        setTextToField(By.id(countryTxtBox),PatientGenerator.getPatientCountry());
        setTextToField(By.id(postalCodeTxtBox),"345234");

        setTextToField(By.id(latitudeTxtBox),"12");
        setTextToField(By.id(longitudeTxtBox),"47");
    }

    public void enterPatientBirthDate() {
        setTextToField(By.id(birthDayTxtBox),PatientGenerator.getPatientBirthDay());
        selectFrom(By.id(birthMonthDropDown), PatientGenerator.getPatientBirthMonth());
        setTextToField(By.id(birthYearTxtBox),PatientGenerator.getPatientBirthYear());

    }

    public void selectPatientGender() {
        clickOn(By.cssSelector(genderRadioBtn.replace("XX", PatientGenerator.getPatientGender())));
    }

    public void enterPatientFamilyName() {
        setTextToField(By.name(familyNameTxtBox), PatientGenerator.getPatientFamilyName());
    }

    public void enterPatientGivenName() {
        setTextToField(By.name(givenNameTxtBox), PatientGenerator.getPatientGivenName());
    }

    public void enterPatientGivenNameForAutoSuggestFn(String name){
        setTextToField(By.name(givenNameTxtBox),name);
    }
    public void enterPatientFamilyNameForAutoSuggestFn(String name){
        setTextToField(By.name(familyNameTxtBox),name);
    }

    public void clickOnContactInfo(){
        clickOn(By.xpath(contactInfoSectionLink));
    }

    public void clickOnPhoneNumber() {
        clickOn(By.xpath(phoneNumberLink));
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

    public String getAddressValueInConfirmationPage() {
        return  getText(By.xpath(addressInConfirmationPage)) ;
    }


	@Override
    public String expectedUrlPath() {
	    return "/openmrs/registrationapp/registerPatient.page?appId=referenceapplication.registrationapp.registerPatient";
    }


}
