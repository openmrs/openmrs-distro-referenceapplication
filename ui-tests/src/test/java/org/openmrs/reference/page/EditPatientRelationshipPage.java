package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

public class EditPatientRelationshipPage extends Page {
	
    private  HomePage homePage;
    private static String name = "John";
    private  static  final By SELECT_RELATIONSHIP_TYPE = By.id("relationship_type");
    private static final By PERSON_NAME = By.xpath("[@id=\"relationship\"]/p[2]/input[1]");
    private static final By NEXT_BUTTON = By.id("next-button");
    private static final By CONFIRM_BUTTON = By.id("registration-submit");   

    public EditPatientRelationshipPage(Page parent) {
        super(parent);
    }

    public void clickOnSelectRelationshipType() throws InterruptedException{
        clickOn(SELECT_RELATIONSHIP_TYPE);
        Select relationshipType = new Select(driver.findElement(By.id("relationship_type")));
        relationshipType.selectByVisibleText("Doctor");
        setTextToFieldNoEnter(PERSON_NAME, name);
        clickOn(NEXT_BUTTON);
        clickOn(CONFIRM_BUTTON);
    }
    
    @Override
    public String getPageUrl() {
        return "/registrationapp/editSection.page";
    }
}  
