package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by tomasz on 15.07.15.
 */
public class RelationTypePage extends AdminManagementPage {

    static final By DELETE_RELATION_TYPE = By.name("purge");
    static final By RETIRE_RELATION_TYPE= By.name("retire");
    static final By A_IS_TO_B = By.name("aIsToB");
    static final By B_IS_TO_A = By.name("bIsToA");
    static final By DESCRIPTION = By.name("description");
    public RelationTypePage(WebDriver driver) {
        super(driver);
        MANAGE = By.linkText("Manage Relationship Types");
        ADD = By.linkText("Add Relationship Type");
        SAVE = By.name("save");
        RETIRE = By.name("retire");
    }




    public void fillInRelationTypeAIsToB(String name) {
        fillInField(findElement(A_IS_TO_B),name);
    }

    public void fillInRelationTypeBIsToA(String name) {
        fillInField(findElement(B_IS_TO_A),name);
    }

    public void createRelationType(String aistob,String bistoa, String description) throws InterruptedException {
        fillInRelationTypeAIsToB(aistob);
        fillInRelationTypeBIsToA(bistoa);
        fillInDescription(description);
        save();
    }

    @Override
    public String expectedUrlPath() {
        return URL_ROOT + "/admin/person/relationshipType.list";
    }

}
