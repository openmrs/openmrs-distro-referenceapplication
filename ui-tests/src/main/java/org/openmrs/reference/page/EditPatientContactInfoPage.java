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

public class EditPatientContactInfoPage extends AbstractBasePage {

	private static final By OPTIONS_LIST = By.id("formBreadcrumb");
	private static final By CONFIRMATION_QUESTION = By.id("confirmationQuestion");
	private static final By CONTACT_INFO = By.id("contactInfo");
	
	public EditPatientContactInfoPage(WebDriver driver) {
		super(driver);
	}
	
	public void chooseContactInfoOption(int index) {
		WebElement optionsList = driver.findElement(OPTIONS_LIST);
		List<WebElement> liCollection = optionsList.findElements(By.xpath("id('formBreadcrumb')/li[1]/ul/li"));
		liCollection.get(index).click();
	}
	
	public String getAddress() {
		WebElement input = driver.findElement(By.id("address1"));
		return input.getAttribute("value");
	}
	
	public void setAddress(String address1) {
		WebElement input = driver.findElement(By.id("address1"));
		input.clear();
		input.sendKeys(address1);
	}
	
	public String getAddressSecond() {
		WebElement input = driver.findElement(By.id("address2"));
		return input.getAttribute("value");
	}
	
	public void setAddressSecond(String address2) {
		WebElement input = driver.findElement(By.id("address2"));
		input.clear();
		input.sendKeys(address2);
	}
	
	public String getCityVillage() {
		WebElement input = driver.findElement(By.id("cityVillage"));
		return input.getAttribute("value");
	}
	
	public void setCityVillage(String cityVillage) {
		WebElement input = driver.findElement(By.id("cityVillage"));
		input.clear();
		input.sendKeys(cityVillage);
	}
	
	public String getStateProvince() {
		WebElement input = driver.findElement(By.id("stateProvince"));
		return input.getAttribute("value");
	}
	
	public void setStateProvince(String stateProvince) {
		WebElement input = driver.findElement(By.id("stateProvince"));
		input.clear();
		input.sendKeys(stateProvince);
	}
	
	public String getCountry() {
		WebElement input = driver.findElement(By.id("country"));
		return input.getAttribute("value");
	}
	
	public void setCountry(String country) {
		WebElement input = driver.findElement(By.id("country"));
		input.clear();
		input.sendKeys(country);
	}
	
	public String getPostalCode() {
		WebElement input = driver.findElement(By.id("postalCode"));
		return input.getAttribute("value");
	}
	
	public void setPostalCode(String postalCode) {
		WebElement input = driver.findElement(By.id("postalCode"));
		input.clear();
		input.sendKeys(postalCode);
	}
	
	public String getPhoneNumber() {
		WebElement optionsList = driver.findElement(CONTACT_INFO);
		WebElement input = optionsList.findElement(By.xpath("id('contactInfo')/fieldset[2]/p/input"));
		return input.getAttribute("value");
	}
	
	public void setPhoneNumber(String phoneNumber) {
		WebElement optionsList = driver.findElement(CONTACT_INFO);
		WebElement input = optionsList.findElement(By.xpath("id('contactInfo')/fieldset[2]/p/input"));
		input.clear();
		input.sendKeys(phoneNumber);
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
		return URL_ROOT + "/registrationapp/editPatientContactInfo.page";
	}

}
