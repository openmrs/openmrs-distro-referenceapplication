package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

public class MarkPatientDeceasedPage extends Page {
	
	private static final String URL = "/coreapps/markPatientDead.page";
	private static final By DICTIONARY = By.id("dictionaryNavLink");
	private static final By ADD_NEW_CONCEPT = By.linkText("Add new Concept");
	private static final By CONCEPT_NAME = By.id("namesByLocale[en].name");
	private static final By ADD_ANSWER= By.cssSelector("#codedDatatypeRow > td > table > tbody > tr > td.buttons > input:nth-child(1)");
	private static final By CAUSEBOX = By.id("newAnswerConcept");
	private static final By FEVER = By.xpath("/html/body/ul[1]/li[2]/a");
	private static final By ADDBUTTON = By.xpath("/html/body/div[5]/div[11]/button[1]");
	private static final By SAVE = By.cssSelector("#saveDeleteButtons > input:nth-child(1)");
	private static final By SEARCH = By.id("inputNode");
	private static final By COD = By.xpath("//*[@id=\"openmrsSearchTable\"]/tbody/tr[1]/td/span");
	private static final By NEW_COD_ID = By.xpath("//*[@id=\"conceptTable\"]/tbody/tr[1]/td");
	private static final By ADMINISTRATION = By.id("administrationNavLink");
	private static final By ADVANCED_SETTINGS = By.linkText("Advanced Settings");
	private static final By CONCEPT_VALUE = By.xpath("//*[@id=\"globalPropsList\"]/tr[133]/td[2]/input");
	private static final By SAVE_CONCEPT_VALUE = By.xpath("//*[@id=\"buttonsAtBottom\"]/input[1]");
	private static final By HOME = By.id("homeNavLink");
	private static final By MARK_PATIENT_DECEASED = By.id("org.openmrs.module.coreapps.markPatientDead");
	private static final By CHECK_BOX = By.id("deceased");
	private static final By DATE_PICKER = By.id("death-date-display");
	private static final By SAVE_DECEASED = By.cssSelector("#mark-patient-dead > fieldset > p:nth-child(4) > span:nth-child(2) > input");
	private static final By ACTIVE_DAY = By.cssSelector("td.day.active");
	private static final By DEATH_MESSAGE = By.className("death-message");
	private static final By EDIT = By.id("editConcept");
	private static final By DELETE_BUTTON = By.cssSelector("#saveDeleteButtons > input[type=submit]:nth-child(4)");
	
	public MarkPatientDeceasedPage(Page page) {
		super(page);
	}
	
	public void getConcept() {
		findElement(DICTIONARY).click();
		Concept();
		findElement(HOME).click();
	}
	
	/*
	 * If there is a concept called Cause Of Death, The method gets its concept code
	 * if there is no concept at all or no concept called Cause of death, the method creates one
	 * and copies its concept code
	 */
	public void Concept() {
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
		findElement(ADD_ANSWER).click();
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
	
	public void deleteConcept() {
		findElement(DICTIONARY).click();
		findElement(SEARCH).sendKeys("cause of death");
		findElement(COD).click();
		findElement(EDIT).click();
		findElement(DELETE_BUTTON).click();
		Alert alert = driver.switchTo().alert();
		alert.accept();
	}
	
	public void pickDate() {
		findElement(ACTIVE_DAY).click();
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
