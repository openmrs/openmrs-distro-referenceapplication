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
package org.openmrs.reference;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.uitestframework.test.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;

public class StyleColorTest extends TestBase {
	private HeaderPage headerPage;
	private HomePage homePage;
	
	private List<String> colors;
	private List<WebElement> allFormChildElements;
	
	// The colors that are considered normal
	// Anyone can change the list by adding or removing color
	private static final String[] NORMAL_COLORS = {
		
		"#000000", "#ffffff",	// Black and white colors
		
		"#f26522", "#5b57a6", "#eea616", "#009384", "#231f20",	// OpenMRS Colors
		
		"#363463", "#007fff", "#003366",	// Others very repeated colors
		
		"#f9f9f9", "#fffdf7", "#f3f3f3", "#fafafa",		// Backgrounds colors
		
		"#aaaaaa", "#bbbbbb", "#cccccc", "#dddddd", "#eeeeee",	// Shades of gray
		"#222222", "#333333", "#555555", "#666666", "#888888", "#999999",
		"#808080", "#d8d8d8", "#e2e2e2", "#e6e6e6"
		
	};
	
	@Before
	public void before() {
		headerPage = new HeaderPage(driver);
        homePage = new HomePage(driver);
        
        login();
		assertPage(homePage);
	}
	
	@After
	public void after() {
		homePage.go();
		assertPage(homePage);
		
		headerPage.logOut();
		assertPage(loginPage);
	}
	
	// Anyone can add new page for testing
	@Test
	public void homePageTest() {
		checkWebPageAllElementsColor();
	}
	
	@Test
	public void adminPageTest() {
		currentPage().gotoPage("/admin/index.htm");
		checkWebPageAllElementsColor();
	}
	
	@Test
	public void findPatientsPageTest() {
		currentPage().gotoPage("/coreapps/findpatient/findPatient.page?app=coreapps.findPatient");
		checkWebPageAllElementsColor();
	}
	
	@Test
	public void findActiveVisitsPageTest() {
		currentPage().gotoPage("/coreapps/activeVisits.page?app=coreapps.activeVisits");
		checkWebPageAllElementsColor();
	}
	
	@Test
	public void registerPatientPageTest() {
		currentPage().gotoPage("/registrationapp/registerPatient.page?appId=referenceapplication.registrationapp.registerPatient");
		checkWebPageAllElementsColor();
	}
	
	@Test
	public void captureVitalsPageTest() {
		currentPage().gotoPage("/coreapps/findpatient/findPatient.page?app=referenceapplication.vitals");
		checkWebPageAllElementsColor();
	}
	
	@Test
	public void styleGuidePageTest() {
		currentPage().gotoPage("/uicommons/styleGuide.page");
		checkWebPageAllElementsColor();
	}
	
	public String getElementXPath(WebDriver driver, WebElement element) {
	    return (String)((JavascriptExecutor)driver).executeScript("gPt=function(c){if(c.id!==''){return'id(\"'+c.id+'\")'}if(c===document.body){return c.tagName}var a=0;var e=c.parentNode.childNodes;for(var b=0;b<e.length;b++){var d=e[b];if(d===c){return gPt(c.parentNode)+'/'+c.tagName+'['+(a+1)+']'}if(d.nodeType===1&&d.tagName===c.tagName){a++}}};return gPt(arguments[0]).toLowerCase();", element);
	}
	
	private void checkWebPageAllElementsColor() {
		// print page url
		System.out.println("\n\n" + driver.getCurrentUrl() + "\n");
		
		// get all elements
		allFormChildElements = driver.findElements(By.cssSelector("*"));
		// get all normal colors
		colors = Arrays.asList(NORMAL_COLORS);
		
		for(WebElement item : allFormChildElements)
		{
			String color = Color.fromString(item.getCssValue("color")).asHex();
			String backgroundColor = Color.fromString(item.getCssValue("background-color")).asHex();
			
			// check "color" css value
			if(color != "" && !colors.contains(color)) {
				System.out.println("Weird color: " + color + ", Item path: " + getElementXPath(driver, item));
			}
			
			// check "background-color" css value
			if(backgroundColor != "" && !colors.contains(backgroundColor)) {
				System.out.println("Weird background color: " + backgroundColor + ", Item path: " + getElementXPath(driver, item));
			}
		}
	}
}
