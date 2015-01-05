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


public class PatientDashboardPage extends AbstractBasePage {

	public static final String URL_PATH = "/coreapps/clinicianfacing/patient.page";
	private static final By START_VISIT = By.id("org.openmrs.module.coreapps.createVisit");
	private static final By END_VISIT = By.id("referenceapplication.realTime.endVisit");
	private static final By CONFIRM = By.cssSelector("#quick-visit-creation-dialog .confirm");
	private static final By STARTED_AT = By.className("active-visit-started-at-message");
	private static final By VISIT_NOTE = By.id("referenceapplication.realTime.simpleVisitNote");
	private static final By CAPTURE_VITALS = By.id("referenceapplication.realTime.vitals");
	private static final By DIAGNOSIS_SEARCH_CONTAINER = By.id("diagnosis-search-container");
	private static final By DIAGNOSIS_SEARCH = By.id("diagnosis-search");
	private static final By CONTENT = By.id("content");
	
	private static final By VITALS_HEIGHT = By.id("height");
	private static final By VITALS_WEIGHT = By.id("weight");
	private static final By VITALS_TEMPERATURE = By.id("temperature");
	private static final By VITALS_HEART_RATE = By.id("heart_rate");
	private static final By VITALS_RESPIRATORY_RATE = By.id("respiratory_rate");
	private static final By VITALS_BP_SYSTOLIC = By.id("bp_systolic");
	private static final By VITALS_BP_DIASTOLIC = By.id("bp_diastolic");
	private static final By VITALS_OXYGEN_SATURATION = By.id("o2_sat");
	
	public PatientDashboardPage(WebDriver driver) {
	    super(driver);
    }
	
	public void editDemographics() {
		WebElement content = driver.findElement(CONTENT);
		WebElement editButton = content.findElement(By.xpath("id('content')/div[5]/div[1]/h1/span[3]/span[3]"));
		editButton.click();
	}
	
	public void editContactInfo() {
		WebElement contactInfoContent = driver.findElement(By.id("contactInfoContent"));
		WebElement editContactInfoButton = contactInfoContent.findElement(By.xpath("id('contactInfoContent')/div/small"));
		editContactInfoButton.click();
	}
	
	public String getGivenName() {
		WebElement content = driver.findElement(CONTENT);
		WebElement span = content.findElement(By.xpath("id('content')/div[5]/div[1]/h1/span[2]"));
		return span.getText().split("\\s+")[0];
	}
	
	public String getFamilyName() {
		WebElement content = driver.findElement(CONTENT);
		WebElement span = content.findElement(By.xpath("id('content')/div[5]/div[1]/h1/span[1]"));
		String family = span.getText().split("\\s+")[0];
		family = family.substring(0, family.length() - 1);	// delete last character ','
		return family;
	}
	
	public String getGender() {
		WebElement content = driver.findElement(CONTENT);
		WebElement span = content.findElement(By.xpath("id('content')/div[5]/div[1]/h1/span[3]/span[1]"));
		return span.getText();
	}

	public String[] getBirthdateData() {
		WebElement content = driver.findElement(CONTENT);
		WebElement span = content.findElement(By.xpath("id('content')/div[5]/div[1]/h1/span[3]/span[2]"));
		
		String data = span.getText().split("\\s+")[2];
		data = data.substring(1, data.length() - 1);
		
		return data.split("\\.");
	}
	
	public String[] getAddressData() {
		WebElement contactInfoContent = driver.findElement(By.id("contactInfoContent"));
		WebElement span = contactInfoContent.findElement(By.xpath("id('contactInfoContent')/div/span[1]"));
		
		String[] address = span.getText().split("\\s+");
		
		address[0] = address[0].substring(0, address[0].length() - 1);	// remove ','
		address[1] = address[1].substring(0, address[1].length() - 1);	// remove ','
		
		return address;
	}
	
	public String getPhoneNumber() {
		WebElement contactInfoContent = driver.findElement(By.id("contactInfoContent"));
		WebElement span = contactInfoContent.findElement(By.xpath("id('contactInfoContent')/div/span[2]"));
		String[] text = span.getText().split("\\s+");
		return text[0];
	}
	
