package org.openmrs.reference.page;

import static org.junit.Assert.assertTrue;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class MarkPatientDeceasedPage extends Page {

    private static final String URL = "/coreapps/markPatientDead.page";
    private static final By DICTIONARY_LINK= By.id("dictionaryNavLink");
    private static final By ADD_NEW_CONCEPT_LINK = By.linkText("Add new Concept");
    private static final By CONCEPT_NAME = By.id("namesByLocale[en].name");
    private static final By ADD_ANSWER_BUTTON = By.cssSelector("#codedDatatypeRow > td > table > tbody > tr > td.buttons > input:nth-child(1)");
    private static final By CAUSE_SEARCH_FIELD = By.id("newAnswerConcept");
    private static final By FIRST_CAUSE_OPTION_LINK = By.className("autocompleteresult");
    private static final By ADD_BUTTON = By.className("ui-button-text");
    private static final By SAVE_CONCEPT_BUTTON = By.cssSelector("#saveDeleteButtons > input[type=submit]:nth-child(1)");
    private static final By SEARCH_CONCEPT_FIELD = By.id("inputNode");
    private static final By FIRST_RESULT_LINK = By.className("odd");
    private static final By CONCEPT_ID_VALUE = By.cssSelector("#conceptTable > tbody > tr:nth-child(1) > td");
    private static final By ADMINISTRATION_PAGE = By.id("administrationNavLink");
    private static final By ADVANCED_SETTINGS_LINK = By.linkText("Advanced Settings");
    private static final By CONCEPT_VALUE_FIELD = By.cssSelector("#globalPropsList > tr:nth-child(133) > td:nth-child(2) > input[type=text]");
    private static final By SAVE_CONCEPT_VALUE = By.name("action");
    private static final By HOME_LINK = By.id("homeNavLink");
    private static final By MARK_PATIENT_DECEASED_LINK = By.id("org.openmrs.module.coreapps.markPatientDead");
    private static final By MARK_PATIENT_DECEASED_CHECK_BOX = By.id("deceased");
    private static final By DATE_PICKER_BUTTON = By.id("death-date-display");
    private static final By SAVE_DECEASED_BUTTON= By.cssSelector("#mark-patient-dead > fieldset > p:nth-child(4) > span:nth-child(2) > input");
    private static final By ACTIVE_DAY_CELL = By.cssSelector("td.day.active");
    private static final By DEATH_MESSAGE_TEXT = By.className("death-message");
    private static final By EDIT_CONCEPT_LINK = By.id("editConcept");
    private static final By DELETE_BUTTON = By.cssSelector("#saveDeleteButtons > input[type=submit]:nth-child(4)");
    private static final By DELETE_CONFIRMATION = By.id("openmrs_msg");

    public MarkPatientDeceasedPage(Page page) {
        super(page);
    }

    public void getConcept() {
        clickOn(DICTIONARY_LINK); 
        Concept();
        clickOn(HOME_LINK); 
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
        clickOn(MARK_PATIENT_DECEASED_LINK); 
        clickOn(MARK_PATIENT_DECEASED_CHECK_BOX);
        clickOn(DATE_PICKER_BUTTON);
        pickDate();
        Select dropdown = new Select(findElement(By.id("cause-of-death")));
        dropdown.selectByValue("113230AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        clickOn(SAVE_DECEASED_BUTTON); 
    }

    public void newConcept() {
        clickOn(ADD_NEW_CONCEPT_LINK); 
        findElement(CONCEPT_NAME).sendKeys("cause of death");
        Select datatype = new Select(findElement(By.id("datatype")));
        datatype.selectByVisibleText("Coded");
        clickOn(ADD_ANSWER_BUTTON); 
        findElement(CAUSE_SEARCH_FIELD).sendKeys("Fever");
        clickOn(FIRST_CAUSE_OPTION_LINK); 
        clickOn(ADD_BUTTON);
        waiter.until(ExpectedConditions.elementToBeClickable(SAVE_CONCEPT_BUTTON));
        clickOn(SAVE_CONCEPT_BUTTON); 
    }

    public void enterConceptid(String ID) {
        clickOn(ADMINISTRATION_PAGE);
        clickOn(ADVANCED_SETTINGS_LINK);
        findElement(CONCEPT_VALUE_FIELD).clear();
        findElement(CONCEPT_VALUE_FIELD).sendKeys(ID);
        clickOn(SAVE_CONCEPT_VALUE);
    }

    public void deleteConcept() {
        clickOn(DICTIONARY_LINK);
        findElement(SEARCH_CONCEPT_FIELD).sendKeys("cause of death");
        clickOn(FIRST_RESULT_LINK);
        clickOn(EDIT_CONCEPT_LINK);
        clickOn(DELETE_BUTTON);
        Alert alert = driver.switchTo().alert();
        alert.accept();
        String delete_message = findElement(DELETE_CONFIRMATION).getText();
        assertTrue(delete_message.contains("Concept deleted successfully"));
    }

    public void pickDate() {
        findElement(ACTIVE_DAY_CELL).click();
    }

    public String getConceptId() {
        String conceptId = findElement(CONCEPT_ID_VALUE).getText();
        return conceptId;
    }

    public String confirmDeadMessage() {
        String confirmdeadMessage = findElement(DEATH_MESSAGE_TEXT).getText();
        return confirmdeadMessage;
    }

    @Override
    public String getPageUrl() {
        return URL;
    }
}
