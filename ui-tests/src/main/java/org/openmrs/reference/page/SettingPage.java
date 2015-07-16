package org.openmrs.reference.page;


import org.apache.commons.lang3.StringUtils;
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




    @Override
    public String expectedUrlPath() {
        return URL_ROOT + "admin/maintenance/settings.list";
    }


}
