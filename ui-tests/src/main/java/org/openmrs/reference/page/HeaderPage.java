package org.openmrs.reference.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HeaderPage extends AbstractBasePage {

    public HeaderPage(WebDriver driver) {
        super(driver);
    }

    public void logOut() {
        clickOn(By.className("logout"));
    }

	@Override
    public String expectedTitle() {
	    return null;
    }
}
