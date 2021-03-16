package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 *
 */
public abstract class AdminManagementPage extends Page {

    protected static final By HOME = By.id("homeNavLink");
    protected static final By RETIRE_REASON = By.name("retireReason");
    protected static final By DESCRIPTION = By.name("description");
    protected static final By DELETE = By.name("purge");
    protected static final By FOUND_ELEMENT = By.className("odd");
    static final By SEARCH_ELEMENT = By.id("inputNode");
    protected static By ADD;
    protected static By MANAGE;
    protected static By NAME = By.name("name");
    protected static By SAVE = By.name("save");
    protected static By RETIRE = By.name("retire");

    public AdminManagementPage(WebDriver driver) {
        super(driver);
    }

    public AdminManagementPage(Page parent) {
        super(parent);
    }

    public void clickOnHomeLink() throws InterruptedException {
        clickOn(HOME);
    }

    public void add() {
        clickOn(ADD);
    }


    public void manage() {
        clickOn(MANAGE);
    }

    public void save() throws InterruptedException {
        clickOn(SAVE);
    }


    public void clickOnFound() {
        clickOn(FOUND_ELEMENT);
    }

    public void findBySearch(String element) {
        findElement(SEARCH_ELEMENT).sendKeys(element);
        waitForElement(FOUND_ELEMENT);
        clickOnFound();
    }

    public boolean exists(String element) throws InterruptedException {
        findElement(SEARCH_ELEMENT).sendKeys(element);
        if (driver.getPageSource().contains("No matches found for")) {
            return false;
        } else {
            try {
                waitForElement(FOUND_ELEMENT);
            } catch (Exception e) {
                return false;
            }
            clickOnFound();
            return true;
        }
    }

    public void find(String item) {
        List<WebElement> elements = findElements(By.linkText(item));
        for (WebElement element : elements) {
            if (element.findElements(By.tagName("del")).isEmpty()) {
                element.click();
                break;
            }
        }
    }

    public boolean ifExists(String item) {
        try {
            List<WebElement> elements = findElements(By.linkText(item));
            for (WebElement element : elements) {
                if (element.findElements(By.tagName("del")).isEmpty()) {
                    element.click();
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void findRetired(String item) {
        List<WebElement> elements = findElements(By.linkText(item));
        for (WebElement element : elements) {
            if (!element.findElements(By.tagName("del")).isEmpty()) {
                element.click();
                break;
            }
        }
    }


    public void retire() throws InterruptedException {
        clickOn(RETIRE);
    }

    public void delete() throws InterruptedException {
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
        fillInField(findElement(DESCRIPTION), description);
    }


}
