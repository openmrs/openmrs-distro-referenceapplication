package org.openmrs.reference.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by tomasz on 15.07.15.
 */
public class VisitTypePage extends AdminManagementPage {

    public VisitTypePage(WebDriver driver) {
        super(driver);
        MANAGE = By.linkText("Manage Visit Types");
        ADD = By.linkText("Add Visit Type");
        SAVE = By.name("save");
        NAME = By.name("name");
        RETIRE = By.name("retire");
    }
    private static final By ERROR = By.cssSelector("span.error");



    public void createVisitType(String name,String description) throws InterruptedException {
        fillInName(name);
        fillInDescription(description);
        save();
    }
    public void waitForError(){
        waitForElement(ERROR);
    }

    @Override
    public String getPageUrl() {
        return "/admin/visits/visitType.list";
    }

}
