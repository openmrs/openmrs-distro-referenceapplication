package org.openmrs.reference.page;


import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class SettingPage extends AbstractBasePage{
    public SettingPage(WebDriver driver) {
        super(driver);
    }

    private static final By SETTING = By.xpath("//a[contains(@href, '/openmrs/admin/maintenance/settings.list')]");
    private static final By REGISTRATIONCORE = By.linkText("Registrationcore");
    private static final By FAMILY_NAME_LIST = By.id("settings0.globalProperty.propertyValue");
    private static final By GIVEN_NAME_LIST = By.id("settings2.globalProperty.propertyValue");
    private static final By SAVE_LIST = By.id("saveButton");
    public String GIVEN;
    public String FAMILY;
    public static By LIST = By.className("ui-corner-all");
    private static final By SECURITY = By.linkText("Security");
    private static final By FALSE = By.id("settings[1].globalProperty.propertyValue_f");
    private static final By SAVE_PROPERTIES = By.id("saveButton");
    private static final By TRUE = By.id("settings[1].globalProperty.propertyValue_t");



    public void clickOnSetting(){ clickOn(SETTING);}
    public void clickOnRegistrationcore(){ clickOn(REGISTRATIONCORE);}
    public void enterFamilyList(String family){
        setText(FAMILY_NAME_LIST, family);
        FAMILY = family;
    }
    public void enterGivenList(String given){
        setText(GIVEN_NAME_LIST, given);
        GIVEN = given;
    }
    public void saveList(){ clickOn(SAVE_LIST);}
    public WebElement list(){
       return findElement(LIST);
    }
    public void clearFamilyList(){ findElement(FAMILY_NAME_LIST).clear();}
    public void clearGivenList(){ findElement(GIVEN_NAME_LIST).clear();}
    public void waitForList(){ waitForElement(LIST);}
    public void clickOnSecurity(){ clickOn(SECURITY);}
    public void chooseFalse(){ clickOn(FALSE);}
    public void saveProperties(){ clickOn(SAVE_PROPERTIES);}
    public void chooseTrue(){
        clickOn(TRUE);
        clickOn(SAVE_PROPERTIES);
    }



    @Override
    public String expectedUrlPath() {
        return URL_ROOT + "admin/maintenance/settings.list";
    }


}
