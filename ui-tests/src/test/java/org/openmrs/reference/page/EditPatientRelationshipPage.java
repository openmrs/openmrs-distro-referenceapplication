package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

public class EditPatientRelationshipPage extends Page {
    private  static  final By SELECT_RELATIONSHIP_TYPE = By.id("relationship_type");

    public EditPatientRelationshipPage(RegistrationSummaryPage parent) {
        super(parent);
    }

    public void clickOnSelectRelationshipType() throws InterruptedException{
        clickOn(SELECT_RELATIONSHIP_TYPE);
   }

    @Override
    public String getPageUrl() {
        return "/registrationapp/editSection.page";
    }
}
