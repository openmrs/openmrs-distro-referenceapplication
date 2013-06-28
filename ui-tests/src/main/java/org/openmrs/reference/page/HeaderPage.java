package org.openmrs.reference.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HeaderPage extends AbstractBasePage {

    public HeaderPage(WebDriver driver) {
        super(driver);
    }

    private String LOGOUT_LINK = "logout";
    private String HOME_Icon = "icon-home small";
    private String HOME_Link = "Home";

    public void clickOnHomeIcon(){
        clickOn(By.id(HOME_Icon));
    }

    public void clickOnHomeLink(){
        clickOn(By.linkText(HOME_Link));
    }

    public void logOut() {
        clickOn(By.className(LOGOUT_LINK));
    }

	@Override
    public String expectedUrlPath() {
	    return null;
    }

}
