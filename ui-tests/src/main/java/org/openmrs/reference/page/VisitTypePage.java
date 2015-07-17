package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by tomasz on 15.07.15.
 */
public class VisitTypePage extends AdminManagementPage {

    public VisitTypePage(WebDriver driver) {
        super(driver);
        MANAGE = By.linkText("Manage Visit Types");
        ADD = By.linkText("Add Visit Type");
        SAVE = By.name("save");
    }




    public void createVisitType(String name,String description) {
        fillInName(name);
        fillInDescription(description);
        save();
    }

    @Override
    public String expectedUrlPath() {
        return URL_ROOT + "/admin/visits/visitType.list";
    }

}
