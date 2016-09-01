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
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.util.List;


public class ManageProviderPage extends Page {

	private static final By SEARCH_ELEMENT = By.id("inputNode");
	private static final By INCLUDE_RETIRED = By.id("includeVoided");
	private static final By ADD_PROVIDER = By.cssSelector("#content a[href='provider.form']");
	private static final By PROVIDERS_IDENTIFIERS = By.cssSelector("#openmrsSearchTable tr td:nth-child(2)");
	private static final By PROVIDERS_NAMES = By.cssSelector("#openmrsSearchTable tr td:first-child");
	private static final By OPENMRS_MSG = By.id("openmrs_msg");

	public ManageProviderPage(Page parent) {
		super(parent);
	}

	public ProviderPage clickOnAddProvider(){
		clickOn(ADD_PROVIDER);
		return new ProviderPage(this);
	}

	public void setProviderNameOrId(String text){
		WebElement element = findElement(PROVIDERS_IDENTIFIERS);
		setText(SEARCH_ELEMENT, text);
		findElement(SEARCH_ELEMENT).sendKeys(Keys.BACK_SPACE);
		//wait for new search results to come
		waitForStalenessOf(element);
	}

	public void clickOnIncludeRetired(){
		clickOn(INCLUDE_RETIRED);
	}

	public ProviderPage clickOnProvider(String name){
		findElement(PROVIDERS_IDENTIFIERS);

		List<WebElement> elements = findElements(PROVIDERS_NAMES);
		for (WebElement element: elements) {
			if (element.getText().equals(name)) {
				element.click();
				return new ProviderPage(this);
			}
		}

		throw new IllegalStateException("Could not find provider with identifier: " + name);
	}

	@Override
	public String getPageUrl() {
		return "/admin/provider/index.htm";
	}

	public String getActionMessage() {
		return findElement(OPENMRS_MSG).getText();
	}
}
