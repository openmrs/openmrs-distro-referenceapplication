package org.openmrs.reference.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by tomasz on 15.07.15.
 */
public class ManagePersonPage extends AdminManagementPage {

    static final By AGE = By.id("age");
    static final By DESCRIPTION = By.name("description");
    static final By CREATE_PERSON = By.cssSelector("input[type=\"submit\"]");
    static final By FAMILY_NAME = By.name("names[0].familyName");
    static final By GIVEN_NAME = By.name("names[0].givenName");
    public ManagePersonPage(WebDriver driver) {
        super(driver);
        ADD = By.linkText("Create Person");
        MANAGE = By.linkText("Manage Persons");
        SAVE = By.id("saveButton");
        RETIRE = By.cssSelector("fieldset > input[name=\"action\"]");
        NAME = By.name("addName");
    }


    public void fillInAge(String age) {
        fillInField(findElement(AGE),age);
    }

    public void fillInFamilyName(String familyName) {
        fillInField(findElement(FAMILY_NAME),familyName);
    }

    public void fillInGivenName(String givenName) {
        fillInField(findElement(GIVEN_NAME),givenName);
    }

    public void createPerson(String name, String age, char gender) {
        fillInName(name);
        fillInAge(age);
        clickOn(By.id("gender-"+gender));
        create();
    }

    public void create() {
        clickOn(CREATE_PERSON);
    }
    @Override
    public String expectedUrlPath() {
        return URL_ROOT + "/admin/person/index.htm";
    }

}
