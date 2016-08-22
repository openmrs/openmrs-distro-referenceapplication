package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

public class AddEditUserPage extends Page {

    private static final By CREATE_NEW_PERSON = By.id("createNewPersonButton");
    private static final By DELETE_USER = By.xpath("(//input[@name='action'])[3]");
    public static final String URL = "/admin/users/user.form";

    public AddEditUserPage(Page parent) {
        super(parent);
    }

    @Override
    public String getPageUrl() {
        return URL;
    }

    public CreateNewUserPage createNewPerson() {
        findElement(CREATE_NEW_PERSON).click();
        return new CreateNewUserPage(this);
    }

    public void unassignRole(String roleToUnassign) {
        waitForElement(By.id(roleToUnassign));
        WebElement roleElement = null;
        Long startTime = System.currentTimeMillis();
        while(true) {
            if((System.currentTimeMillis() - startTime) > 30000) {
                throw new TimeoutException("Couldn't uncheck a role in 30 seconds");
            }
            roleElement = findElement(By.id(roleToUnassign));
            if(roleElement.getAttribute("checked") != null && roleElement.getAttribute("checked").equals("true")) {
                roleElement.click();
            }
            else {
                break;
            }
        }
    }

    public void deleteUser(){ clickOn(DELETE_USER);}
}
