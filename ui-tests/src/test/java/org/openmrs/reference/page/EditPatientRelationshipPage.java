package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

public class EditPatientRelationshipPage extends Page {

    private  HomePage homePage;
    private static final String  NAME = "John";
    private  static  final By SELECT_RELATIONSHIP_TYPE = By.id("relationship_type");
    private static final By PERSON_NAME = By.cssSelector("#relationship > p:nth-child(2) > input.person-typeahead.ng-pristine.ng-valid.ng-empty.ng-touched");
    private static final By NEXT_BUTTON = By.id("next-button");
    private static final By CONFIRM_BUTTON = By.id("registration-submit");   

    public EditPatientRelationshipPage(Page parent) {
        super(parent);
    }

    public void clickOnSelectRelationshipType() throws InterruptedException{
        clickOn(SELECT_RELATIONSHIP_TYPE);
        Select relationshipType = new Select(driver.findElement(By.id("relationship_type")));
        relationshipType.selectByVisibleText("Doctor");
        setTextToFieldNoEnter(PERSON_NAME, NAME);
        clickOn(NEXT_BUTTON);
        clickOn(CONFIRM_BUTTON);
    }

    @Override
    public String getPageUrl() {
        return "/registrationapp/editSection.page";
    }
}