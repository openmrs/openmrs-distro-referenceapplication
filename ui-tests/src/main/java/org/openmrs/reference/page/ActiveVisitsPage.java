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

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ActiveVisitsPage extends SearchPage {

	static final By SEARCH = By.id("active-visits_filter");
	static final By TABLE = By.id("active-visits");
	
	public ActiveVisitsPage(WebDriver driver) {
		super(driver);
	}
	
	public void clickFirstPatient() {
		WebElement tableElement = driver.findElement(TABLE);
		WebElement link = tableElement.findElement(By.xpath("id('active-visits')/tbody/tr[1]/td[2]/a"));
		link.click();
	}
	
	@Override
	public void search(String template) {
		WebElement searchInput = driver.findElement(SEARCH).findElement(By.xpath("id('active-visits_filter')/label/input"));
		searchInput.clear();
		searchInput.sendKeys(template);
	}
	
	@Override
	public List<String> getStringsFromColumn(int column) {
		// Takes all the strings of this column (e.g. 0 - Identifier, 1 - Name)
		List<String> list = new ArrayList<String>();
		WebElement tableElement = driver.findElement(TABLE);
		List<WebElement> tr_collection = tableElement.findElements(By.xpath("id('active-visits')/tbody/tr"));
		
        for(WebElement trElement : tr_collection) {
            List<WebElement> td_collection = trElement.findElements(By.xpath("td"));
            String patient = td_collection.get(column).getText();
            list.add(patient);
        }
        
        return list;
	}
	
	@Override
	public boolean verifyTable(int column, String template) throws Exception {
		waitForElement(TABLE);
		WebElement tableElement = driver.findElement(TABLE);
		List<WebElement> tr_collection = tableElement.findElements(By.xpath("id('active-visits')/tbody/tr"));
		
        for(WebElement trElement : tr_collection) {
            List<WebElement> td_collection = trElement.findElements(By.xpath("td"));
            String patient = td_collection.get(column).getText();
            
            if(patient.indexOf(template) == -1) {
            	return false;
            }
        }
        
        return true;
	}

	public boolean isLoaded() {
		return driver.findElements(TABLE).size() != 0;
	}
	
	@Override
	public String expectedUrlPath() {
		return URL_ROOT + "/coreapps/activeVisits.page";
	}

}
