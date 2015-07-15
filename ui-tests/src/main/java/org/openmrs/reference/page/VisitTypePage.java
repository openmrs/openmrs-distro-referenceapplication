package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created by tomasz on 15.07.15.
 */
public class VisitTypePage extends AbstractBasePage {

    static final By MANAGE_VISIT_TYPES = By.linkText("Manage Visit Types");
    static final By ADD_VISIT_TYPE = By.linkText("Add Visit Type");
    //static final By RETIRE_PROVIDER = By.name("retireProviderButton");
    static final By SAVE_VISIT_TYPE = By.name("save");
    static final By DELETE_VISIT_TYPE = By.name("purge");
    static final By RETIRE_VISIT_TYPE= By.name("retire");
    static final By RETIRE_REASON = By.name("retireReason");
//    static final By IDENTIFIER = By.name("identifier");
    static final By NAME = By.name("name");
    static final By DESCRIPTION = By.name("description");
    static final By HOME = By.id("homeNavLink");
//    static final By SEARCH_ELEMENT = By.id("inputNode");
    public VisitTypePage(WebDriver driver) {
        super(driver);
    }


    public void manageVisitTypes() {
        clickOn(MANAGE_VISIT_TYPES);

    }

    public void addVisitType() {
        clickOn(ADD_VISIT_TYPE);
    }

    public void saveVisitType() {
        clickOn(SAVE_VISIT_TYPE);
    }

    public void findVisitType(String visitType){
        clickOn(By.linkText(visitType));
    }

    public void retireVisitType(String visitType, String retireReason) throws InterruptedException {
        findVisitType(visitType);
        fillInField(findElement(RETIRE_REASON),retireReason);
        clickWhenVisible(RETIRE_VISIT_TYPE);
    }
//    public void retireProvider() {
//        clickOn(RETIRE_PROVIDER);
//    }

    public void fillInField(WebElement field, String text) {
        field.clear();
        field.sendKeys(text);
    }

    public void clickOnHomeLink() {
        clickOn(HOME);
    }

//    public void fillInIdentifier(String text) {
//        fillInField(findElement(IDENTIFIER), text);
//    }

    public void fillInVisitType(String name,String description) {
        fillInField(findElement(NAME),name);
        fillInField(findElement(DESCRIPTION),description);
        saveVisitType();
    }

    @Override
    public String expectedUrlPath() {
        return URL_ROOT + "/admin/visits/visitType.list";
    }

}
