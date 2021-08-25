package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

public class RegistrationSummaryPage extends Page {

    private static final By RELATIONSHIP_LINK = By.id("relationships-info-edit-link");

    public RegistrationSummaryPage(Page parent) {
        super(parent);
    }

    public EditPatientRelationshipPage goToEditPatientRelationship(){
        clickOnLast(RELATIONSHIP_LINK);
        return new EditPatientRelationshipPage(this);
    }

    @Override
    public String getPageUrl() {
        return "/registrationapp/registrationSummary.page";
    }
}
