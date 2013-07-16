package org.openmrs.reference.page;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AdminPage extends AbstractBasePage{

    public AdminPage(WebDriver driver) {
        super(driver);
    }

    private String manageModuleLink = "Manage Modules";
    private String advancedSettingsLink = "Advanced Settings";

    public void clickOnManageModules(){
        clickOn(By.linkText(manageModuleLink));
    }

    public void clickOnAdvancedSettings(){
        clickOn(By.linkText(advancedSettingsLink));
    }

    @Override
    public String expectedUrlPath() {
        return "/openmrs/admin/index.htm";
    }
}
