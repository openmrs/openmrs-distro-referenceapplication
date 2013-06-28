package org.openmrs.reference.page;


import org.openmrs.reference.helper.PatientGenerator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AdminPage extends AbstractBasePage{

    public AdminPage(WebDriver driver) {
        super(driver);
    }

    private String MANAGE_MODULE_LINK = "Manage Modules";
    private String ADVANCED_SETTINGS_LINK = "Advanced Settings";

    public void clickOnManageModules(){
        clickOn(By.linkText(MANAGE_MODULE_LINK));
    }

    public void clickOnAdvancedSettings(){
        clickOn(By.linkText(ADVANCED_SETTINGS_LINK));
    }

    @Override
    public String expectedUrlPath() {
        return "/openmrs/admin/index.htm";
    }
}
