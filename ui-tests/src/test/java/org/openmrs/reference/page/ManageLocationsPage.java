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

    public static final By CONFIRM_PURGE_BUTTON = By.cssSelector("#adminui-purge-location-dialog > div.dialog-content > form > button.confirm.right");
    public static final By CONFIRM_RETIRE_BUTTON = By.cssSelector("#adminui-retire-location-dialog > div.dialog-content > form > button.confirm.right");
    protected static final String PAGE_URL = "adminui/metadata/locations/manageLocations.page";
    private static final By ADD_EDIT_LOCATION = By.cssSelector("#content a[href='/openmrs/" + AddEditLocationPage.PAGE_URL + "']");
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
