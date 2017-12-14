package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

public abstract class TransferPatientPage extends Page {

    private final Page parent;

    public TransferPatientPage(Page parent) {
        super(parent);
        this.parent = parent;
    }

    private static final String URL = "htmlformentryui/htmlform/enterHtmlFormWithStandardUi.page";
    private static final By SELECT_LOCATION = By.id("w5");
    private static final By SAVE = By.cssSelector(".submitButton");

    @Override
    public String getPageUrl() {
        return URL;
    }

    public Page confirm(String location){
        selectFrom(SELECT_LOCATION, location);
        clickOn(SAVE);
        return parent;
    }
}
