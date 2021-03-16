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
import org.openqa.selenium.WebElement;

public class ManageServiceTypesPage extends Page {

    private static final By NEW_SERVICE_TYPE = By.cssSelector("#content div div:nth-child(1) button");
    private static final By SERVICE_TYPES = By.cssSelector("#appointmentTypesTable tbody tr td:nth-child(1)");
    private static final By NEXT_BUTTON = By.id("appointmentTypesTable_next");
    private static final By CONFIRM_DELETE = By.cssSelector("#simplemodal-container #delete-appointment-type-dialog div.dialog-content button.confirm");
    private static String EDIT_ICON_ID = "appointmentschedulingui-edit-%s";
    private static String DELETE_ICON_ID = "appointmentschedulingui-delete-%s";

    public ManageServiceTypesPage(Page parent, WebElement waitForStaleness) {
        super(parent, waitForStaleness);
    }

    public ManageServiceTypesPage(Page parent) {
        super(parent);
    }

    public ServicePage clickOnNewServiceType() {
        clickOn(NEW_SERVICE_TYPE);
        return new ServicePage(this);
    }

    public boolean getServiceType(String name) {
        boolean serviceFound = false;
        while (!serviceFound) {
            for (WebElement element : findElements(SERVICE_TYPES)) {
                if (element.getText().equals(name)) {
                    serviceFound = true;
                }
            }
            if (serviceFound) {
                return true;
            } else {
                if (findElement(NEXT_BUTTON).getAttribute("class").contains("ui-state-disabled")) {
                    return false;
                } else {
                    goToNextPage();
                }
            }
        }
        return false;
    }

    private void goToNextPage() {
        clickOn(NEXT_BUTTON);
    }

    public void deleteServiceType(String name) {
        clickOn(By.id(String.format(DELETE_ICON_ID, name)));
    }

    public ServicePage editServiceType(String name) {
        clickOn(By.id(String.format(EDIT_ICON_ID, name)));
        return new ServicePage(this);
    }

    public ManageServiceTypesPage confirmDelete() {
        WebElement newServiceBtn = findElement(NEW_SERVICE_TYPE);
        clickOn(CONFIRM_DELETE);
        return new ManageServiceTypesPage(this, newServiceBtn);
    }

    @Override
    public String getPageUrl() {
        return "/appointmentschedulingui/manageAppointmentTypes.page";
    }
}
