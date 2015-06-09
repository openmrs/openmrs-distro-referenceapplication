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
import org.openqa.selenium.support.ui.Select;

public class EditPatientDemographicsPage extends AbstractBasePage {

	private static final By DEMOGRAPHICS = By.id("demographics");
	private static final By OPTIONS_LIST = By.id("formBreadcrumb");
	private static final By CONFIRMATION_QUESTION = By.id("confirmationQuestion");
	
	public EditPatientDemographicsPage(WebDriver driver) {
		super(driver);
	}
	
	public void chooseDemographicsOption(int index) {
		WebElement optionsList = driver.findElement(OPTIONS_LIST);
		List<WebElement> liCollection = optionsList.findElements(By.xpath("id('formBreadcrumb')/li[1]/ul/li"));
		liCollection.get(index).click();
	}
	
	public void setGivenName(String name) {
		WebElement demographics = driver.findElement(DEMOGRAPHICS);
		WebElement input = demographics.findElement(By.xpath("id('demographics')/fieldset[1]/p[1]/input"));
		input.clear();
		input.sendKeys(name);
	}
	
	public void setFamilyName(String name) {
		WebElement demographics = driver.findElement(DEMOGRAPHICS);
		WebElement input = demographics.findElement(By.xpath("id('demographics')/fieldset[1]/p[2]/input"));
		input.clear();
		input.sendKeys(name);
	}
	
	public void setGender(int gender) {
		WebElement demographics = driver.findElement(DEMOGRAPHICS);
		WebElement radioButton = demographics.findElement(By.xpath("id('demographics')/fieldset[2]/p[" + gender + "]/input"));
		radioButton.click();
	}
	
	public void setBirthdateYear(int year) {
		WebElement demographics = driver.findElement(DEMOGRAPHICS);
		WebElement input = demographics.findElement(By.xpath("id('demographics')/fieldset[3]/p[4]/input"));
		input.clear();
		input.sendKeys(Integer.toString(year));
	}
	
	public int getBirthdateYear() {
		WebElement demographics = driver.findElement(DEMOGRAPHICS);
		WebElement input = demographics.findElement(By.xpath("id('demographics')/fieldset[3]/p[4]/input"));
		return Integer.parseInt(input.getAttribute("value"));
	}
	
	public void setBirthdateMonth(int month) {
		WebElement demographics = driver.findElement(DEMOGRAPHICS);
		WebElement input = demographics.findElement(By.xpath("id('demographics')/fieldset[3]/p[3]/select"));
		Select select = new Select(input);
		select.selectByIndex(month);
	}
	
	public int getBirthdateMonth() {
		WebElement demographics = driver.findElement(DEMOGRAPHICS);
		WebElement input = demographics.findElement(By.xpath("id('demographics')/fieldset[3]/p[3]/select"));
		Select select = new Select(input);
		WebElement option = select.getFirstSelectedOption();
		return Integer.parseInt(option.getAttribute("value"));
	}
	
	public String getBirthdateMonthString() {
		WebElement demographics = driver.findElement(DEMOGRAPHICS);
		WebElement input = demographics.findElement(By.xpath("id('demographics')/fieldset[3]/p[3]/select"));
		Select select = new Select(input);
		WebElement option = select.getFirstSelectedOption();
		return option.getText();
	}
	
	public void setBirthdateDay(int day) {
		WebElement demographics = driver.findElement(DEMOGRAPHICS);
		WebElement input = demographics.findElement(By.xpath("id('demographics')/fieldset[3]/p[2]/input"));
		input.clear();
		input.sendKeys(Integer.toString(day));
	}
	
	public int getBirthdateDay() {
		WebElement demographics = driver.findElement(DEMOGRAPHICS);
		WebElement input = demographics.findElement(By.xpath("id('demographics')/fieldset[3]/p[2]/input"));
		return Integer.parseInt(input.getAttribute("value"));
	}
	
	public void confirm() {
		WebElement optionsList = driver.findElement(OPTIONS_LIST);
		WebElement liConfirm = optionsList.findElement(By.xpath("id('formBreadcrumb')/li[2]"));
		liConfirm.click();
		
		WebElement confirmationQuestion = driver.findElement(CONFIRMATION_QUESTION);
		WebElement confirmButton = confirmationQuestion.findElement(By.xpath("id('confirmationQuestion')/p[1]/input"));
		confirmButton.click();
	}

	@Override
	public String expectedUrlPath() {
		return URL_ROOT + "/registrationapp/editPatientDemographics.page";
	}

}
