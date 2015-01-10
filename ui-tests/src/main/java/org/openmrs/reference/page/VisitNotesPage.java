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

import java.util.ArrayList;
import java.util.List;

import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class VisitNotesPage extends AbstractBasePage {

	private static final By ENCOUNTERS_LIST = By.id("encountersList");
	private static final By DELETE_DIALOG = By.id("delete-encounter-dialog");
	private static final By PROVIDER_LIST = By.id("w1");
	private static final By LOCATION_LIST = By.id("w3");
	private static final By DIAGNOSES_CONTAINER = By.id("display-encounter-diagnoses-container");
	private static final By VISITS_LIST = By.id("visits-list");
	
	public VisitNotesPage(WebDriver driver) {
		super(driver);
	}
	
	public String getAllDiagnosesString() {
		WebElement visitsList = driver.findElement(VISITS_LIST);
		WebElement span = visitsList.findElement(By.xpath("id('visits-list')/li/span[2]"));
		return span.getText();
	}
	
	public void deleteLastVisitNote() {
		waitForElement(ENCOUNTERS_LIST);
		WebElement encountersList = driver.findElement(ENCOUNTERS_LIST);
		WebElement deleteButton = encountersList.findElement(By.xpath("id('encountersList')/li[1]/span/i[2]"));
		deleteButton.click();
		WebElement deleteDialog = driver.findElement(DELETE_DIALOG);
		WebElement yesButton = deleteDialog.findElement(By.xpath("id('delete-encounter-dialog')/div[2]/button[1]"));
		yesButton.click();
	}
	
	public void editLastVisitNote() {
		waitForElement(ENCOUNTERS_LIST);
		WebElement encountersList = driver.findElement(ENCOUNTERS_LIST);
		WebElement editButton = encountersList.findElement(By.xpath("id('encountersList')/li[1]/span/i[1]"));
		editButton.click();
	}
	
	public void showDetails() {
		waitForElement(ENCOUNTERS_LIST);
		WebElement encountersList = driver.findElement(ENCOUNTERS_LIST);
		WebElement detailsLink = encountersList.findElement(By.xpath("id('encountersList')/li[1]/ul/li[3]/div/a"));
		detailsLink.click();
	}
	
	public String getNote() {
		waitForElement(ENCOUNTERS_LIST);
		WebElement encountersList = driver.findElement(ENCOUNTERS_LIST);
		WebElement span = encountersList.findElement(By.xpath("id('encountersList')/li[1]/div/div/p[2]/span"));
		return span.getText();
	}
	
	public void editChooseProvider(int index) {
		WebElement providerList = driver.findElement(PROVIDER_LIST);
		Select select = new Select(providerList);
		select.selectByIndex(index);
	}
	
	public String editGetProviderName() {
		// get name from edit page
		WebElement providerList = driver.findElement(PROVIDER_LIST);
		Select select = new Select(providerList);
		WebElement option = select.getFirstSelectedOption();
		return option.getText();
	}
	
	public String getProviderName() {
		// get name from dashboard
		waitForElement(ENCOUNTERS_LIST);
		WebElement encountersList = driver.findElement(ENCOUNTERS_LIST);
		WebElement strong = encountersList.findElement(By.xpath("id('encountersList')/li[1]/ul/li[2]/div/strong[1]"));
		return strong.getText();
	}
	
	public void editChooseSessionLocation(int index) {
		WebElement sessionLocationList = driver.findElement(LOCATION_LIST);
		Select select = new Select(sessionLocationList);
		select.selectByIndex(index);
	}
	
	public void editChooseSessionLocation(String name) {
		WebElement sessionLocationList = driver.findElement(LOCATION_LIST);
		Select select = new Select(sessionLocationList);
		select.selectByVisibleText(name);
	}
	
	public String editGetSessionLocation() {
		// get name from edit page
		WebElement sessionLocationList = driver.findElement(LOCATION_LIST);
		Select select = new Select(sessionLocationList);
		WebElement option = select.getFirstSelectedOption();
		return option.getText();
	}

	public String getSessionLocation() {
		// get name from dashboard
		waitForElement(ENCOUNTERS_LIST);
		WebElement encountersList = driver.findElement(ENCOUNTERS_LIST);
		WebElement strong = encountersList.findElement(By.xpath("id('encountersList')/li[1]/ul/li[2]/div/strong[2]"));
		return strong.getText();
	}
	
	public void editDeleteDiagnosis() {
		WebElement container = driver.findElement(DIAGNOSES_CONTAINER);
		WebElement deleteButton = container.findElement(By.xpath("id('display-encounter-diagnoses-container')"
				+ "/ul[1]/li/span/i"));
		deleteButton.click();
	}
	
	public String editGetDiagnosis() {
		// get name from edit page
		WebElement container = driver.findElement(DIAGNOSES_CONTAINER);
		WebElement strong = container.findElement(By.xpath("id('display-encounter-diagnoses-container')"
				+ "/ul[1]/li/span/div/strong"));
		return strong.getText();
	}
	
	public String getDiagnosis() {
		// get name from dashboard
		waitForElement(ENCOUNTERS_LIST);
		WebElement encountersList = driver.findElement(ENCOUNTERS_LIST);
		WebElement span = encountersList.findElement(By.xpath("id('encountersList')/li[1]/div/div/p[1]/span"));
		return span.getText();
	}
	
	public void checkDiagnosisState(boolean confirmed) {
		WebElement container = driver.findElement(DIAGNOSES_CONTAINER);
		WebElement checkbox = container.findElement(By.xpath("id('display-encounter-diagnoses-container')"
				+ "/ul[1]/li/span/div/div/label[2]/input"));
		
		if(checkbox.isSelected() != confirmed) {
			checkbox.click();
		}
	}
	
	public List<String> getLocations() {
		List<WebElement> locationsElements = driver.findElements(By.xpath("id('w5')/option"));
		List<String> locationsName = new ArrayList<String>();
		
		for (WebElement location : locationsElements) {
			locationsName.add(location.getText());
		}
		
		return locationsName;
	}
	
	@Override
	public String expectedUrlPath() {
		return null;
	}

}