	public void showContactInfo() {
		WebElement showContactInfoButton = driver.findElement(By.id("patient-header-contactInfo"));
		showContactInfoButton.click();
	}

	public void startVisit() {
		clickOn(START_VISIT);
		waitForElement(CONFIRM);
		clickOn(CONFIRM);
	}
	
	public void captureVitals() {
		clickOn(CAPTURE_VITALS);
	}
	
	public void editAllergies(String currPage) {
		currPage = currPage.substring(currPage.indexOf('?') + 1);
		((JavascriptExecutor) driver).executeScript("location.href='/openmrs/allergyui/allergies.page?" + currPage + "';");
	}
	
	public String getAllergen() {
		WebElement span = driver.findElement(By.className("allergen"));
		return span.getText();
	}
	
	public String getAllergyReaction() {
		WebElement span = driver.findElement(By.className("allergyReaction"));
		return span.getText();
	}
	
	public String getAllergyReaction(int index) {
		List<WebElement> span = driver.findElements(By.className("allergyReaction"));
		return span.get(index).getText();
	}
	
	public String getAllergiesString() {
		WebElement content = driver.findElement(By.id("content"));
		WebElement span = content.findElement(By.xpath("id('content')/div[8]/div/div[2]/div[2]/div[2]"));
		return span.getText();
	}
	
	public double getVitalsHeight() {
		WebElement span = driver.findElement(VITALS_HEIGHT).findElement(By.xpath("id('height')/span"));
		return Double.parseDouble(span.getText());
	}
	
	public double getVitalsWeight() {
		WebElement span = driver.findElement(VITALS_WEIGHT).findElement(By.xpath("id('weight')/span"));
		return Double.parseDouble(span.getText());
	}
	
	public double getVitalsTemperature() {
		WebElement span = driver.findElement(VITALS_TEMPERATURE).findElement(By.xpath("id('temperature')/span"));
		return Double.parseDouble(span.getText());
	}
	
	public int getVitalsHeartRate() {
		WebElement span = driver.findElement(VITALS_HEART_RATE).findElement(By.xpath("id('heart_rate')/span"));
		return Integer.parseInt(span.getText());
	}
	
	public long getVitalsRespiratoryRate() {
		WebElement span = driver.findElement(VITALS_RESPIRATORY_RATE).findElement(By.xpath("id('respiratory_rate')/span"));
		return Long.parseLong(span.getText());
	}
	
	public int getVitalsBpSystolic() {
		WebElement span = driver.findElement(VITALS_BP_SYSTOLIC).findElement(By.xpath("id('bp_systolic')/span"));
		return Integer.parseInt(span.getText());
	}
	
	public int getVitalsBpDiastolic() {
		WebElement span = driver.findElement(VITALS_BP_DIASTOLIC).findElement(By.xpath("id('bp_diastolic')/span"));
		return Integer.parseInt(span.getText());
	}
	
	public int getVitalsOxygenSaturation() {
		WebElement span = driver.findElement(VITALS_OXYGEN_SATURATION).findElement(By.xpath("id('o2_sat')/span"));
		return Integer.parseInt(span.getText());
	}
	
	@Override
    public String expectedUrlPath() {
	    return URL_ROOT + URL_PATH;
    }

	public boolean hasActiveVisit() {
	    return findElement(STARTED_AT) != null;
    }

	public WebElement endVisitLink() {
	    return findElement(END_VISIT);
    }

	public void visitNote() {
	    clickOn(VISIT_NOTE);
	    waitForElement(DIAGNOSIS_SEARCH_CONTAINER);
    }

	public void enterDiagnosis(String diag) {
		setTextToFieldNoEnter(DIAGNOSIS_SEARCH, diag);
		clickOn(By.className("code"));
    }

	public String primaryDiagnosis() {
	    return findElement(By.cssSelector(".diagnosis.primary .matched-name")).getText().trim();
    }

	public void enterNote(String note) {
	    setText(By.id("w10"), note);
    }

	public void save() {
	    clickOn(By.cssSelector(".submitButton.confirm"));
    }

	public WebElement visitLink() {
	    return findElement(By.className("visit-link"));
    }

}