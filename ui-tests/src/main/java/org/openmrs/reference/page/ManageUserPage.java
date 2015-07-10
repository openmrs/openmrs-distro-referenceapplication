package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by tomasz on 10.07.15.
 */
public class ManageUserPage extends AbstractBasePage {


    public static String URL_PATH = "/admin/users/users.list";

    private static By ADD_USER = By.linkText("Add User");
    public ManageUserPage(WebDriver driver) {
        super(driver);
    }


    @Override
    public String expectedUrlPath() {
        return URL_ROOT + URL_PATH;
    }

    public void clickOnAddUser() {
        findElement(ADD_USER).click();
    }
}
