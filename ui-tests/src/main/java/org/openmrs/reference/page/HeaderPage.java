package org.openmrs.reference.page;

import org.openmrs.reference.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HeaderPage extends AbstractBasePage {

    public HeaderPage(WebDriver driver) {
        super(driver);
    }

    private String LOGOUT_LINK = "logout";
    private String HOME_Link = "icon-home small";

    public void clickOnHomeIcon(){
        clickOn(By.id(HOME_Link));
    }

    public void logOut() {
        clickOn(By.className(LOGOUT_LINK));
    }

}
