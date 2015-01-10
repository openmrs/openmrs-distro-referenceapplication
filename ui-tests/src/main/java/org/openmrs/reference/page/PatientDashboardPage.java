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


public class PatientDashboardPage extends AbstractBasePage {

	public static final String URL_PATH = "/coreapps/clinicianfacing/patient.page";
	private static final By START_VISIT = By.id("org.openmrs.module.coreapps.createVisit");
	private static final By END_VISIT = By.id("referenceapplication.realTime.endVisit");
	private static final By CONFIRM = By.cssSelector("#quick-visit-creation-dialog .confirm");
	private static final By STARTED_AT = By.className("active-visit-started-at-message");
	private static final By VISIT_NOTE = By.id("referenceapplication.realTime.simpleVisitNote");
	private static final By DIAGNOSIS_SEARCH_CONTAINER = By.id("diagnosis-search-container");
	private static final By DIAGNOSIS_SEARCH = By.id("diagnosis-search");
	private static final By EXIT_INPATIENT = By.id("referenceapplication.realTime.simpleDischarge");
	private static final By ADMIT_INPATIENT = By.id("referenceapplication.realTime.simpleAdmission");
	private static final By TRANSFER = By.id("referenceapplication.realTime.simpleTransfer");
	private static final By VITALS = By.id("referenceapplication.realTime.vitals");

	public PatientDashboardPage(WebDriver driver) {
	    super(driver);
    }

	public void startVisit() {
		clickOn(START_VISIT);
		waitForElement(CONFIRM);
		clickOn(CONFIRM);
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

	public void admitInpatient() {
		WebElement admitBtn = driver.findElement(ADMIT_INPATIENT);
		if (admitBtn != null)
		{
			clickOn(ADMIT_INPATIENT);
			WebElement dropDownListBox = driver.findElement(By.id("w5"));
			List<WebElement> options = dropDownListBox.findElements(By.tagName("option"));
		    for (WebElement option : options) {
		        if("Inpatient Ward".equals(option.getText()))
		            option.click();
		    }
		    save();
		}
	}
	
	public void captureVitals() {
		clickOn(VITALS);
	}
	
	public boolean hasExitBtn() {
		WebElement container = driver.findElement(By.id("exit-form-container"));
		boolean exitBtn = container.findElement(By.xpath("id('exit-form-container')/a")) != null;
		return exitBtn;
	}
	
	public void transfer() {
		clickOn(TRANSFER);
	}
	
	public boolean hasDischarge() {
		WebElement discharge = driver.findElement(By.id("discharge"));
		return discharge != null;
	}
	
	public void exitInpatient() {
		clickOn(EXIT_INPATIENT);
	}
	
	public boolean hasDropdown() {
		WebElement dropdown = driver.findElement(By.id("w5"));
		return dropdown != null;
	}
	
	public boolean hasSearchContainer()
	{
		WebElement container = driver.findElement(DIAGNOSIS_SEARCH_CONTAINER);
		return container != null;
	}
	
	public void endVisit()
	{
		clickOn(END_VISIT);
		waitForElement(By.id("end-visit-dialog"));
	}
	
	public boolean hasEndDialog()
	{
		WebElement endDialog = driver.findElement(By.id("end-visit-dialog"));
		return endDialog != null;
	}
}