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
import org.openqa.selenium.WebElement;

public class AddAllergyPage extends AbstractBasePage {

	private static final By ALLERGENS = By.id("allergens");
	private static final By ALLERGENS_TYPES = By.id("types");
	private static final By REACTIONS = By.id("reactions");
	private static final By SEVERITIES = By.id("severities");
	private static final By COMMENT = By.id("comment");
	private static final By ADD_ALLERGY_BUTTON = By.id("addAllergyBtn");
	
	public AddAllergyPage(WebDriver driver) {
		super(driver);
	}
	
	public void chooseAllergenType(int index) {
		WebElement table = driver.findElement(ALLERGENS_TYPES);
		List<WebElement> labelCollection = table.findElements(By.xpath("id('types')/label"));
		labelCollection.get(index).click();
	}
	
	public int getAllergensCount(int typeIndex) {
		WebElement table = driver.findElement(ALLERGENS);
		List<WebElement> allergenTypeCollection = table.findElements(By.xpath("id('allergens')/ul[" + (typeIndex + 1) + "]/li"));
		return allergenTypeCollection.size() - 1; // should not take "Other"
	}
	
	public String getAllergenName(int typeIndex, int allergenIndex) {
		WebElement table = driver.findElement(ALLERGENS);
		List<WebElement> allergenTypeCollection = table.findElements(By.xpath("id('allergens')/ul[" + (typeIndex + 1) + "]/li"));
		return allergenTypeCollection.get(allergenIndex).getText();
	}
	
	public void chooseAllergen(int typeIndex, int allergenIndex) {
		WebElement table = driver.findElement(ALLERGENS);
		List<WebElement> allergenTypeCollection = table.findElements(By.xpath("id('allergens')/ul[" + (typeIndex + 1) + "]/li/input[1]"));
		allergenTypeCollection.get(allergenIndex).click();
	}
	
	public int getReactionsCount() {
		WebElement table = driver.findElement(REACTIONS);
		List<WebElement> reactionsCollection = table.findElements(By.xpath("id('reactions')/ul/li"));
		return reactionsCollection.size() - 1; // should not take "Other"
	}
	
	public String getReactionName(int index) {
		WebElement table = driver.findElement(REACTIONS);
		List<WebElement> reactionsCollection = table.findElements(By.xpath("id('reactions')/ul/li"));
		return reactionsCollection.get(index).getText();
	}
	
	public void chooseReaction(int index) {
		WebElement table = driver.findElement(REACTIONS);
		List<WebElement> reactionsCollection = table.findElements(By.xpath("id('reactions')/ul/li/input[1]"));
		reactionsCollection.get(index).click();
	}
	
	public String getSeverityName(int index) {
		WebElement table = driver.findElement(SEVERITIES);
		List<WebElement> severitiesLabelsCollection = table.findElements(By.xpath("id('severities')/p/label"));
		return severitiesLabelsCollection.get(index).getText();
	}
	
	public void chooseSeverity(int index) {
		WebElement table = driver.findElement(SEVERITIES);
		List<WebElement> severitiesInputCollection = table.findElements(By.xpath("id('severities')/p/input"));
		severitiesInputCollection.get(index).click();
	}
	
	public void addComment(String comment) {
		WebElement table = driver.findElement(COMMENT);
		WebElement textArea = table.findElement(By.xpath("id('comment')/textarea"));
		textArea.sendKeys(comment);
	}
	
	public void save() {
		clickOn(ADD_ALLERGY_BUTTON);
	}

	@Override
	public String expectedUrlPath() {
		return URL_ROOT + "/openmrs/allergyui/allergy.page";
	}

}
