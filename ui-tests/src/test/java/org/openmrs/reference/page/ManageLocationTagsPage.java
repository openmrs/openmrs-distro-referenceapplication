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

public class ManageLocationTagsPage extends Page {
   
    protected static final String PAGE_URL = "adminui/metadata/locations/manageLocationTags.page";
    private static final By ADD_NEW_LOCATION_TAG_BUTTON = By.cssSelector("input.button");
    private static final By EDIT_LOCATION_LINK = By.cssSelector("i.icon-pencil.edit-action");
    private static final By LOCATION_TAG_NAME_FIELD = By.cssSelector("input#name-field");
    private static final By LOCATION_TAG_DESCRIPTION_FIELD = By.cssSelector("#description-field");
    private static final By DELETE_LOCATION_ICON = By.cssSelector("i.icon-trash.delete-action.right");
    private static final By RETIRE_LOCATION_ICON = By.cssSelector("i.icon-remove.delete-action");
    private static final By CONFIRM_RETIRE_LOCATION_BUTTON = By.cssSelector("#retireLocationTagForm button.confirm.right");
    private static final By CANCEL_RETIRE_LOCATION_ICON = By.cssSelector("#retireLocationTagForm button.cancel");
    private static final By CONFIRM_DELETE_LOCTION_BUTTON = By.cssSelector("#purgeLocationTagForm button.confirm.right");
    private static final By CANCEL_DELETE_LOCATION_BUTTON = By.cssSelector("#purgeLocationTagForm button.cancel");
    private static final By SAVE_LOCATION_BUTTON = By.cssSelector("#save-button");
    private static final By CANCEL_LOCATION_BUTTON = By.cssSelector("input.cancel");
    
    public ManageLocationTagsPage(Page parent) {
        super(parent);
    }

    @Override
    public String getPageUrl() {
        return PAGE_URL;
    }

    public void goToAddNewLocationTagForm() {
        clickOn(ADD_NEW_LOCATION_TAG_BUTTON);
    }
    
    public void goToEditLocationTagForm() {
    	clickOn(EDIT_LOCATION_LINK);
    }
    
    public void enterLocationTagName(String locationName) {
        findElement(LOCATION_TAG_NAME_FIELD).clear();
        findElement(LOCATION_TAG_NAME_FIELD).sendKeys(locationName);
    }
  
    public void enterLocationTagDescription(String locationDescription) {
        findElement(LOCATION_TAG_DESCRIPTION_FIELD).clear();
        findElement(LOCATION_TAG_DESCRIPTION_FIELD).sendKeys(locationDescription);
    }
    
    public void saveLocationTag() {
        clickOn(SAVE_LOCATION_BUTTON);
    }
 
    public void clickOnCancelButton() {
        clickOn(CANCEL_LOCATION_BUTTON);
    }

    public void deleteLocationTag() {
        clickOn(DELETE_LOCATION_ICON);
        waitForElement(CONFIRM_DELETE_LOCTION_BUTTON);
        clickOn(CONFIRM_DELETE_LOCTION_BUTTON);
    }
  
    public void cancelDeleteLocation() {
        clickOn(CANCEL_DELETE_LOCATION_BUTTON);
    }
    
    public void retireLocation() {
        clickOn(RETIRE_LOCATION_ICON);
        waitForElement(CONFIRM_RETIRE_LOCATION_BUTTON);
        clickOn(CONFIRM_RETIRE_LOCATION_BUTTON);
    }
    
    public void cancelRetireLocation() {
        clickOn(CANCEL_RETIRE_LOCATION_ICON);
        waitForElement(CONFIRM_RETIRE_LOCATION_BUTTON);
        clickOn(CONFIRM_RETIRE_LOCATION_BUTTON);
    }
}
