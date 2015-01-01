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

public class FindPatientRecordPage extends SearchPage {

	static final By SEARCH = By.id("patient-search");
	static final By TABLE = By.id("patient-search-results-table");
	
	public FindPatientRecordPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void search(String template) {
		setTextToFieldNoEnter(SEARCH, template);
	}
	
	@Override
	public List<String> getStringsFromColumn(int column) {
		// Takes all the strings of this column (e.g. 0 - Identifier, 1 - Name)
		List<String> list = new ArrayList<String>();
		WebElement tableElement = driver.findElement(TABLE);
		List<WebElement> tr_collection = tableElement.findElements(By.xpath("id('patient-search-results-table')/tbody/tr"));
		
        for(WebElement trElement : tr_collection) {
            List<WebElement> td_collection = trElement.findElements(By.xpath("td"));
            String patient = td_collection.get(column).getText();
            list.add(patient);
        }
        
        return list;
	}
	
	@Override
	public boolean verifyTable(int column, String template) throws Exception {
		Thread.sleep(1500); 	// A little hack - we have to wait until the table is fully loaded
		WebElement tableElement = driver.findElement(TABLE);
		List<WebElement> tr_collection = tableElement.findElements(By.xpath("id('patient-search-results-table')/tbody/tr"));
		
        for(WebElement trElement : tr_collection) {
            List<WebElement> td_collection = trElement.findElements(By.xpath("td"));
            String patient = td_collection.get(column).getText();
            
            if(patient.indexOf(template) == -1) {
            	return false;
            }
        }
        
        return true;
	}
	
	@Override
	public String expectedUrlPath() {
		return URL_ROOT + "/coreapps/findpatient/findPatient.page";
	}

}
