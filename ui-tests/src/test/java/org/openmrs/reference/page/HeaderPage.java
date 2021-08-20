package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HeaderPage extends Page {

    static final String HOME_ICON = "logo";
    static final By LOGOUT = By.cssSelector(".logout a");
    
    public HeaderPage(WebDriver driver) {
        super(driver);
    }

    public void clickOnHomeIcon() throws InterruptedException {
        clickOn(By.className(HOME_ICON));
    }

    public void logOut() throws InterruptedException {
        clickOn(LOGOUT);
    }

    @Override
    public String getPageUrl() {
        return null;
    }
}
