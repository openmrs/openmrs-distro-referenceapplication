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

public class UserOptionsPage extends AbstractBasePage {

	private static final By OPTSECTION_1 = By.id("optsection-1");
	private static final By OPTIONS_LIST = By.id("optionsTOC");
	
	public static String URL_PAGE = "/options.form";
	
	public UserOptionsPage(WebDriver driver) {
		super(driver);
	}
	
	public void pressChangeLoginInfo() {
		WebElement table = driver.findElement(OPTIONS_LIST);
		List<WebElement> liCollection = table.findElements(By.xpath("id('optionsTOC')/li"));
		liCollection.get(1).click();
	}
	
	public String getUserOption(int trIndex) {
		WebElement table = driver.findElement(OPTSECTION_1);
		List<WebElement> inputCollection = table.findElements(By.xpath("id('optsection-1')/table/tbody/tr/td/input"));
		return inputCollection.get(trIndex).getAttribute("value");
	}
	
	public void pasteNewUserOption(int trIndex, String option) {
		WebElement table = driver.findElement(OPTSECTION_1);
		List<WebElement> inputCollection = table.findElements(By.xpath("id('optsection-1')/table/tbody/tr/td/input"));
		inputCollection.get(trIndex).clear();
		inputCollection.get(trIndex).sendKeys(option);
	}
	
	public void saveOptions() {
		WebElement content = driver.findElement(By.id("content"));
		WebElement button = content.findElement(By.xpath("id('content')/form/input"));
		button.click();
	}

	@Override
	public String expectedUrlPath() {
		return URL_ROOT + URL_PAGE;
	}

}
