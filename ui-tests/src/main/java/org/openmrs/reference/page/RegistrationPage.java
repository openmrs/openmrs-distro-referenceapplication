package org.openmrs.reference.page;

import org.openmrs.reference.helper.PatientGenerator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegistrationPage extends AbstractBasePage {

    public RegistrationPage(WebDriver driver) {
        super(driver);
    }


    private String DEMOGRAPHICS_SPAN = "ul#formBreadcrumb li:nth-of-type(1)";
    private String PHONE_NUMBER_SPAN = "ul#formBreadcrumb li:nth-of-type(2)";
    private String CONFIRM_SPAN = "ul#formBreadcrumb li:nth-of-type(3)";
    private String NAME_LINK = "ul#formBreadcrumb li:nth-of-type(1) li:nth-of-type(1)";
    private String GENDER_LINK = "ul#formBreadcrumb li:nth-of-type(1) li:nth-of-type(2)";
    private String BIRTHDATE_LINK = "ul#formBreadcrumb li:nth-of-type(1) li:nth-of-type(3)";
    private String ADDRESS_LINK = "ul#formBreadcrumb li:nth-of-type(1) li:nth-of-type(4)";

    private String GivenName_TxtBox = "fr9985-field";
    private String FamilyName_TxtBox = "fr4462-field";
    private String Gender_SelectBox = "fr2708-field";
    private String BirthDate_Day_TxtBox = "fr391-field";
    private String BirthDate_Month_SelectBox = "fr4090-field";
    private String BirthDate_Year_TxtBox = "fr6193-field";
    private String Address1_TxtBox = "address1";
    private String Address2_TxtBox = "address2";
    private String CityVillage_TxtBox = "cityVillage";
    private String State_TxtBox = "stateProvince";
    private String Country_TxtBox = "country";
    private String PostalCode_TxtBox = "postalCode";
    private String PhoneNumber_TxtBox = "fr125-field";


    public void clickOnDemographics() {
        clickOn(By.cssSelector(DEMOGRAPHICS_SPAN));
    }

    public void registerPatient(){
        clickOnDemographics();
        clickOnNameLink();
        setTextToField(GivenName_TxtBox, PatientGenerator.getPatientGivenName());
        setTextToField(FamilyName_TxtBox, PatientGenerator.getPatientFamilyName());

        clickOnGenderLink();
        selectCombo(By.id(Gender_SelectBox), PatientGenerator.getPatientGender());

        clickOnBirthDateLink();
        setTextToField(BirthDate_Day_TxtBox, "5");
        selectCombo(By.id(BirthDate_Month_SelectBox), PatientGenerator.getPatientBirthMonth());
        setTextToField(BirthDate_Year_TxtBox, "1985");

        clickOnAddressLink();
        setTextToField(Address1_TxtBox, PatientGenerator.getPatientAddress1());
        setTextToField(Address2_TxtBox, PatientGenerator.getPatientAddress2());
        setTextToField(CityVillage_TxtBox, PatientGenerator.getPatientCity());
        setTextToField(State_TxtBox, PatientGenerator.getPatientState());
        setTextToField(Country_TxtBox,PatientGenerator.getPatientCountry());
        setTextToField(PostalCode_TxtBox,"345234");

        clickOnPhoneNumber();
        setTextToField(PhoneNumber_TxtBox,PatientGenerator.getPhoneNumber());

        clickOnConfirm();
    }

    public void clickOnPhoneNumber() {
        clickOn(By.cssSelector(PHONE_NUMBER_SPAN));
    }

    public void clickOnConfirm() {
        clickOn(By.cssSelector(CONFIRM_SPAN));
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


}
