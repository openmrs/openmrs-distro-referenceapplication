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
import org.openqa.selenium.JavascriptExecutor;

public class ManageProviderSchedulesPage extends Page {

    public static final By CURRENT_DAY = By.className("fc-state-highlight");
    private static final By LOCATION = By.xpath("//*[@id='filter-location']/select");
    private static final By LOCATION_IN_BLOCK = By.xpath("//div[@id='select-location']/select");
    private static final By SERVICE_DROPDOWN = By.cssSelector("a.ng-scope.ng-binding");
    private static final By SERVICE = By.id("createAppointmentBlock");
    private static final By END_TIME_BUTTON = By.cssSelector("#end-time button");
    private static final By SAVE = By.cssSelector("button.confirm");


    public ManageProviderSchedulesPage(Page page) {
        super(page);
    }

    public void selectLocation(String location) {
        selectFrom(LOCATION, location);
        driver.findElement(LOCATION);
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("arguments[0].click()", LOCATION);
    }

    public void clickOnCurrentDay() throws InterruptedException {
        clickOn(CURRENT_DAY);
    }

    public void selectLocationBlock(String locblock) {
        waitForElement(LOCATION_IN_BLOCK);
        selectFrom(LOCATION_IN_BLOCK, locblock);
        clickOn(LOCATION_IN_BLOCK);
    }

    public void enterService(String service) {
        boolean flag = false;
        while (!flag) {
            try {
                findElement(SERVICE).clear();
                setTextToFieldNoEnter(SERVICE, service);
                waitForElement(SERVICE_DROPDOWN);
                clickOn(SERVICE_DROPDOWN);
                flag = true;
            } catch (Exception e) {
                flag = false;
            }
        }

    }

    public void clickOnSave() {
        clickOn(SAVE);
        try {
            waitForElementToBeHidden(SAVE);
        } catch (Exception e) {
        }
    }

    public void clickOnEndTimeButton() {
        clickOn(END_TIME_BUTTON);
    }

    @Override
    public String getPageUrl() {
        return "/appointmentschedulingui/scheduleProviders.page";
    }
}