/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.reference.page;

import java.util.List;

import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LocationPage extends AbstractBasePage {

	private static final By USERNAME = By.id("username");
	private static final By PASSWORD = By.id("password");
	private static final By LOGIN = By.id("login-button");
	private static final By LOCATION_LIST = By.id("sessionLocation");
	
	private static final String LOGOUT_PATH = "/logout";
	
	public LocationPage(WebDriver driver) {
		super(driver);
	}
	
	public String getLocationName(int index) {
		WebElement locationList = driver.findElement(LOCATION_LIST);
		List<WebElement> locationCollection = locationList.findElements(By.xpath("id('sessionLocation')/li"));
		return locationCollection.get(index).getText();
	}
	
	public void clickOnLocation(int index) {
		WebElement locationList = driver.findElement(LOCATION_LIST);
		List<WebElement> locationCollection = locationList.findElements(By.xpath("id('sessionLocation')/li"));
		locationCollection.get(index).click();
	}
	
	public void loginWithoutChooseLocation() {
		// We can simple use login() after clickOnLocation(), but by default login() selects the first location
		setTextToFieldNoEnter(USERNAME, properties.getUserName());
		setTextToFieldNoEnter(PASSWORD, properties.getPassword());
		clickOn(LOGIN);
		findElement(byFromHref(URL_ROOT + LOGOUT_PATH));	// this waits until the Logoff link is present
	}
	
	public void loginWithNewPassword(String password) {
		setTextToFieldNoEnter(USERNAME, properties.getUserName());
		setTextToFieldNoEnter(PASSWORD, password);
		clickOn(LOGIN);
		findElement(byFromHref(URL_ROOT + LOGOUT_PATH));	// this waits until the Logoff link is present
	}

	@Override
	public String expectedUrlPath() {
		return URL_ROOT + "/openmrs/login.htm";
	}

}
