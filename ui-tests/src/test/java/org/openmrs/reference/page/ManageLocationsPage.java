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

public class ManageLocationsPage extends Page {

    protected static final String PAGE_URL = "/adminui/metadata/locations/manageLocations.page";
    private static final By EDIT_LOCATION_LINK = By.cssSelector("i.icon-pencil.edit-action");
    public static final By CONFIRM_PURGE_BUTTON = By.cssSelector("#adminui-purge-location-dialog button.confirm.right");
    public static final By CONFIRM_RETIRE_BUTTON = By.cssSelector("#adminui-retire-location-dialog > div.dialog-content > form > button.confirm.right");
    private static final By LOCATION_TAG_BUTTON = By.cssSelector("#locationForm fieldset table tbody tr:first-child td:first-child input[type=\"checkbox\"]");
    private static final By ADD_NEW_LOCATION_BUTTON = By.xpath("//*[@id='content']/a");
    private static final By LOCATION_NAME_FIELD = By.id("name-field");  
    private static final By ADD_EDIT_LOCATION = By.cssSelector("#content a[href='/openmrs/" + AddEditLocationPage.PAGE_URL + "']");
    private static final By LOCATION_DESCRIPTION_FIELD = By.cssSelector("#description-field");
    private static final By LOCATION_ADDRESS_FIELD_1 = By.cssSelector("#address1-field");
    private static final By LOCATION_ADDRESS_FIELD_2 = By.cssSelector("#address2-field");
    private static final By CITY_FIELD = By.cssSelector("#cityVillage-field");
    private static final By STATE_FIELD = By.cssSelector("#stateProvince-field");
    private static final By COUNTRY_FIELD = By.cssSelector("#country-field");
    private static final By POSTAL_CODE_FIELD = By.cssSelector("#postalCode-field");
    private static final By PARENT_LOCATION_FIELD = By.cssSelector("#postalCode-field");
    private static final By SAVE_BUTTON = By.id("save-button");
    private static final String PURGE_LOCATION_SELECTOR_TMPL = "list-locations > tbody > tr > i[onclick*='purgeLocation('%s', *)']";

    public ManageLocationsPage(Page parent) {
        super(parent);
    }

    @Override
    public String getPageUrl() {
        return PAGE_URL;
    }

    public AddEditLocationPage goToAddLocation() {
        clickOn(ADD_EDIT_LOCATION);
        return new AddEditLocationPage(this);
    }

    public void goToAddNewLocation() {
        clickOn(ADD_NEW_LOCATION_BUTTON);
    }

    public void goToEditLocationForm() {
    	clickOn(EDIT_LOCATION_LINK);
    }
    
    public void enterLocationName(String name) {     
        findElement(LOCATION_NAME_FIELD).clear();
        findElement(LOCATION_NAME_FIELD).sendKeys(name);
    }

    public void enterLocationDescription(String description) {
        findElement(LOCATION_DESCRIPTION_FIELD).clear();
        findElement(LOCATION_DESCRIPTION_FIELD).sendKeys(description);
    }

    public void enterAddress1(String address) {
        findElement(LOCATION_ADDRESS_FIELD_1).clear();
        findElement(LOCATION_ADDRESS_FIELD_1).sendKeys(address);
    }
    
    public void enterAddress2(String address) {
        findElement(LOCATION_ADDRESS_FIELD_2).clear();
        findElement(LOCATION_ADDRESS_FIELD_2).sendKeys(address);
    }
    
    public void enterState(String stateName) {
        findElement(STATE_FIELD).clear();
        findElement(STATE_FIELD).sendKeys(stateName);
    }

    public void enterCity(String cityName) {
        findElement(CITY_FIELD).clear();
        findElement(CITY_FIELD).sendKeys(cityName);
    }
    
    public void enterCountry(String countryName) {
        findElement(COUNTRY_FIELD).clear();
        findElement(COUNTRY_FIELD).sendKeys(countryName);
    }
    
    public void enterPostalCode(String postalCode) {
        findElement(POSTAL_CODE_FIELD).clear();
        findElement(POSTAL_CODE_FIELD).sendKeys(postalCode);
    }
    
    public void selectParentLocation(String locationName) {
        findElement(PARENT_LOCATION_FIELD).clear();
        findElement(PARENT_LOCATION_FIELD).sendKeys(locationName);
    }

    public void selectLocationTag() {
        clickOn(LOCATION_TAG_BUTTON);
    }

    public void saveLocation() {
        clickOn(SAVE_BUTTON);
    }
    
    public void purgeLocation(String name) {
        clickOn(By.xpath("//tr/td[preceding-sibling::td[contains(text(), '" + name + "')]]/i[@class='icon-trash delete-action']"));
        waitForElement(CONFIRM_PURGE_BUTTON);
        clickOn(CONFIRM_PURGE_BUTTON);
    }

    public void retireLocation(String name) {
        clickOn(By.xpath("//tr/td[preceding-sibling::td[contains(text(), '" + name + "')]]/i[@class='icon-remove delete-action']"));
        waitForElement(CONFIRM_RETIRE_BUTTON);
        clickOn(CONFIRM_RETIRE_BUTTON);
    }

    public void assertRetired(String name) {
        try {
            findElement(By.xpath("//tr/td[preceding-sibling::td[contains(text(), '" + name + "')]]/i[@class='icon-reply edit-action']"));
        } catch (TimeoutException e) {
            throw new RuntimeException("Couldn't find restore button, failed to retire location " + name);
        }
    }
}
