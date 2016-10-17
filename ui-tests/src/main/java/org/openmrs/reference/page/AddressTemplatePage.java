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

import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AddressTemplatePage extends AbstractBasePage {

	public AddressTemplatePage(WebDriver driver) {
		super(driver);
	}
	
	public String getXmlCode() {
		WebElement content = driver.findElement(By.id("content"));
		WebElement textArea = content.findElement(By.xpath("id('content')/div[3]/form/table/tbody/tr[1]/td/textarea"));
		return textArea.getText();
	}
	
	public void setXmlCode(String code) {
		WebElement content = driver.findElement(By.id("content"));
		WebElement textArea = content.findElement(By.xpath("id('content')/div[3]/form/table/tbody/tr[1]/td/textarea"));
		textArea.clear();
		textArea.sendKeys(code);
		
		WebElement submit = content.findElement(By.xpath("id('content')/div[3]/form/table/tbody/tr[2]/td/input[1]"));
		submit.click();
	}

	@Override
	public String expectedUrlPath() {
		return URL_ROOT + "/admin/locations/addressTemplate.form";
	}
}
