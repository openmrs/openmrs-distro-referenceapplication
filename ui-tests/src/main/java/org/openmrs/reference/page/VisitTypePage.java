package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

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
//        clickOn(By.xpath("//table/tr/td/a[text()=normalize-space('\"+visitType+\"')]"));
        List<WebElement> elements = findElements(By.linkText(visitType));
        for(WebElement element : elements) {
            if (element.findElements(By.tagName("del")).isEmpty())
            {
                element.click();
                break;
            }
        }
    }

    
    public void retireVisitType() {
        clickOn(RETIRE_VISIT_TYPE);
    }

    private void fillInField(WebElement field, String text) {
        field.clear();
        field.sendKeys(text);
    }

    public void clickOnHomeLink() {
        clickOn(HOME);
    }

    public void fillInRetireReason(String retireReason) {
        fillInField(findElement(RETIRE_REASON),retireReason);
    }

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
