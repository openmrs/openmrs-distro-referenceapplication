package org.openmrs.reference.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HeaderPage extends AbstractBasePage {

    public HeaderPage(WebDriver driver) {
        super(driver);
    }

    private String logoutLink = "//a[@href='/openmrs/logout']";
    private String homeIcon = "icon-home small";
    private String homeLink = "Home";

    public void clickOnHomeIcon(){
        clickOn(By.id(homeIcon));
    }

    public void clickOnHomeLink(){
        clickOn(By.linkText(homeLink));
    }

    public void logOut() {
        clickOn(By.xpath(logoutLink));
    }

	@Override
    public String expectedUrlPath() {
	    return null;
    }

}
