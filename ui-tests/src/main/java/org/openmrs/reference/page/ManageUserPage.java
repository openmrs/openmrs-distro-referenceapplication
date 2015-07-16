package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by tomasz on 10.07.15.
 */
public class ManageUserPage extends AbstractBasePage {


    public static String URL_PATH = "/admin/users/users.list";

    private static final By ADD_USER = By.linkText("Add User");
    private static final By CREATE_NEW_PERSON = By.id("createNewPersonButton");
    private static final By PERSON_GIVEN_NAME = By.name("person.names[0].givenName");
    private static final By PERSON_FAMILY_NAME = By.name("person.names[0].familyName");
    private static final By PASSWORD = By.name("userFormPassword");
    private static final By CONFIRM = By.name("confirm");
    private static final By SAVE_BUTTON = By.id("saveButton");
    private static final By HOME = By.id("homeNavLink");
    private static final By ACTION = By.name("action") ;
    private static final By USER_LINK = By.linkText("34-9");
    private static final By GENDER_FEMALE = By.id("F");
    private static final By SUPER_NURSE_USER_RECORD = By.xpath("//table[@class='openmrsSearchTable']/tbody/tr/td[text()='super_nurse']");
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

    public void createNewPerson() {
        findElement(CREATE_NEW_PERSON).click();
    }

    public void fillInPersonName(String givenName, String familyName, String password) {

        findElement(PERSON_GIVEN_NAME).clear();
        findElement(PERSON_GIVEN_NAME).sendKeys(givenName);
        findElement(PERSON_FAMILY_NAME).clear();
        findElement(PERSON_FAMILY_NAME).sendKeys(familyName);
        clickOn(GENDER_FEMALE);
        findElement(PASSWORD).clear();
        findElement(PASSWORD).sendKeys(password);
        findElement(CONFIRM).clear();
        findElement(CONFIRM).sendKeys(password);
        clickOn(SAVE_BUTTON);

    }

    public boolean userSuperNurseExists() {
        clickOn(ACTION);
        try {
            return findElement(SUPER_NURSE_USER_RECORD) != null;
        } catch(Exception e) {
            return false;
        }
    }

    public void assignRolesToUserSuperNurse(String roleToUnassign, String roleToAssign) throws InterruptedException {
        clickOn(ACTION);
        clickOn(USER_LINK);
        if(roleToUnassign != null) {
            waitForElement(By.id(roleToUnassign));
            clickOn(By.id(roleToUnassign));
            Thread.sleep(200);
        }
        clickOn(By.id(roleToAssign));
        clickOn(SAVE_BUTTON);
    }


    public void clickOnHomeLink() {
        clickOn(HOME);
    }
}
