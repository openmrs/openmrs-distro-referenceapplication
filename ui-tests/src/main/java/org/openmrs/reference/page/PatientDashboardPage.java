package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.Calendar;
import java.util.List;


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
	public static final By FORM_EXIST = By.className("action-section");
	private static final By EDIT_PATIENT = By.linkText("Edit");
	private static final By SHOW_CONTACT_INFO = By.cssSelector("span.show");
	private static final By EDIT_CONTACT_INFO = By.id("contact-info-inline-edit");
	private static final By PATIENT_ID = By.cssSelector("div.identifiers > span");
	private static final By CODE = By.className("code");
	private static final By UI_ID_1 = By.id("ui-id-1");
	private static final By UI_MENU_ITEM = By.className("ui-menu-item");
	private static final By PRIMARY_DIAGNOSIS_ELEMENT = By.cssSelector(".diagnosis.primary .matched-name");
	private static final By SECONDARY_DIAGNOSIS_ELEMENT = By.xpath("//ul[2]/li/span/div/strong");
	private static final By CURRENT_DATE = By.linkText("Today");
	private static final By VISIT_NOTE_ENCOUNTER = By.xpath("//div[@id='visit-details']/ul/li/ul/li/div/strong/span[text()='Visit Note']");
	private static final By NOTE = By.id("w10");
	private static final By SAVE_VISIT_NOTE = By.cssSelector(".submitButton.confirm");
	private static final By CONFIRM_DEL_BUTTON = By.cssSelector("#delete-encounter-dialog > div.dialog-content > button.confirm.right");
	private static final By DEL_DIAGNOSIS = By.xpath("//div[@id='display-encounter-diagnoses-container']/ul/li/span/i");
	private static final By DATE_FIELD = By.id("w5-display");
	private static final By PROVIDER = By.id("w1");
	private static final By LOCATION = By.id("w3");
	private static final By WHO_WHEN_WHERE = By.id("who-when-where");
	private static final By REQUEST_APPOINTMENT = By.linkText("Request Appointment");
	private static final By FRAME_VALUE = By.id("min-time-frame-value");
	private static final By FRAME_UNITS = By.id("min-time-frame-units");
	private static final By SAVE_REQUEST = By.id("save-button");
	private static final By APPOINTMENT_TYPE = By.id("appointment-type");
	private static final By SERVICE_DROPDOWN = By.cssSelector("a.ng-scope.ng-binding");

	private static final By ERROR = By.cssSelector("li.error > span");
	private static final By ADD_PAST_VISIT = By.linkText("Add Past Visit");

	private static final By TODAY = By.cssSelector("td.day.active");
	private static final By CONFIRM_PAST_VISIT = By.cssSelector("div.dialog-content.form > button.confirm.right");
	private static final By CHANGE_DATE = By.cssSelector("button.confirm.no-color");
	public static final By SUCCESS_MESSAGE = By.cssSelector("div.toast-item.toast-type-success > p");
	private static final By DAY = By.xpath("//table[@class=' table-condensed']/tbody/tr[1]/td[0]");
	private static final By MERGE_VISIT_BUTTON = By.xpath("//a[@id='org.openmrs.module.coreapps.mergeVisits']/li");
	private static final By MERGE = By.id("mergeVisitsBtn");

	public PatientDashboardPage(WebDriver driver) {
		super(driver);
    }

	public void startVisit() {
		clickOn(START_VISIT);
		waitForElement(CONFIRM);
		clickOn(CONFIRM);
	}

	public void clickOnEditPatient(){
		clickOn(EDIT_PATIENT);
	}

	public void endVisit() throws InterruptedException{
		clickWhenVisible(END_VISIT);
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
		clickOn(CODE);
	}

	public void addDiagnosis(String diag) {
		WebElement diagnosisElement = findElement(DIAGNOSIS_SEARCH);
		diagnosisElement.click();
		enterDiagnosis(diag);
		diagnosisElement.clear();
		diagnosisElement.click();

	}

	public void addSecondaryDiagnosis(String diag) {
		WebElement diagnosisElement = findElement(DIAGNOSIS_SEARCH);
		diagnosisElement.click();
		enterSecondaryDiagnosis(diag);
		diagnosisElement.clear();
		diagnosisElement.click();

	}

	public void enterSecondaryDiagnosis(String diag) {
		setTextToFieldNoEnter(DIAGNOSIS_SEARCH, diag);
		waitForElement(UI_ID_1);
		clickOn(UI_MENU_ITEM);
	}

	public String primaryDiagnosis() {
		return findElement(PRIMARY_DIAGNOSIS_ELEMENT).getText().trim();
	}

	public String secondaryDiagnosis() {
		return findElement(SECONDARY_DIAGNOSIS_ELEMENT).getText();
	}

	public WebElement location() {
		return findElement(SELECT_LOCATION);
	}

	public void enterNote(String note) {
		setText(NOTE, note);
	}

	public void addNote(String note) {
		findElement(NOTE).clear();
		enterNote(note);
	}
	public void save() {
		clickOn(SAVE_VISIT_NOTE);
	}

	public WebElement visitLink() {
		return findElement(VISIT_LINK);
	}

	public WebElement findLinkToVisit() {
		waitForElement(VISIT_LINK_2);
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
//Contact Info
	public void clickOnShowContact(){ clickOn(SHOW_CONTACT_INFO);}
	public void clickOnEditContact(){ clickOn(EDIT_CONTACT_INFO);}
//Find Patient Id
	public String findPatientId(){
		return findElement(PATIENT_ID).getText();
	}

	public void goToEditVisitNote() {
		clickOn(CURRENT_DATE);
		String visitNoteId = findElement(VISIT_NOTE_ENCOUNTER).getAttribute("data-encounter-id");
		clickOn(By.xpath("//div[@id='visit-details']/ul/li/span/i[@data-encounter-id='"+visitNoteId+"']"));
	}

	public void deleteVisitNote() {
		String visitNoteId = findElement(VISIT_NOTE_ENCOUNTER).getAttribute("data-encounter-id");
		clickOn(By.xpath("//div[@id='visit-details']/ul/li/span/i[@data-encounter-id='" + visitNoteId + "'][2]"));
	}

	public void confirmDeletion() {
		clickOn(CONFIRM_DEL_BUTTON);
	}
	public void deleteDiagnosis() {
		clickOn(DEL_DIAGNOSIS);
	}

	public void getCurrentDate() {
		findElement(DATE_FIELD).click();
		findElement(By.linkText("" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH))).click();
	}

	public void selectProviderAndLocation() {
		new Select(findElement(PROVIDER)).selectByVisibleText("Super User");
		new Select(findElement(LOCATION)).selectByVisibleText("Isolation Ward");
	}

	public WebElement findPageElement() {
		return findElement(WHO_WHEN_WHERE);
	}

	public boolean errorPresent(){
		try {
			return driver.findElement(ERROR) != null;
		}
		catch (Exception ex) {
			return false;
		}
	}


	public void addPastVisit(){
		clickOn(ADD_PAST_VISIT);
		clickOn(TODAY);
		clickOn(CONFIRM_PAST_VISIT);
	}

	public void clickChangeDate (){
		waitForElement(CHANGE_DATE);
		clickOn(CHANGE_DATE);
	}

	public void enterDate(){
		Calendar currentDay = Calendar.getInstance();
	 	Integer dayOfMoth = currentDay.get(Calendar.DAY_OF_MONTH);
	 	A: for(int j = 6; j > 0; j--) {
			for (int i = 7; i > 0; i--) {
				WebElement day = findElement(By.xpath(String.format("//table[@class=' table-condensed']/tbody/tr[%s]/td[%s]",j, i)));
				if(!day.getAttribute("class").contains("disabled")) {
					if (day.getText().equals("" + dayOfMoth)) {
						day.click();
						dayOfMoth--;
						clickOn(CONFIRM_PAST_VISIT);
						try {
							clickChangeDate();
						} catch(Exception e) {
							break A;
						}

					}
				}
			}
		}
	}

	public String mergeVisits() {

		clickOn(MERGE_VISIT_BUTTON);
		driver.findElement(By.xpath("//table[@id='active-visits']/tbody/tr/td/input[@name='mergeVisits']")).click();
		driver.findElement(By.xpath("//table[@id='active-visits']/tbody/tr[2]/td/input[@name='mergeVisits']")).click();
		clickOn(MERGE);
		waitForVisitLink();
		String text = findElement(VISIT_LINK).getText();
		waitForVisitLinkHidden();
		return text;
	}
	
	public void clickOnRequest(){ clickOn(REQUEST_APPOINTMENT);}
	public void enterValue(String value){ setText(FRAME_VALUE, value);}
	public void selectUnits(String units){ selectFrom(FRAME_UNITS, units);}
	public void saveRequest(){ clickOn(SAVE_REQUEST);}
	public void enterAppointmentType(String type){
		setTextToFieldNoEnter(APPOINTMENT_TYPE, type);
		waitForElement(SERVICE_DROPDOWN);
		clickOn(SERVICE_DROPDOWN);
	}
}