package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

public class MarkPatientDeceasedPage extends Page {

	String URL = "/coreapps/markPatientDead.page";

	private static final By DICTIONARY = By.id("dictionaryNavLink");

	private static final By ADD_NEW_CONCEPT = By.xpath("//*[@id=\"content\"]/a[2]");

	private static final By CONCEPT_NAME = By.id("namesByLocale[en].name");

	private static final By ADD_ALERGY = By.xpath("//*[@id=\"codedDatatypeRow\"]/td/table/tbody/tr/td[2]/input[1]");

	private static final By CAUSEBOX = By.id("newAnswerConcept");

	private static final By FEVER = By.xpath("/html/body/ul[1]/li[2]/a");

	private static final By ADDBUTTON = By.xpath("/html/body/div[5]/div[11]/button[1]");

	private static final By SAVE = By.xpath("//*[@id=\"saveDeleteButtons\"]/input[1]");

	private static final By SEARCH = By.id("inputNode");

	private static final By CONCEPT_SEARCH_MESSAGE = By.id("openmrsSearchTable_info");

	private static final By COD = By.xpath("//*[@id=\"openmrsSearchTable\"]/tbody/tr[1]/td/span");

	private static final By COD_ID = By.xpath("//*[@id=\"conceptTable\"]/tbody/tr[1]/td");

	private static final By NEW_COD_ID = By.xpath("//*[@id=\"conceptTable\"]/tbody/tr[1]/td");

	private static final By ADMINISTRATION = By.id("administrationNavLink");

	private static final By ADVANCED_SETTINGS = By.linkText("Advanced Settings");

	private static final By CONCEPT_VALUE = By.xpath("//*[@id=\"globalPropsList\"]/tr[125]/td[2]/input");

	private static final By SAVE_CONCEPT_VALUE = By.xpath("//*[@id=\"buttonsAtBottom\"]/input[1]");

	private static final By HOME = By.id("homeNavLink");

	String conceptname = "Cause Of Death";

	private static final By MARK_PATIENT_DECEASED = By.id("org.openmrs.module.coreapps.markPatientDead");

	private static final By CHECK_BOX = By.id("deceased");

	private static final By DATE_PICKER = By.id("death-date-display");

	private static final By SAVE_DECEASED = By.xpath("//*[@id=\"mark-patient-dead\"]/fieldset/p[3]/span[2]/input");

	private static final By ACTIVE_DAY = By.cssSelector(".datetimepicker .day.active");

	private static final By ACTIVE_HOUR = By.cssSelector(".datetimepicker .hour.active");

	private static final By ACTIVE_MINUTE = By.cssSelector(".datetimepicker .minute.active");

	private static final By DEATH_MESSAGE = By.className("death-message");

	public MarkPatientDeceasedPage(Page page) {
		super(page);

	}

	public void getConcept() {
		findElement(DICTIONARY).click();
		Concept();
		findElement(HOME).click();
	}

	/*
	 * If there is a concept called Cause Of Death, The method gets its ID
	 * if there is no concept at all or no concept called Cause of death, the method creates one
	 * and copies its ID
	 */
	public void Concept() {
		findElement(SEARCH).sendKeys("cause of death");
		String message = findElement(CONCEPT_SEARCH_MESSAGE).getText();
		if (message.contains("1 entries")) {
			System.out.println("Concept already exists");
			getConceptid(getExistingId());

		}

			newConcept();
			getConceptid(newId());



	}

	public void markPatientDead() {
		findElement(MARK_PATIENT_DECEASED).click();
		findElement(CHECK_BOX).click();
		findElement(DATE_PICKER).click();
		pickDate();
		Select dropdown = new Select(findElement(By.id("cause-of-death")));
		dropdown.selectByValue("140238AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		findElement(SAVE_DECEASED).click();
	}

	public void newConcept() {
		findElement(ADD_NEW_CONCEPT).click();
		findElement(CONCEPT_NAME).sendKeys("cause of death");
		Select datatype = new Select(findElement(By.id("datatype")));
		datatype.selectByVisibleText("Coded");
		findElement(ADD_ALERGY).click();
		findElement(CAUSEBOX).sendKeys("Fever");
		findElement(FEVER).click();
		findElement(ADDBUTTON).click();
		findElement(SAVE).click();
	}

	public void getConceptid(String ID) {

		findElement(ADMINISTRATION).click();
		findElement(ADVANCED_SETTINGS).click();
		findElement(CONCEPT_VALUE).clear();
		findElement(CONCEPT_VALUE).sendKeys(ID);
		findElement(SAVE_CONCEPT_VALUE).click();

	}

	public void pickDate() {
		findElement(ACTIVE_DAY).click();
		findElement(ACTIVE_HOUR).click();
		findElement(ACTIVE_MINUTE).click();
	}

	public String getExistingId() {
		findElement(COD).click();
		String existingId = findElement(COD_ID).getText();
		return existingId;
	}

	public String newId() {
		String newId = findElement(NEW_COD_ID).getText();
		return newId;
	}

	public String confirmDeadMessage() {
		String confirmdeadMessage = findElement(DEATH_MESSAGE).getText();
		return confirmdeadMessage;
	}

	@Override
	public String getPageUrl() {
		return URL;
	}
}
