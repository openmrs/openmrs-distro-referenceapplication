/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

public class ManageLocationAttributeTypesPage extends Page {

    protected static final String PAGE_URL = "adminui/metadata/locations/manageLocationAttributeTypes.page";
    private static final By ADD_LOCATION_ATTRIBUTE_TYPE_BUTTON = By.cssSelector("input.button");
    private static final By ATTRIBUTE_TYPE_NAME_FIELD = By.cssSelector("#name-field");
    private static final By ATTRIBUTE_TYPE_DESCRIPTION_FIELD = By.cssSelector("#description-field");
    private static final By MIN_OCCURS_FIELD = By.cssSelector("#minOccurs-field");
    private static final By MAX_OCCURS_FIELD = By.cssSelector("#maxOccurs-field");
    private static final By DATA_TYPE_FIELD = By.cssSelector("#datatypeClassname-field");
    private static final By DATA_TYPE_CONFIGURATION_FIELD = By.cssSelector("#datatypeConfig-field");
    private static final By PREFERRED_HANDLER_FIELD = By.cssSelector("#preferredHandlerClassname-field");
    private static final By HANDLER_CONFIGURATION_FILED = By.cssSelector("#handlerConfig-field");
    private static final By SAVE_BUTTON = By.cssSelector("#save-button");
    private static final By CANCEL_BUTTON = By.cssSelector("#locationAttributeTypeForm input.cancel");
    private static final By EDIT_LOCATION_ATTRIBUTE_TYPE_ICON = By.cssSelector("i.icon-pencil.edit-action");
    private static final By DELETE_LOCATION_ICON = By.cssSelector("i.icon-trash.delete-action.right");
    private static final By CONFIRM_DELETE_LOCATION_BUTTON = By.cssSelector("#purgeLocationAttributeTypeForm button.confirm.right");
    private static final By CONFIRM_RETIRE_LOCATION_BUTTON = By.cssSelector("#retireLocationAttributeTypeForm button.confirm.right");
    private static final By REASON_TO_RETIRE_LOCATION_BUTTON = By.cssSelector("#retireLocationAttributeTypeForm input[type=text]");
    private static final By CANCEL_RETIRE_LOCATION_BUTTON = By.cssSelector("#retireLocationAttributeTypeForm button.cancel");
    private static final By CANCEL_DELETE_LOACTION_BUTTON = By.cssSelector("#purgeLocationAttributeTypeForm button.cancel");

    public ManageLocationAttributeTypesPage(Page parent) {
        super(parent);
    }

    @Override
    public String getPageUrl() {
        return PAGE_URL;
    }
    
    public void goToAddNewLocationAttributeTypeForm() {
        clickOn(ADD_LOCATION_ATTRIBUTE_TYPE_BUTTON);
    }
   
    public void goToEditLocationAttributeTypeForm() {
        clickOn(EDIT_LOCATION_ATTRIBUTE_TYPE_ICON);
    }
    
    public void enterAttributeTypeName(String locationName) {
        findElement(ATTRIBUTE_TYPE_NAME_FIELD).clear();
        findElement(ATTRIBUTE_TYPE_NAME_FIELD).sendKeys(locationName);
    }

    public void enterAttributeTypeDescription(String locationDescription) {
        findElement(ATTRIBUTE_TYPE_DESCRIPTION_FIELD).clear();
        findElement(ATTRIBUTE_TYPE_DESCRIPTION_FIELD).sendKeys(locationDescription);
    }
    
    public void enterMinimumOccurs(String value) {
        setText(MIN_OCCURS_FIELD, value);
    }
   
    public void enterMaximumOccurs(String value) {
        setText(MAX_OCCURS_FIELD, value);
    }
  
    public void selectDatatype(String datatype) {
        selectFrom(DATA_TYPE_FIELD, datatype);
    }
  
    public void enterDatatypeConfiguration(String datatypeConfiguration) {
        findElement(DATA_TYPE_CONFIGURATION_FIELD).clear();
        findElement(DATA_TYPE_CONFIGURATION_FIELD).sendKeys(datatypeConfiguration);
    }
   
    public void selectPreferredHandler(String preferredHandler) {
        selectFrom(PREFERRED_HANDLER_FIELD, preferredHandler);
    }
   
    public void enterHandlerConfiguration(String handlerConfiguarion) {
        selectFrom(HANDLER_CONFIGURATION_FILED, handlerConfiguarion);
    }
  
    public void clickOnSaveButton() {
        clickOn(SAVE_BUTTON);
    }
 
    public void clickOnCancelButton() {
        clickOn(CANCEL_BUTTON);
    }
    
    public void purgeLocation() {
        clickOn(DELETE_LOCATION_ICON);
        waitForElement(CONFIRM_DELETE_LOCATION_BUTTON);
        clickOn(CONFIRM_DELETE_LOCATION_BUTTON);
    }

    public void cancelPurgeLocation() {
        clickOn(CANCEL_DELETE_LOACTION_BUTTON);
    }
    
    public void retireLocation(String name) {
        clickOn(By.xpath("//tr/td[preceding-sibling::td[contains(text(), '" + name + "')]]/i[@class='icon-remove delete-action']"));
        waitForElement(CONFIRM_RETIRE_LOCATION_BUTTON);
        clickOn(CONFIRM_RETIRE_LOCATION_BUTTON);
    }

    public void assertRetired(String name, String reason) {
        try {
            findElement(By.xpath("//tr/td[preceding-sibling::td[contains(text(), '" + name + "')]]/i[@class='icon-reply edit-action']"));
            findElement(REASON_TO_RETIRE_LOCATION_BUTTON).clear();
            findElement(REASON_TO_RETIRE_LOCATION_BUTTON).sendKeys(reason);
        } catch (TimeoutException e) {
            throw new RuntimeException("Couldn't find restore button, failed to retire location " + name);
        }
    }
    
    public void cancelRetireLocation() {
        clickOn(CANCEL_RETIRE_LOCATION_BUTTON);
    }
}
