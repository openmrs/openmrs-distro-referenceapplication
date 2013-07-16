package org.openmrs.reference.page;

import org.openmrs.reference.helper.TestProperties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends AbstractBasePage {

    protected TestProperties properties = new TestProperties();
    private String UserName;
    private String Password;


    public LoginPage(WebDriver driver) {
        super(driver);
        UserName = properties.getUserName();
        Password = properties.getPassword();
    }

    private String userNameTxtBox = "username";
    private String passwordTxtBox = "password";
    private String loginBtn = "login-button";

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
