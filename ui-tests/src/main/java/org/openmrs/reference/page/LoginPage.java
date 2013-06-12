package org.openmrs.reference.page;

import org.openqa.selenium.WebDriver;

public class LoginPage extends AbstractBasePage {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void loginAsAdmin() {
        login("admin", "Admin123");
    }
}
