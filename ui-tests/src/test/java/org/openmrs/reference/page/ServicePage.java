package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

/**
 * Created by tomasz on 24.06.15.
 */
public class ServicePage extends Page {
	
    public static final String URL_PATH = "/appointmentschedulingui/appointmentType.page";
    public static final By NAME_FIELD = By.id("name-field");
    public static final By DURATION_FIELD = By.id("duration-field");
    public static final By DESCRIPTION_FIELD = By.id("description-field");
    public static final By SAVE_BUTTON = By.id("save-button");
    public static final By CANCEL_BUTTON = By.className("cancel");

    public ServicePage(Page parent) {
        super(parent);
    }

    @Override
    public String getPageUrl() {
        return URL_PATH;
    }

    public void setName(String name) {
        setTextToFieldNoEnter(NAME_FIELD, name);
    }

    public void setDuration(String duration) {
        setTextToFieldNoEnter(DURATION_FIELD, duration);
    }

    public void setDescription(String description) {
        setTextToFieldNoEnter(DESCRIPTION_FIELD, description);
    }

    public ManageServiceTypesPage save() {
        clickOn(SAVE_BUTTON);
        return new ManageServiceTypesPage(this);
    }

    public void cancel() {
        clickOn(CANCEL_BUTTON);
    }

    private String getValue(By field) {
        String text = getText(field);
        if (!text.isEmpty()) {
            return text;
        }
        return findElement(field).getAttribute("value");
    }

    public String getNameValue() {
        return getValue(NAME_FIELD);
    }

    public String getDurationValue() {
        return getValue(DURATION_FIELD);
    }

    public String getDescriptionValue() {
        return getValue(DESCRIPTION_FIELD);
    }
}
