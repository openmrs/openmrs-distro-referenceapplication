package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by tomasz on 17.07.15.
 */
public abstract class AdminManagementPage extends AbstractBasePage {

    public AdminManagementPage(WebDriver driver) {
        super(driver);
    }
    protected static By ADD;
    protected static By MANAGE;

    protected static final By HOME = By.id("homeNavLink");
    protected static final By RETIRE_REASON = By.name("retireReason");
    protected static By NAME = By.name("name");
    protected static final By DESCRIPTION = By.name("description");

    protected static By SAVE = By.name("save");
    protected static final By DELETE = By.name("purge");
    protected static By RETIRE = By.name("retire");
    protected static final By FOUND_ELEMENT = By.className("odd");

    static final By SEARCH_ELEMENT = By.id("inputNode");

    public void clickOnHomeLink() {
        clickOn(HOME);
    }

    public void add() {
        clickOn(ADD);
    }


    public void manage() {
        clickOn(MANAGE);
    }

    public void save() {
        clickOn(SAVE);
    }

    public void findBySearch(String element){
        findElement(SEARCH_ELEMENT).sendKeys(element);
        waitForElement(FOUND_ELEMENT);
        clickOn(FOUND_ELEMENT);
    }

    public void find(String item){
        List<WebElement> elements = findElements(By.linkText(item));
        for(WebElement element : elements) {
            if (element.findElements(By.tagName("del")).isEmpty())
            {
                element.click();
                break;
            }
        }
    }


    public void findRetired(String item){
        List<WebElement> elements = findElements(By.linkText(item));
        for(WebElement element : elements) {
            if (!element.findElements(By.tagName("del")).isEmpty())
            {
                element.click();
                break;
            }
        }
    }


    public void retire() {
        clickOn(RETIRE);
    }

    public void delete() {
        clickOn(DELETE);
    }

    protected void fillInField(WebElement field, String text) {
        field.clear();
        field.sendKeys(text);
    }

    public void fillInRetireReason(String retireReason) {
        fillInField(findElement(RETIRE_REASON), retireReason);
    }

    public void fillInName(String name) {
        fillInField(findElement(NAME), name);
    }

    public void fillInDescription(String description) {
        fillInField(findElement(DESCRIPTION),description);
    }



}
