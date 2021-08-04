package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

public class RegistrationSummaryPage extends Page {
	
    private static final By RELATIONSHIP = By.id("relationships-info-edit-link");
    private static final By DEMOGRAPHICS = By.id("demographics-edit-link");
    private  static final By CONTACT_INFO = By.id("contactInfo-edit-link");

    public RegistrationSummaryPage(Page parent) {
        super(parent);
    }

    public EditPatientRelationshipPage goToEditPatientRelationship(){
        clickOnLast(RELATIONSHIP);
        return new EditPatientRelationshipPage(this);
    }

    @Override
    public String getPageUrl() {
        return "/registrationapp/registrationSummary.page";
    }
	
}
