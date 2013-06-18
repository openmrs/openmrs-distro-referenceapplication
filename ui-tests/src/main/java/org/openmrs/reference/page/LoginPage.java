package org.openmrs.reference.page;

import org.openqa.selenium.WebDriver;

public class LoginPage extends AbstractBasePage {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void login(String user, String password) {
    	getElementById("username").sendKeys(user);
    	getElementById("password").sendKeys(password);
    	getElementById("login-button").click();
    }

    public void loginAsAdmin() {
        login("admin", properties.getUserPass());
    }
    
    @Override
    public String expectedTitle() {
    	return "Login";
    }
}
