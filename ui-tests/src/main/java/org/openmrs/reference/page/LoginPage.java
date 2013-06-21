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

    private String USERNAME_TEXTBOX = "username";
    private String PASSWORD_TEXTBOX = "password";
    private String LOGIN_BTN = "login-button";

    public void login(String user, String password) {
        setTextToField(USERNAME_TEXTBOX,user);
        setTextToField(PASSWORD_TEXTBOX, password);
        clickOn(By.id(LOGIN_BTN));
    }

    public void loginAsAdmin(){
        login(UserName,Password);
    }

	@Override
    public String expectedUrlPath() {
    	return "/openmrs/login.htm";
    }
}
