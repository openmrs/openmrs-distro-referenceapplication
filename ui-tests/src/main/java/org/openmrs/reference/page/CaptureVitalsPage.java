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

import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration;

public class CaptureVitalsPage extends AbstractBasePage {

	private static final By HEIGHT = By.id("height");
	private static final By WEIGHT = By.id("weight");
	private static final By TEMPERATURE = By.id("temperature");
	private static final By HEART_RATE = By.id("heart_rate");
	private static final By RESPIRATORY_RATE = By.id("respiratory_rate");
	private static final By BP_SYSTOLIC = By.id("bp_systolic");
	private static final By BP_DIASTOLIC = By.id("bp_diastolic");
	private static final By OXYGEN_SATURATION = By.id("o2_sat");
	
	
	public CaptureVitalsPage(WebDriver driver) {
		super(driver);
	}
	
	public void clickVitalUL(int index) {
		WebElement form = driver.findElement(By.id("htmlform"));
		List<WebElement> tr_collection = form.findElements(By.xpath("id('htmlform')/ul/li/ul/li"));
		tr_collection.get(index).click();
	}
	
	public void setHeight(double height) {
		WebElement heightInput = driver.findElement(HEIGHT).findElement(By.xpath("id('height')/input"));
		heightInput.clear();
		heightInput.sendKeys(Double.toString(height));
	}
	
	public void setWeight(double weight) {
		WebElement weightInput = driver.findElement(WEIGHT).findElement(By.xpath("id('weight')/input"));
		weightInput.clear();
		weightInput.sendKeys(Double.toString(weight));
	}
	
	public void setTemperature(double temperature) {
		WebElement temperatureInput = driver.findElement(TEMPERATURE).findElement(By.xpath("id('temperature')/input"));
		temperatureInput.clear();
		temperatureInput.sendKeys(Double.toString(temperature));
	}
	
	public void setHeartRate(int heartRate) {
		WebElement heartRateInput = driver.findElement(HEART_RATE).findElement(By.xpath("id('heart_rate')/input"));
		heartRateInput.clear();
		heartRateInput.sendKeys(Integer.toString(heartRate));
	}
	
	public void setRespiratoryRate(long respiratoryRate) {
		WebElement respiratoryRateInput = driver.findElement(RESPIRATORY_RATE).findElement(By.xpath("id('respiratory_rate')/input"));
		respiratoryRateInput.clear();
		respiratoryRateInput.sendKeys(Long.toString(respiratoryRate));
	}
	
	public void setBpSystolic(int bpSystolic) {
		WebElement bpSystolicInput = driver.findElement(BP_SYSTOLIC).findElement(By.xpath("id('bp_systolic')/input"));
		bpSystolicInput.clear();
		bpSystolicInput.sendKeys(Integer.toString(bpSystolic));
	}
	
	public void setBpDiastolic(int bpDiastolic) {
		WebElement bpDiastolicInput = driver.findElement(BP_DIASTOLIC).findElement(By.xpath("id('bp_diastolic')/input"));
		bpDiastolicInput.clear();
		bpDiastolicInput.sendKeys(Integer.toString(bpDiastolic));
	}
	
	public void setOxygenSaturation(int oxygenSaturation) {
		WebElement oxygenSaturationInput = driver.findElement(OXYGEN_SATURATION).findElement(By.xpath("id('o2_sat')/input"));
		oxygenSaturationInput.clear();
		oxygenSaturationInput.sendKeys(Integer.toString(oxygenSaturation));
	}
	
	public void save() {
		((JavascriptExecutor) driver).executeScript("submitHtmlForm();");
	}

	@Override
	public String expectedUrlPath() {
		return URL_ROOT + "/openmrs/htmlformentryui/htmlform/enterHtmlFormWithSimpleUi.page";
	}

}
