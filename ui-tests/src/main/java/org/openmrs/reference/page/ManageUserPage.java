package org.openmrs.reference.page;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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
    private static final By USER_LINK = By.xpath("//table[@class='openmrsSearchTable']/tbody/tr/td/a");
    private static final By GENDER_FEMALE = By.id("F");
    private static final By GENDER_MALE = By.id("M");
    private static final String USER_RECORD = "//table[@class='openmrsSearchTable']/tbody/tr/td[text()='";
    private static final By USERNAME = By.name("username");
    private static final By ORGANIZATIONAL_DOCTOR = By.id("roleStrings.Organizational:Doctor");
    public String NAME;
    private static final By FIND_USER = By.name("name");
    private static final By DELETE_USER = By.xpath("(//input[@name='action'])[3]");
    private static final By ERROR_DEMOGRAPHIC = By.xpath("//form[@id='thisUserForm']/fieldset/span");
    private static final By ERROR_GENDER = By.xpath("//form[@id='thisUserForm']/fieldset/table/tbody/tr[5]/td[2]/span");
    private static final By ERROR_USER = By.xpath("//form[@id='thisUserForm']/fieldset[2]/table/tbody/tr[2]/td[2]/i");
    private static final By ERROR_PASSWORD = By.xpath("//form[@id='thisUserForm']/fieldset[2]/table/tbody/tr[3]/td[2]/i");
    private static final By USER = By.xpath("//div[@id='content']/div[2]/table/tbody/tr/td/a");
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

    public void fillInPersonName(String givenName, String familyName, String username, String password) {

        findElement(USERNAME).clear();
        findElement(USERNAME).sendKeys(username);findElement(PERSON_GIVEN_NAME).clear();
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
    public void enterUserMale(String givenName, String familyName,String username, String password) {

        findElement(PERSON_GIVEN_NAME).clear();
        findElement(PERSON_GIVEN_NAME).sendKeys(givenName);
        NAME = givenName;
        findElement(PERSON_FAMILY_NAME).clear();
        findElement(PERSON_FAMILY_NAME).sendKeys(familyName);
        clickOn(GENDER_MALE);
        findElement(USERNAME).clear();
        findElement(USERNAME).sendKeys(username);
        findElement(PASSWORD).clear();
        findElement(PASSWORD).sendKeys(password);
        findElement(CONFIRM).clear();
        findElement(CONFIRM).sendKeys(password);
            }

    public boolean userExists(String username) {
        clickOn(ACTION);
        try {
            return findElement(By.xpath(USER_RECORD+username+"']")) != null;
        } catch(Exception e) {
            return false;
        }
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

    public void assignRole(String roleToAssign) {
        waitForElement(By.id(roleToAssign));
        WebElement roleElement = null;
        Long startTime = System.currentTimeMillis();
        while(true) {
            if((System.currentTimeMillis() - startTime) > 30000) {
                throw new TimeoutException("Couldn't uncheck a role in 30 seconds");
            }
            roleElement = findElement(By.id(roleToAssign));
            if(roleElement.getAttribute("checked") != null && roleElement.getAttribute("checked").equals("true")) {
                roleElement.click();
            }
            else {
                break;
            }
        }
    }

    public void assignRolesToUser(String roleToUnassign, String roleToAssign, String user) throws InterruptedException {
        setText(FIND_USER, user);
        clickOn(ACTION);
        clickOn(USER_LINK);
        if(roleToUnassign != null) {
            unassignRole(roleToUnassign);
        }
        clickOn(By.id(roleToAssign));
        clickOn(SAVE_BUTTON);
    }
    public void chooseRole(){ clickOn(ORGANIZATIONAL_DOCTOR);}
    public void saveUser(){ clickOn(SAVE_BUTTON);}
    public void findUser(String user){
        setText(FIND_USER, user);
        NAME = user;
        clickOn(ACTION);
        findElement(USER).click();
    }
    public void removeUser(String user) {
        setText(FIND_USER, user);
        clickOn(ACTION);
        clickOn(USER_LINK);
        deleteUser();
    }
    public boolean userExist(String find){
        try {
            return findElement(By.xpath("//div[@id='content']/div[2]/table/tbody/tr/td[2]")).getText().contains(find);
        }
        catch (Exception ex) {
            return false;
        }
    }
    public void checkUser(String user){
        setText(FIND_USER, user);
        NAME = user;
        clickOn(ACTION);}


    public void deleteUser(){ clickOn(DELETE_USER);}
    public void changePassword(String password){
        findElement(PASSWORD).clear();
        findElement(PASSWORD).sendKeys(password);
        findElement(CONFIRM).clear();
        findElement(CONFIRM).sendKeys(password);
    }

    public void clickOnHomeLink() {
        clickOn(HOME);
    }
    public String errorDemographic(){
        return findElement(ERROR_DEMOGRAPHIC).getText();
    }
    public void enterGivenFamily(String givenName, String familyName){
        findElement(PERSON_GIVEN_NAME).clear();
        findElement(PERSON_GIVEN_NAME).sendKeys(givenName);
        findElement(PERSON_FAMILY_NAME).clear();
        findElement(PERSON_FAMILY_NAME).sendKeys(familyName);
    }
    public String errorGender(){
        return findElement(ERROR_GENDER).getText();
    }
    public String errorUser(){
        return findElement(ERROR_USER).getText();
    }
    public void clickOnFemale(){clickOn(GENDER_FEMALE);}
    public void enterUsernamePassword(String username, String password, String confirm){
        findElement(USERNAME).clear();
        findElement(USERNAME).sendKeys(username);
        findElement(PASSWORD).clear();
        findElement(PASSWORD).sendKeys(password);
        findElement(CONFIRM).clear();
        findElement(CONFIRM).sendKeys(confirm);
    }
    public String errorPassword(){
        return findElement(ERROR_PASSWORD).getText();
    }
    public void clickOnUser(){ clickOn(USER);}
}
