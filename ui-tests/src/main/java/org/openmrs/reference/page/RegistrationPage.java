package org.openmrs.reference.page;

import org.openmrs.reference.helper.PatientGenerator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegistrationPage extends AbstractBasePage {


    public RegistrationPage(WebDriver driver) {
        super(driver);
    }


    private String DEMOGRAPHICS_SPAN = "//ul[@id='formBreadcrumb']/li[1]/span";
    private String PHONE_NUMBER_SPAN = "//ul[@id='formBreadcrumb']/li[2]/span";
    private String CONFIRM_SPAN = "//ul[@id='formBreadcrumb']/li[3]/span";
    private String NAME_LINK = "ul#formBreadcrumb li:nth-of-type(1) li:nth-of-type(1)";
    private String GENDER_LINK = "ul#formBreadcrumb li:nth-of-type(1) li:nth-of-type(2)";
    private String DATE_PICKER = "span.date i.icon-calendar";
    private String DATE_WIDGET_ELEMENT = "div.datetimepicker-days tbody tr:nth-child(1) td";
    private String BIRTHDATE_LINK = "ul#formBreadcrumb li:nth-of-type(1) li:nth-of-type(3)";
    private String ADDRESS_LINK = "ul#formBreadcrumb li:nth-of-type(1) li:nth-of-type(4)";

    private String GivenName_TxtBox = "givenName";
    private String FamilyName_TxtBox = "familyName";
    private String Gender = "input[value='XX']";
    private String BirthDate_Day_TxtBox = "fr391-field";
    private String BirthDate_Month_SelectBox = "fr4090-field";
    private String BirthDate_Year_TxtBox = "fr6193-field";
    private String Address1_TxtBox = "address1";
    private String Address2_TxtBox = "address2";
    private String CityVillage_TxtBox = "cityVillage";
    private String State_TxtBox = "stateProvince";
    private String Country_TxtBox = "country";
    private String PostalCode_TxtBox = "postalCode";
    private String PhoneNumber_TxtBox = "phoneNumber";


    public void clickOnDemographics() {
        clickOn(By.xpath(DEMOGRAPHICS_SPAN));
    }

    public void registerPatient(){
        setTextToField(By.name(GivenName_TxtBox), PatientGenerator.getPatientGivenName());
        setTextToField(By.name(FamilyName_TxtBox), PatientGenerator.getPatientFamilyName());
        clickOnGenderLink();
        clickOn(By.cssSelector(Gender.replace("XX",PatientGenerator.getPatientGender())));

        clickOnBirthDateLink();
//        setTextToField(BirthDate_Day_TxtBox, "5");
//        selectFromCombo(By.id(BirthDate_Month_SelectBox), PatientGenerator.getPatientBirthMonth());
//        setTextToField(BirthDate_Year_TxtBox, "1985");
         clickOn(By.cssSelector(DATE_PICKER));
         clickOn(By.cssSelector(DATE_WIDGET_ELEMENT));

        clickOnAddressLink();
        setTextToField(By.id(Address1_TxtBox), PatientGenerator.getPatientAddress1());
        setTextToField(By.id(Address2_TxtBox), PatientGenerator.getPatientAddress2());
        setTextToField(By.id(CityVillage_TxtBox), PatientGenerator.getPatientCity());
        setTextToField(By.id(State_TxtBox), PatientGenerator.getPatientState());
        setTextToField(By.id(Country_TxtBox),PatientGenerator.getPatientCountry());
        setTextToField(By.id(PostalCode_TxtBox),"345234");

        clickOnPhoneNumber();
        setTextToField(By.name(PhoneNumber_TxtBox), PatientGenerator.getPhoneNumber());
//
//        clickOnConfirm();
    }

    public void clickOnPhoneNumber() {
        clickOn(By.xpath(PHONE_NUMBER_SPAN));
    }

    public void clickOnConfirm() {
        clickOn(By.xpath(CONFIRM_SPAN));
    }

    public void clickOnNameLink(){
        clickOn(By.cssSelector(NAME_LINK));
    }

    public void clickOnGenderLink(){
        clickOn(By.cssSelector(GENDER_LINK));
    }

    public void clickOnBirthDateLink(){
        clickOn(By.cssSelector(BIRTHDATE_LINK));
    }

    public void clickOnAddressLink(){
        clickOn(By.cssSelector(ADDRESS_LINK));
    }

	@Override
    public String expectedUrlPath() {
	    return "/openmrs/registrationapp/registerPatient.page?appId=referenceapplication.registrationapp.registerPatient";
    }


}
