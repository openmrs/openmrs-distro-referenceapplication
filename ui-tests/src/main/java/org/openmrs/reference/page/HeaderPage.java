package org.openmrs.reference.page;

import org.openmrs.reference.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HeaderPage extends AbstractBasePage {

    public HeaderPage(WebDriver driver) {
        super(driver);
    }

    public void logOut() {
        clickOn(By.className("logout"));
    }
}
