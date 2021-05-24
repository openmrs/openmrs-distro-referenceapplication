package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HeaderPage extends Page {

    static final String HOME_ICON = "logo";
    static final String HOME_LINK_TEXT = "Home";
    static final By LOGOUT = By.className("logout");
    
    public HeaderPage(WebDriver driver) {
        super(driver);
    }

    public void clickOnHomeIcon() throws InterruptedException {
        clickOn(By.className(HOME_ICON));
    }

    // TODO This is unused, do we really need it?
    public void clickOnHomeLink() {
        clickOn(By.linkText(HOME_LINK_TEXT));
    }

    public void logOut() throws InterruptedException {
        waiter.until(ExpectedConditions.visibilityOfElementLocated(LOGOUT));
        clickOn(LOGOUT);
    }

    @Override
    public String getPageUrl() {
        return null;
    }

}
