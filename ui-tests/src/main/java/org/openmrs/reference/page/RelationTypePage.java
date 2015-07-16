package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by tomasz on 15.07.15.
 */
public class RelationTypePage extends AbstractBasePage {

    static final By MANAGE_RELATION_TYPES = By.linkText("Manage Relationship Types");
    static final By ADD_RELATION_TYPE = By.linkText("Add Relationship Type");
    static final By SAVE_RELATION_TYPE = By.name("save");
    static final By DELETE_RELATION_TYPE = By.name("purge");
    static final By RETIRE_RELATION_TYPE= By.name("retire");
    static final By RETIRE_REASON = By.name("retireReason");
    static final By A_IS_TO_B = By.name("aIsToB");
    static final By B_IS_TO_A = By.name("bIsToA");
    static final By DESCRIPTION = By.name("description");
    static final By HOME = By.id("homeNavLink");
    public RelationTypePage(WebDriver driver) {
        super(driver);
    }


    public void manageRelationTypes() {
        clickOn(MANAGE_RELATION_TYPES);

    }

    public void addRelationType() {
        clickOn(ADD_RELATION_TYPE);
    }

    public void saveRelationType() {
        clickOn(SAVE_RELATION_TYPE);
    }

    public void findRelationType(String relationType){
        List<WebElement> elements = findElements(By.linkText(relationType));
        for(WebElement element : elements) {
            if (element.findElements(By.tagName("del")).isEmpty())
            {
                element.click();
                break;
            }
        }
    }

    public void findRetiredRelationType(String relationType){
        List<WebElement> elements = findElements(By.linkText(relationType));
        for(WebElement element : elements) {
            if (!element.findElements(By.tagName("del")).isEmpty())
            {
                element.click();
                break;
            }
        }
    }
    
    public void retireRelationType() {
        clickOn(RETIRE_RELATION_TYPE);
    }

    public void deleteRelationType() {
        clickOn(DELETE_RELATION_TYPE);
    }
    private void fillInField(WebElement field, String text) {
        field.clear();
        field.sendKeys(text);
    }

    public void clickOnHomeLink() {
        clickOn(HOME);
    }

    public void fillInRetireReason(String retireReason) {
        fillInField(findElement(RETIRE_REASON), retireReason);
    }

    public void fillInRelationTypeAIsToB(String name) {
        fillInField(findElement(A_IS_TO_B),name);
    }

    public void fillInRelationTypeBIsToA(String name) {
        fillInField(findElement(B_IS_TO_A),name);
    }

    public void fillInRelationTypeDescription(String description) {
        fillInField(findElement(DESCRIPTION),description);
    }

    public void createRelationType(String aistob,String bistoa, String description) {
        fillInRelationTypeAIsToB(aistob);
        fillInRelationTypeBIsToA(bistoa);
        fillInRelationTypeDescription(description);
        saveRelationType();
    }

    @Override
    public String expectedUrlPath() {
        return URL_ROOT + "/admin/person/relationshipType.list";
    }

}
