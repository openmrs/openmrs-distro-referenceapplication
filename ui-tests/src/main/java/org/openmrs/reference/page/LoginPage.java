package org.openmrs.reference.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends AbstractBasePage {

	static final String userNameTxtBox = "username";
	static final String passwordTxtBox = "password";
	static final String loginBtn = "login-button";
	
    private String UserName;
    private String Password;

    public LoginPage(WebDriver driver) {
        super(driver);
        UserName = properties.getUserName();
        Password = properties.getPassword();
    }

    public void login(String user, String password) {
        setTextToField(By.id(userNameTxtBox),user);
        setTextToField(By.id(passwordTxtBox), password);
        clickOn(By.id(loginBtn));
    }

    public void loginAsAdmin(){
        login(UserName,Password);
    }

	@Override
    public String expectedUrlPath() {
    	return "/openmrs/login.htm";
    }
}
