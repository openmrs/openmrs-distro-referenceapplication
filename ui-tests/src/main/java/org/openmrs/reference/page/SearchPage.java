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

public class SearchPage extends AbstractBasePage {

	static final By SEARCH = null;
	static final By TABLE = null;
	
	public SearchPage(WebDriver driver) {
		super(driver);
	}

	public void search(String template) {
		
	}
	
	public List<String> getStringsFromColumn(int column) {
		return null;
	}
	
	public boolean verifyTable(int column, String template) throws Exception {
		return true;
	}
	
	@Override
	public String expectedUrlPath() {
		return null;
	}

}
