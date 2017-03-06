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

import java.util.List;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AppointmentBlocksPage extends Page {
	
	private static final By APPOINTMENT_SCHEDULING = By
	        .id("appointmentschedulingui-homeAppLink-appointmentschedulingui-homeAppLink-extension");
	
	private static final By MANAGE_PROVIDER_SCHEDULES = By.id("appointmentschedulingui-scheduleProviders-app");
	
	private static final By LOCATION = By.className("ng-pristine");
	
	private static final By START_TIME = By.xpath("(//input[@type='text'])[5]");
	
	private static final By SERVICE = By.id("createAppointmentBlock");
	
	private static final By SERVICE_DROPDOWN = By.cssSelector("a.ng-scope.ng-binding");
	
	private static final By EDIT_BLOCK = By.linkText("Edit");
	
	private static final By SAVE = By.cssSelector("button.confirm");
	
	private static final By PROVIDER = By.xpath("(//input[@type='text'])[3]");
	
	//This identifier works if the calendar shows only the day 
	private static final By SERVICE_TITLE_OF_THE_DAY = By.className("fc-event-title");
	
	private static final By DAY = By.xpath("/html/body/div/div[3]/div[2]/div[1]/div[2]/table/tbody/tr/td[1]/span[5]");
	
	private static final By DAY_CLASS = By.className("fc-day-content");
	
	public static final By CURRENT_DAY = By
	        .className("fc-button fc-button-today fc-state-default fc-corner-left fc-corner-right fc-state-disabled");
	
	private static final By LOCATION_IN_BLOCK = By.xpath("//div[@id='select-location']/select");
	
	public static final By DELETE = By.linkText("Delete");
	
	private static final By DELETE_CONFIRM = By.cssSelector("#delete-appointment-block-modal-buttons .confirm");
	
	private static final By SERVICE_DELETE = By
	        .xpath("//div[@id='appointment-block-form']/selectmultipleappointmenttypes/div/div/div/div/i");
	
	private static final By CLOSE_WINDOW = By.cssSelector("#delete-appointment-block-modal > div.dialog-header > h3");
	
	private static final By SERVICE_BLOCK = By.className("fc-event-inner");
	
	private static final By DAY_BLOCK = By.xpath("//table[@class='fc-border-separate']/tbody/tr/td/div");
	
	private static final By REMOVE_APPOINTMENT = By
	        .xpath("//div[@id='appointment-block-form']/selectmultipleappointmenttypes/div/div/div/div/i");
	
	private static final By CANCEL = By.className("cancel");
	
	public void goToAppointmentBlock() {
		clickOn(APPOINTMENT_SCHEDULING);
		clickOn(MANAGE_PROVIDER_SCHEDULES);
	}
	
	/**
	 * Removes an appointment. Before calling this method is necessary to click on the day (User
	 * should click on the day button on the left)
	 * 
	 * @see #clickOnDay()
	 */
	public void removeAppointment() {
		clickOn(REMOVE_APPOINTMENT);
	}
	
	public void selectLocation(String location) {
		// a wait might be needed if there are many elements to load in the dropdown menu
		waiter.until(ExpectedConditions.elementToBeClickable(LOCATION));
		selectFrom(LOCATION, location);
		clickOn(LOCATION);
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
			}
			catch (Exception e) {
				flag = false;
			}
		}
		
	}
	
	public void enterProvider(String provider) {
		setTextToFieldNoEnter(PROVIDER, provider);
	}
	
	public void clickOnCurrentDay() throws InterruptedException {
		clickOn(CURRENT_DAY);
	}
	
	public void enterStartTime(String start) {
		setTextToFieldNoEnter(START_TIME, start);
	}
	
	/**
	 * Scrolls the page up This method has been created since the delete button is hidden by the
	 * tooltip. An alternative solution to this method is to create a method that closes the tooltip
	 * such as: private static final By CLOSE_TOOLTIP =
	 * By.cssSelector("span.ui-icon.ui-icon-close"); public void clickOnCloseTooltip(){
	 * clickOn(CLOSE_TOOLTIP); } Unfortunately this method did not prove to be always reliable
	 * (sometime Selenium fails to locate span.ui-icon.ui-icon-close)
	 */
	public void clickOnleft() {
		executeScript("scroll(300, 0)");
	}
	
	/**
	 * Clicks on the current day and open the "editing page". This method should be invoked when the
	 * page shows only a block with the day
	 */
	public void clickOnDay() {
		clickOn(DAY);
		clickOn(DAY_CLASS);
	}
	
	/**
	 * Clicks on the Cancel button available in the "editing page"
	 **/
	public void clickOnCancel() {
		clickOn(CANCEL);
	}
	
	public void clickOnSave() {
		clickOn(SAVE);
		try {
			waitForElementToBeHidden(SAVE);
		}
		catch (Exception e) {
			
		}
	}
	
	/**
	 * Checks if the the Save button available in the "editing page" is enabled
	 */
	public Boolean isSaveEnabled() {
		return findElement(SAVE).isEnabled();
	}
	
	public void clickOnDelete() throws InterruptedException {
		waitForElement(DELETE);
		clickOn(DELETE);
	}
	
	public void clickOnConfirmDelete() {
		clickOn(DELETE_CONFIRM);
	}
	
	public void clickOnEdit() {
		clickOn(EDIT_BLOCK);
	}
	
	public void clickOnServiceDelete() {
		clickOn(SERVICE_DELETE);
	}
	
	public AppointmentBlocksPage(Page page) {
		super(page);
	}
	
	public void clickOnAppointment() {
		findElement(SERVICE_BLOCK).click();
		;
	}
	
	/**
	 * Clicks on the Close button available in the "editing page"
	 */
	public void clickOnClose() {
		findElement(CLOSE_WINDOW);
		clickOn(CLOSE_WINDOW);
	}
	
	/**
	 * Extracts the service from a row displayed in the block
	 * 
	 * @param serviceChoice, the index of the service. For instance if there are 2 rows on the
	 *            block, to get the first one serviceChoice = 0
	 * @return the Service in the row (e.g. Dermatology)
	 */
	public String getServiceOfDay(int serviceChoice) {
		
		List<WebElement> elements = findElements(SERVICE_TITLE_OF_THE_DAY);
		if (null != elements && elements.size() >= serviceChoice) {
			return elements.get(serviceChoice).getText();
		} 
		return new String();
	}
	
	public void clickOnDayBlock() {
		clickOn(DAY_BLOCK);
	}
	
	@Override
	public String getPageUrl() {
		return "/appointmentschedulingui/scheduleProviders.page";
	}
}
