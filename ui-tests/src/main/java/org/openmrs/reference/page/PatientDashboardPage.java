package org.openmrs.reference.page;

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
	public static final By DIAGNOSIS_SEARCH_CONTAINER = By.id("diagnosis-search-container");
	public static final By DIAGNOSIS_SEARCH = By.id("diagnosis-search");
    public static final By VISIT_LINK = By.className("toast-item-wrapper");
	private static final By YES = By.cssSelector("#end-visit-dialog .confirm");


    public PatientDashboardPage(WebDriver driver) {
	    super(driver);
    }

	public void startVisit() {
		clickOn(START_VISIT);
		waitForElement(CONFIRM);
		clickOn(CONFIRM);
    }

	public void endVisit(){
		clickOn(END_VISIT);
		waitForElement(YES);
		clickOn(YES);
	}


	@Override
    public String expectedUrlPath() {
	    return URL_ROOT + URL_PATH;
    }

	public boolean hasActiveVisit() {
        try {
            return findElement(STARTED_AT) != null;
        } catch(Exception e) {
            return false;
        }
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

	public void enterSecondaryDiagnosis(String diag) {
		setTextToFieldNoEnter(DIAGNOSIS_SEARCH, diag);
		waitForElement(By.id("ui-id-1"));
		clickOn(By.className("ui-menu-item"));
	}


	public String primaryDiagnosis() {
	    return findElement(By.cssSelector(".diagnosis.primary .matched-name")).getText().trim();
    }

	public String secondaryDiagnosis() {
		return findElement(By.xpath("//ul[2]/li/span/div/strong")).getText();
	}

	public void enterNote(String note) {
	    setText(By.id("w10"), note);
    }

	public void save() {
	    clickOn(By.cssSelector(".submitButton.confirm"));
    }

	public WebElement visitLink() {
	    return findElement(VISIT_LINK);
    }
    public void waitForVisitLink() {
        waitForElement(VISIT_LINK);
    }

	public void waitForVisitLinkHidden() {
		waitForElementToBeHidden(VISIT_LINK);
	}
}