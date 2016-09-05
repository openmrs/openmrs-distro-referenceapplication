/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AppointmentBlocksPage extends Page {

    private static final By APPOINTMENT_SCHEDULING = By.id("appointmentschedulingui-homeAppLink-appointmentschedulingui-homeAppLink-extension");
    private static final By MANAGE_PROVIDER_SCHEDULES = By.id("appointmentschedulingui-scheduleProviders-app");
    private static final By LOCATION = By.className("ng-pristine");
    private static final By START_TIME = By.xpath("(//input[@type='text'])[5]");
    private static final By SERVICE = By.id("createAppointmentBlock");
    private static final By BLOCK = By.className("fc-event-title");
    private static final By SERVICE_DROPDOWN = By.cssSelector("a.ng-scope.ng-binding");
    private static final By EDIT_BLOCK= By.linkText("Edit");
    private static final By SAVE = By.cssSelector("button.confirm");
    private static final By PROVIDER = By.xpath("(//input[@type='text'])[3]");
    public static final By CURRENT_DAY = By.className("fc-state-highlight");
    private static final By LOCATION_IN_BLOCK = By.xpath("//div[@id='select-location']/select");
    public static final By DELETE = By.linkText("Delete");
    private static final By DELETE_CONFIRM = By.cssSelector("#delete-appointment-block-modal-buttons .confirm");
    private static final By SERVICE_DELETE = By.xpath("//div[@id='appointment-block-form']/selectmultipleappointmenttypes/div/div/div/div/i");
    private static final By CLOSE_WINDOW = By.cssSelector("#delete-appointment-block-modal > div.dialog-header > h3");
    public void goToAppointmentBlock(){
        clickOn(APPOINTMENT_SCHEDULING);
        clickOn(MANAGE_PROVIDER_SCHEDULES);
    }

    public void selectLocation(String location) {
        selectFrom(LOCATION, location);
        clickOn(LOCATION);
    }

    public void selectLocationBlock(String locblock){
        waitForElement(LOCATION_IN_BLOCK);
        selectFrom(LOCATION_IN_BLOCK, locblock);
        clickOn(LOCATION_IN_BLOCK);
    }

    public void enterService(String service){
        boolean flag = false;
        while(!flag) {
            try {
                findElement(SERVICE).clear();
                setTextToFieldNoEnter(SERVICE, service);
                waitForElement(SERVICE_DROPDOWN);
                clickOn(SERVICE_DROPDOWN);
                flag = true;
            } catch(Exception e) {
                flag = false;
            }
        }

    }

    public void enterProvider(String provider){setTextToFieldNoEnter(PROVIDER, provider);}

    public void clickOnCurrentDay() throws InterruptedException{clickOn(CURRENT_DAY);}
    public void enterStartTime(String start){setTextToFieldNoEnter(START_TIME, start);}

    public void clickOnSave(){
        clickOn(SAVE);
        try {
            waitForElementToBeHidden(SAVE);
        } catch(Exception e)
        {

        }
    }

    public void clickOnDelete() throws InterruptedException{
        waitForElement(DELETE);
        clickOn(DELETE);
    }
    public void clickOnConfirmDelete(){clickOn(DELETE_CONFIRM);}
    public void clickOnEdit(){clickOn(EDIT_BLOCK);}
    public void clickOnServiceDelete(){clickOn(SERVICE_DELETE);}
    public AppointmentBlocksPage(WebDriver driver) {super(driver);}


    public void findBlock() {
        int iCount = 0;

        try {
            iCount = driver.findElements(By.xpath("//div[@id='calendar']/div/div/div/div")).size();
            findElement(By.xpath("//div[@id='calendar']/div/div/div/div[" + iCount + "]/div/span[2]")).click();
        } catch (Exception e) {

        }
    }
    public void clickOnClose(){
        findElement(CLOSE_WINDOW);
        clickOn(CLOSE_WINDOW);
    }



    @Override
    public String getPageUrl() {
        return "/appointmentschedulingui/scheduleProviders.page";
    }
}

