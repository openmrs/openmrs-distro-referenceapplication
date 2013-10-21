package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class PatientDashboardPage extends AbstractBasePage {

	public static final String URL_PATH = "/coreapps/clinicianfacing/patient.page";
	private static final String START_VISIT_ID = "org.openmrs.module.coreapps.createVisit";
	private static final String END_VISIT_ID = "referenceapplication.realTime.endVisit";
	private static final String VISIT_NOTE_ID = "referenceapplication.realTime.simpleVisitNote";
	private static final String CONFIRM_DIALOG_ID = "quick-visit-creation-dialog";
	private static final String CONFIRM_BUTTON_CLASS = "confirm";
	private static final String DIAGNOSIS_SEARCH_CONTAINER_ID = "diagnosis-search-container";
	private static final String DIAGNOSIS_SEARCH_ID = "diagnosis-search";

	public PatientDashboardPage(WebDriver driver) {
	    super(driver);
    }

	public void startVisit() {
		clickOn(By.id(START_VISIT_ID));
		waitForElement(By.id(CONFIRM_DIALOG_ID));
		clickOn(By.cssSelector("#" + CONFIRM_DIALOG_ID + " ." + CONFIRM_BUTTON_CLASS));
	}
	
	@Override
    public String expectedUrlPath() {
	    return URL_ROOT + URL_PATH;
    }

	public boolean hasActiveVisit() {
	    return findElement(By.className("active-visit-started-at-message")) != null;
    }

	public WebElement endVisitLink() {
	    return findElement(By.id(END_VISIT_ID));
    }

	public void visitNote() {
	    clickOn(By.id(VISIT_NOTE_ID));
	    waitForElement(By.id(DIAGNOSIS_SEARCH_CONTAINER_ID));
    }

	public void enterDiagnosis(String diag) {
		setTextToFieldNoEnter(By.id(DIAGNOSIS_SEARCH_ID), diag);
		clickOn(By.className("code"));
    }

	public String primaryDiagnosis() {
	    return findElement(By.cssSelector(".diagnosis.primary .matched-name")).getText().trim();
    }

	public void enterNote(String note) {
	    setTextToField(By.id("w10"), note);
    }

	public void save() {
	    clickOn(By.cssSelector(".submitButton.confirm"));
    }

	public WebElement visitLink() {
	    return findElement(By.className("visit-link"));
    }

}