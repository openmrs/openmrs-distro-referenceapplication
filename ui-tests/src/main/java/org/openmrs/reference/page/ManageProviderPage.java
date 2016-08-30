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


public class ManageProviderPage extends Page {

	private static final By SEARCH_ELEMENT = By.id("inputNode");
	private static final By INCLUDE_RETIRED = By.id("includeVoided");
	private static final By ADD_PROVIDER = By.cssSelector("#content a[href='provider.form']");
	private static final By FIRST_PROVIDER = By.cssSelector("#openmrsSearchTable td:first-child");
	private static final By OPENMRS_MSG = By.id("openmrs_msg");

	public ManageProviderPage(Page parent) {
		super(parent);
	}

	public ProviderPage clickOnAddProvider(){
		clickOn(ADD_PROVIDER);
		return new ProviderPage(this);
	}

	public void setProviderNameOrId(String text){
		setText(SEARCH_ELEMENT, text);
		findElement(SEARCH_ELEMENT).sendKeys(Keys.BACK_SPACE);
	}

	public void clickOnIncludeRetired(){
		clickOn(INCLUDE_RETIRED);
	}

	public ProviderPage clickOnFirstProvider(){
		clickOn(FIRST_PROVIDER);
		return new ProviderPage(this);
	}

	@Override
	public String getPageUrl() {
		return "/admin/provider/index.htm";
	}

	public String getActionMessage() {
		return findElement(OPENMRS_MSG).getText();
	}
}
