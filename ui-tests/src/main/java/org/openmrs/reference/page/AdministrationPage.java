package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by tomasz on 10.07.15.
 */
public class AdministrationPage extends Page {


    public static String URL_PATH = "/admin/index.htm";

    private static By MANAGE_USERS = By.linkText("Manage Users");
    public AdministrationPage(WebDriver driver) {
        super(driver);
    }


    @Override
    public String expectedUrlPath() {
        return URL_ROOT + URL_PATH;
    }

    public void clickOnManageUsers() {
        findElement(MANAGE_USERS).click();
    }
}
