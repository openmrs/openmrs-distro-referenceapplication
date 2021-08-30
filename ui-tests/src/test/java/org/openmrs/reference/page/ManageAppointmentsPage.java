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

public class ManageAppointmentsPage extends Page {
	
	public static final By DELETE_REQUEST = By.cssSelector(".delete-item.icon-remove");
	
	private static final By CANCEL_BUTTON = By.cssSelector("#searchButtons > button");
	
	private static final By SEARCH = By.cssSelector("#searchButtons > button.confirm");
	
	private static final By APPOINTMENT = By.xpath("//table[@id='appointmentTable']/div[2]/div/div/div/div[2]/div[2]/div");
	
	private static final By NEXT = By.cssSelector("#selectAppointment > button.confirm");
	
	private static final By SAVE = By.xpath("//div[@id='confirmAppointment']/div[2]/button[2]");
	
	private static final By BOOK_APPOINTMENT = By.cssSelector("i.icon-calendar:nth-child(1)");
	
	private static final By YES_DELETE_REQUEST = By.xpath("//div[@id='confirm-cancel-appointment-request']/div[2]/button");
	
	private static final By VIEW_ALL_TYPES_LINK = By.cssSelector("#viewAllAppointmentTypes > a");
	
	private static final By PATIENT_APPOINTMENT = By.id("miniPatientAppointments");
	
	public ManageAppointmentsPage(Page page) {
		super(page);
	}
	
	public void searchAppointment() {
		clickOn(SEARCH);
	}
	
	public void clickAppointment() {
		clickOn(APPOINTMENT);
	}
	
	public FindPatientPage saveAppointment() {
		clickOn(NEXT);
		clickOn(SAVE);
		return new FindPatientPage(this);
	}
	
	public void deleteRequest() {
		waitForElement(DELETE_REQUEST);
		clickOn(DELETE_REQUEST);
		clickOn(YES_DELETE_REQUEST);
	}
	
	public String getAppointmentServiceType() {
		final int serviceColumn = 1;
		return findElement(By.cssSelector("div.col" + serviceColumn + " > span")).getText();
	}
	
	public String getAppointmentStatus() {
		final int statusColumn = 4;
		return findElement(By.cssSelector("div.col" + statusColumn + " > span")).getText();
	}
	
	public void clickOnBookAppointment() {
		clickOn(BOOK_APPOINTMENT);
	}
	
	public void clickOnViewAllTypes() {
		clickOn(VIEW_ALL_TYPES_LINK);
	}
	
	public WebElement checkAppointment() {
		return findElement(PATIENT_APPOINTMENT);
	}
	
	public void clickOnService(String serviceName) {
		By link = By.xpath("/" + "/div[@id='allAppointmentTypesModal']" + "/div[@class='dialog-content']" + "//*[text()='"
		        + serviceName + "']");
		clickOn(link);
	}
	
	public ClinicianFacingPatientDashboardPage clickCancel() {
		clickOn(CANCEL_BUTTON);
		return new ClinicianFacingPatientDashboardPage(this);
	}
	
	@Override
	public String getPageUrl() {
		return "/appointmentschedulingui/manageAppointments.page";
	}
}
