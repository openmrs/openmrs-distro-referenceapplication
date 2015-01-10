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

public class ManageLocationsPage extends AbstractBasePage {
	
	public ManageLocationsPage(WebDriver driver) {
		super(driver);
	}
	
	public void addLocation() {
		WebElement link = driver.findElement(By.xpath("id('content')/a"));
		link.click();
	}
	
	public void enterName(String name) {
		WebElement input = driver.findElement(By.xpath("//table[@class='left-aligned-th']/tbody/tr[1]/td/input"));
		input.clear();
		input.sendKeys(name);
	}
	
	public void saveLocation() {
		WebElement input = driver.findElement(By.xpath("//input[@name='saveLocation']"));
		input.click();
	}
	
	public void editLocation(String locationName) {
		List<WebElement> links = driver.findElements(By.xpath("id('locationTable')/tbody/tr/td[2]/a"));
		for (WebElement link : links) {
			if(link.getText().equals(locationName)) {
				link.click();
				break;
			}
		}
	}
	
	public void deleteLocation(String locationName) {
		List<WebElement> links = driver.findElements(By.xpath("id('locationTable')/tbody/tr/td[2]/a"));
		List<WebElement> checkboxes = driver.findElements(By.xpath("id('locationTable')/tbody/tr/td[1]/input"));
		
		for (int i = 0; i < links.size(); i++) {
			if(links.get(i).getText().equals(locationName)) {
				checkboxes.get(i).click();
				break;
			}
		}
		
		WebElement input = driver.findElement(By.xpath("//input[@name='action']"));
		input.click();
	}
	
	public void selectTag(int index) {
		List<WebElement> checkboxes = driver.findElements(By.xpath("//table[@class='left-aligned-th']/tbody/tr[8]/td/input[@type='checkbox']"));
		checkboxes.get(index).click();
	}
	
	public List<String> getLocationsFromLoginPage() {
		List<WebElement> locationsElements = driver.findElements(By.xpath("id('sessionLocation')/li"));
		List<String> locationsName = new ArrayList<String>();
		
		for (WebElement location : locationsElements) {
			locationsName.add(location.getText());
		}
		
		return locationsName;
	}

	@Override
	public String expectedUrlPath() {
		return URL_ROOT + "/admin/locations/location.list";
	}

}
