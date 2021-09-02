package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class MarkPatientDeceasedPage extends Page {

    private static final String URL = "/coreapps/markPatientDead.page";
    private static final By DICTIONARY_LINK= By.id("dictionaryNavLink");
    private static final By ADD_NEW_CONCEPT = By.linkText("Add new Concept");
    private static final By CONCEPT_NAME = By.id("namesByLocale[en].name");
    private static final By ADD_ANSWER_BUTTON = By.cssSelector("#codedDatatypeRow > td > table > tbody > tr > td.buttons > input:nth-child(1)");
    private static final By CAUSE_BOX = By.id("newAnswerConcept");
    private static final By FEVER = By.xpath("/html/body/ul[1]/li[2]/a");
    private static final By ADD_BUTTON = By.xpath("/html/body/div[5]/div[11]/button[1]");
    private static final By SAVE_CONCEPT_BUTTON = By.xpath("/html/body/div[1]/div[3]/div[2]/form/div/input[1]");
    private static final By SEARCH_CONCEPT_FIELD = By.id("inputNode");
    private static final By FIRST_RESULT = By.xpath("//*[@id=\"openmrsSearchTable\"]/tbody/tr[1]/td/span");
    private static final By CONCEPT_ID = By.xpath("//*[@id=\"conceptTable\"]/tbody/tr[1]/td");
    private static final By ADMINISTRATION_PAGE = By.id("administrationNavLink");
    private static final By ADVANCED_SETTINGS = By.linkText("Advanced Settings");
    private static final By CONCEPT_VALUE_FIELD = By.xpath("//*[@id=\"globalPropsList\"]/tr[133]/td[2]/input");
    private static final By SAVE_CONCEPT_VALUE = By.xpath("//*[@id=\"buttonsAtBottom\"]/input[1]");
    private static final By HOME_LINK = By.id("homeNavLink");
    private static final By MARK_PATIENT_DECEASED = By.id("org.openmrs.module.coreapps.markPatientDead");
    private static final By CHECK_BOX = By.id("deceased");
    private static final By DATE_PICKER = By.id("death-date-display");
    private static final By SAVE_DECEASED_BUTTON= By.cssSelector("#mark-patient-dead > fieldset > p:nth-child(4) > span:nth-child(2) > input");
    private static final By ACTIVE_DAY = By.cssSelector("td.day.active");
    private static final By DEATH_MESSAGE = By.className("death-message");
    private static final By EDIT_CONCEPT = By.id("editConcept");
    private static final By DELETE_BUTTON = By.cssSelector("#saveDeleteButtons > input[type=submit]:nth-child(4)");

    public MarkPatientDeceasedPage(Page page) {
        super(page);
    }

    public void getConcept() {
        findElement(DICTIONARY_LINK).click();
        Concept();
        findElement(HOME_LINK).click();
    }

    /*
     * If there is a concept called Cause Of Death, The method gets its concept code
     * if there is no concept at all or no concept called Cause of death, the method
     * creates one and copies its concept code
     */
    public void Concept() {
        newConcept();
        enterConceptid(getConceptId());
    }

    public void clickOnMarkPatientDead() {
        findElement(MARK_PATIENT_DECEASED).click();
        findElement(CHECK_BOX).click();
        findElement(DATE_PICKER).click();
        pickDate();
        Select dropdown = new Select(findElement(By.id("cause-of-death")));
        dropdown.selectByValue("140238AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        findElement(SAVE_DECEASED_BUTTON).click();
    }

    public void newConcept() {
        findElement(ADD_NEW_CONCEPT).click();
        findElement(CONCEPT_NAME).sendKeys("cause of death");
        Select datatype = new Select(findElement(By.id("datatype")));
        datatype.selectByVisibleText("Coded");
        findElement(ADD_ANSWER_BUTTON).click();
        findElement(CAUSE_BOX).sendKeys("Fever");
        findElement(FEVER).click();
        findElement(ADD_BUTTON).click();
        waiter.until(ExpectedConditions.elementToBeClickable(SAVE_CONCEPT_BUTTON));
        findElement(SAVE_CONCEPT_BUTTON).click();
    }

    public void enterConceptid(String ID) {
        findElement(ADMINISTRATION_PAGE).click();
        findElement(ADVANCED_SETTINGS).click();
        findElement(CONCEPT_VALUE_FIELD).clear();
        findElement(CONCEPT_VALUE_FIELD).sendKeys(ID);
        findElement(SAVE_CONCEPT_VALUE).click();
    }

    public void deleteConcept() {
        findElement(DICTIONARY_LINK).click();
        findElement(SEARCH_CONCEPT_FIELD).sendKeys("cause of death");
        findElement(FIRST_RESULT).click();
        findElement(EDIT_CONCEPT).click();
        findElement(DELETE_BUTTON).click();
        Alert alert = driver.switchTo().alert();
        alert.accept();
    }

    public void pickDate() {
        findElement(ACTIVE_DAY).click();
    }

    public String getConceptId() {
        String conceptId = findElement(CONCEPT_ID).getText();
        return conceptId;
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
