package org.openmrs.reference.page;


import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public class ConceptFormPage extends Page{
    public ConceptFormPage(WebDriver driver) {
        super(driver);
    }

    private boolean acceptNextAlert = true;
    private static final By VIEW_CONCEPT_DICTIONARY = By.linkText("View Concept Dictionary");
    private static final By ADD_NEW_CONCEPT = By.linkText("Add new Concept");
    private static final By FULLY_NAME = By.id("namesByLocale[en].name");
    private static final By CONCEPT_CLASS = By.name("concept.conceptClass");
    private static final By SAVE_CONCEPT = By.xpath("//input[@value='Save Concept]");
    private static final By FIND_CONCEPT = By.id("inputNode");
    public String CONCEPT;
    private static final By EDIT = By.linkText("Edit");
    private static final By DELETE_CONCEPT = By.xpath("(//input[@name='action'])[4]");
    private static By ADDED_DRUG;
    private static final By SAVE_EDIT = By.xpath("//input[@name='action']");
    private static final By RETIRE = By.xpath("(//input[@name='action'])[5]");
    private static final By UNRETIRE = By.xpath("//div[@id='content']/div[3]/div[3]/form/input");
    private static final By FINDED_CONCEPT = By.cssSelector("tr.odd > td > span");



    public void clickOnViewConceptDictionary(){ clickOn(VIEW_CONCEPT_DICTIONARY);}
    public void clickOnAddNewConcept(){ clickOn(ADD_NEW_CONCEPT);}
    public void enterFullyName(String concept){
        setText(FULLY_NAME, concept);
        CONCEPT = concept;
    }
    public void selectClass(String selclass){
        selectFrom(CONCEPT_CLASS, selclass);
    }
    public void clickOnSaveConcept(){ clickOn(SAVE_CONCEPT);}
    public void findAddedConcept(String find){
        setText(FIND_CONCEPT, find);
        CONCEPT = find;
    }
    public boolean conceptExist(String find){
        try {
            return findElement(FINDED_CONCEPT).getText().equals(find);
        }
        catch (Exception ex) {
            return false;
        }
    }



    public void clickOnAddedConcept(){
        ADDED_DRUG = By.xpath("//table[@id='openmrsSearchTable']/tbody/tr/td");
        waitForElement(ADDED_DRUG);
        clickOn(ADDED_DRUG);
    }
    public void editConcept(){
        waitForElement(EDIT);
        clickOn(EDIT);
    }
    public void deleteConcept(){
        clickOn(DELETE_CONCEPT);
    }
    public void saveEdit(){ clickOn(SAVE_EDIT);}
    public String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
    public void retire(){
        clickOn(RETIRE);
        clickOn(UNRETIRE);
    }



    @Override
    public String getPageUrl() {
        return "/admin/maintenance/settings.list";
    }


}
