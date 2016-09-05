package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

public class AdmitToInpatientPage extends Page {

    private static final String URL = "htmlformentryui/htmlform/enterHtmlFormWithStandardUi.page";
    private static final By SELECT_LOCATION = By.id("w5");
    private static final By SAVE = By.cssSelector(".submitButton");

    public AdmitToInpatientPage(Page parent) {
        super(parent);
    }

    @Override
    public String getPageUrl() {
        return URL;
    }

    public PatientVisitsDashboardPage confirm(String location){
        selectFrom(SELECT_LOCATION, location);
        clickOn(SAVE);
        return new PatientVisitsDashboardPage(this);
    }
}
