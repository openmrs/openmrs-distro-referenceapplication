package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;


public class PatientDashboardPage extends AbstractBasePage {

	public static final String URL_PATH = "/coreapps/clinicianfacing/patient.page";
	private static final By START_VISIT = By.id("org.openmrs.module.coreapps.createVisit");
	public static final By END_VISIT = By.id("referenceapplication.realTime.endVisit");
	private static final By CONFIRM = By.cssSelector("#quick-visit-creation-dialog .confirm");
	private static final By STARTED_AT = By.className("active-visit-started-at-message");
	private static final By VISIT_NOTE = By.id("referenceapplication.realTime.simpleVisitNote");
	private static final By DIAGNOSIS_SEARCH_CONTAINER = By.id("diagnosis-search-container");
	private static final By DIAGNOSIS_SEARCH = By.id("diagnosis-search");
	public static final By VISIT_LINK = By.className("toast-item-wrapper");
	private static final By VISIT_LINK_2 = By.className("visit-link");
	private static final By YES = By.cssSelector("#end-visit-dialog .confirm");
	private static final By ADMIT_TO_INPATIENT = By.linkText("Admit to Inpatient");
	private static final By SAVE = By.xpath("//input[@value='Save']");
	private static final By EXIT_FROM_INPATIENT = (By.linkText("Exit from Inpatient"));
	public static final By SELECT_LOCATION = By.id("w5");
	private static final By TRANSFER_TO_WARD_SERVICE = By.id("referenceapplication.realTime.simpleTransfer");

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

	public void clickOnSave(){
		clickOn(SAVE);
	}


	public void clickOnAdmitToInpatient(){
		clickOn(ADMIT_TO_INPATIENT);
	}

	public boolean inpatientPresent(){
		try {
			return driver.findElement(ADMIT_TO_INPATIENT) != null;
		}
		catch (Exception ex) {
			return false;
		}
	}

	public void exitFromInpatient(){
		clickOn(EXIT_FROM_INPATIENT);
		new Select(driver.findElement(By.id("w5"))).selectByVisibleText("Unknown Location");
		clickOn(SAVE);

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

	public WebElement location() {
		return findElement(SELECT_LOCATION);
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

	public WebElement findLinkToVisit() {
		return findElement(VISIT_LINK_2);
	}

	public void waitForVisitLink() {
		waitForElement(VISIT_LINK);
	}

	public void waitForVisitLinkHidden() {
		waitForElementToBeHidden(VISIT_LINK);
	}

	public void selectLocation(String loctation){
		selectFrom(SELECT_LOCATION, loctation);
	}

	public void clickOnTranfer(){
		clickOn(TRANSFER_TO_WARD_SERVICE);
	}
}