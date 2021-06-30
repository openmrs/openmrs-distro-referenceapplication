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

public class AddEditLocationPage extends Page {

    protected static final String PAGE_URL = "adminui/metadata/locations/location.page";
    private static final By SAVE_BUTTON = By.id("save-button");
    private static final By NAME_FIELD = By.id("name-field");
    private static final By FIRST_TAG = By.cssSelector("#locationForm fieldset table tbody tr:first-child td:first-child input[type=\"checkbox\"]");

    public AddEditLocationPage(Page parent) {
        super(parent);
    }

    @Override
    public String getPageUrl() {
        return PAGE_URL;
    }

    /**
     * @return go back to manage locations page, but only if there are no validation errors
     */
    public ManageLocationsPage save() {
        clickOn(SAVE_BUTTON);
        return new ManageLocationsPage(this);
    }

    public void enterName(String name) {
        findElement(NAME_FIELD).sendKeys(name);
    }

    public void selectFirstTag() {
        clickOn(FIRST_TAG);
    }
}
