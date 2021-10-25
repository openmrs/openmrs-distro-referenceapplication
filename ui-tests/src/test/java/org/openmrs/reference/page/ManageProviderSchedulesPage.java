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

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

public class ManageProviderSchedulesPage extends Page {

    private static final By NEXT_WEEKDAY;
    static {
        LocalDate nextWeekday = LocalDate.now();
        switch (nextWeekday.getDayOfWeek()) {
            case SATURDAY:
                nextWeekday.plus(2, ChronoUnit.DAYS);
                break;
            case SUNDAY:
                nextWeekday.plus(1, ChronoUnit.DAYS);
                break;
        }

        NEXT_WEEKDAY = By.cssSelector(String.format("[data-date=\"%s\"]", nextWeekday));
    }
    private static final By LOCATION = By.className("ng-pristine");
    private static final By LOCATION_IN_BLOCK = By.xpath("//div[@id='select-location']/select");
    private static final By SERVICE_DROPDOWN = By.cssSelector("a.ng-scope.ng-binding");
    private static final By SERVICE = By.id("createAppointmentBlock");
    private static final By MIN_HOUR_VALUE_FIELD = By.xpath("//*[@id='start-time']/table/tbody/tr[2]/td[1]/input");
    private static final By MAX_HOUR_VALUE_FIELD = By.xpath("//*[@id='end-time']/table/tbody/tr[2]/td[1]/input");
    private static final By MIN_MINUTES_VALUE_FIELD = By.xpath("//*[@id='start-time']/table/tbody/tr[2]/td[3]/input");
    private static final By MAX_MINUTES_VALUE_FIELD = By.xpath("//*[@id='end-time']/table/tbody/tr[2]/td[3]/input");
    private static final By START_TIME_BUTTON = By.cssSelector("#start-time button");
    private static final By END_TIME_BUTTON = By.cssSelector("#end-time button");
    private static final By SAVE = By.cssSelector("button.confirm");

    public ManageProviderSchedulesPage(Page page) {
        super(page);
    }

    public void selectLocation(String location) {
        selectFrom(LOCATION, location);
        waitForElement(LOCATION);
        clickOn(LOCATION);
    }

    /**
     * Clicks on the next available weekday. We use this because the default for the appointment's module is to only show
     * appointments for weekdays.
     */
    public void clickOnNextWeekday() {
        clickOn(NEXT_WEEKDAY);
    }

    public void selectLocationBlock(String locblock) {
        selectFrom(LOCATION_IN_BLOCK, locblock);
        waitForElement(LOCATION_IN_BLOCK);
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
        } catch (Exception e) {}
    }

    public void enterMinimumTimeValue(String hour, String minutes) {
        findElement(MIN_HOUR_VALUE_FIELD).clear();
        setText(MIN_HOUR_VALUE_FIELD, hour);
        findElement(MIN_MINUTES_VALUE_FIELD).clear();
        setText(MIN_MINUTES_VALUE_FIELD, minutes);       
    }
 
    public void clickOnStartTimeButton() {
        clickOn(START_TIME_BUTTON);
    }
    
    public void enterMaximumTimeValue(String hour, String minutes) {
        findElement(MAX_HOUR_VALUE_FIELD).clear();
        setText(MAX_HOUR_VALUE_FIELD, hour);
        findElement(MAX_MINUTES_VALUE_FIELD).clear();
        setText(MAX_MINUTES_VALUE_FIELD, minutes);
    }
     
    public void clickOnEndTimeButton() {
        clickOn(END_TIME_BUTTON);
    }

    @Override
    public String getPageUrl() {
        return "/appointmentschedulingui/scheduleProviders.page";
    }
}
