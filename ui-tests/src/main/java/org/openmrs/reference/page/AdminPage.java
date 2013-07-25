package org.openmrs.reference.page;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AdminPage extends AbstractBasePage{

    public AdminPage(WebDriver driver) {
        super(driver);
    }

    static final String advancedSettingsLink = "Advanced Settings";

    public void clickOnAdvancedSettings(){
        clickOn(By.linkText(advancedSettingsLink));
    }

    @Override
    public String expectedUrlPath() {
        return OPENMRS_PATH + "/admin/index.htm";
    }
}
