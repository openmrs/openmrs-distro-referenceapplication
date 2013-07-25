package org.openmrs.reference.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SearchPatientPage extends AbstractBasePage {
	
	public SearchPatientPage(WebDriver driver) {
		super(driver);
	}
	
	static final String SEARCH_TEXTBOX_ID = "patient-search";
	static final String SEARCH_BUTTON_ID = "form#patient-search-form input:contains('patient-search-form')"; //css-selector will be replaced with id once it is implemented in the app
	
	private void enterPatientIDOrName(String nameOrID) {
		setTextToField(By.id(SEARCH_TEXTBOX_ID), nameOrID);
	}
	
	private void submitSearch() {
		clickOn(By.cssSelector(SEARCH_BUTTON_ID));
	}
	
	public void searchPatient(String nameOrID) {
		enterPatientIDOrName(nameOrID);
		submitSearch();
	}
	
	@Override
	public String expectedUrlPath() {
		return "openmrs/coreapps/findpatient/findPatient.page?app=coreapps.findPatient";
	}
}
