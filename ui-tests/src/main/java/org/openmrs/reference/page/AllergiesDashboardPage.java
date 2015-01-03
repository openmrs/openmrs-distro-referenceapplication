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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AllergiesDashboardPage extends AbstractBasePage {
	
	private static final By ALLERGIES_TABLE = By.id("allergies");
	private static final By CONTENT = By.id("content");
	
	public AllergiesDashboardPage(WebDriver driver) {
		super(driver);
	}
	
	public String getAllergyString(int column) {
		WebElement table = driver.findElement(ALLERGIES_TABLE);
		List<WebElement> tdCollection = table.findElements(By.xpath("id('allergies')/tbody[1]/tr/td"));
		return tdCollection.get(column).getText();
	}

	public void addAllergy(String currPage) {
		currPage = currPage.substring(currPage.indexOf('?') + 1);
		((JavascriptExecutor) driver).executeScript("location.href='/openmrs/allergyui/allergy.page?" + currPage + "';");
	}
	
	public void viewPatientDashboard(String currPage) {
		currPage = currPage.substring(currPage.indexOf('?') + 1);
		((JavascriptExecutor) driver).executeScript("location.href='/openmrs/coreapps/clinicianfacing/patient.page?" + currPage + "';");
	}
	
	public void deleteAllergy() {
		WebElement table = driver.findElement(ALLERGIES_TABLE);
		
		List<WebElement> tdCollection = table.findElements(By.xpath("id('allergies')/tbody[1]/tr/td"));
		WebElement deleteButton = table.findElement(By.xpath("id('allergies')/tbody[1]/tr/td[" + tdCollection.size() + "]/i[2]"));
		deleteButton.click();
		
		WebElement allergyDialogTable = driver.findElement(By.id("allergyui-remove-allergy-dialog"));
		WebElement okButton = allergyDialogTable.findElement(By.xpath("id('allergyui-remove-allergy-dialog')/div[2]/form/button[1]"));
		okButton.click();
	}
	
	public void addNoKnownAllergy() {
		WebElement content = driver.findElement(CONTENT);
		WebElement button = content.findElement(By.xpath("id('content')/form/button"));
		button.click();
	}
	
	public void deleteNoKnownAllergy() {
		((JavascriptExecutor) driver).executeScript("document.deactivateForm.submit();");
	}
	
	@Override
	public String expectedUrlPath() {
		return URL_ROOT + "/openmrs/allergyui/allergies.page";
	}

}
