package org.openmrs.reference.page;

/**
 * Created by nata on 30.06.15.
 */


import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DataManagementPage extends Page {
    public DataManagementPage(WebDriver driver) {
        super(driver);
    }

    private final static By MERGE_PATIENT_ELECTRONIC_RECORD = By.id("coreapps-mergePatientsHomepageLink-app");
    private final static By ID_PATIENT_1 = By.id("patient1-text");
    public final static By ID_PATIENT_2 = By.id("patient2-text");
    public final static By CONTINUE = By.id("confirm-button");
    private final static By MERGE_PATIENT = By.id("second-patient");
    private final static By NO = By.id("cancel-button");
    private final static By SEARCH = By.id("patient-search");

    public void goToMegrePatient(){ clickOn(MERGE_PATIENT_ELECTRONIC_RECORD);}
    public void enterPatient1( String patient1){
        setText(ID_PATIENT_1, patient1);
    }
    public void enterPatient2(String patient2){
        setText(ID_PATIENT_2, patient2);
    }
    public void clickOnContinue(){
        waitForElementToBeEnabled(CONTINUE);
        clickOn(CONTINUE);}
    public void clickOnMergePatient(){
        waitForElement(MERGE_PATIENT);
        clickOn(MERGE_PATIENT);}
    public void clickOnNo(){ clickOn(NO);}
    public void waitCont() throws InterruptedException {
    	clickOn(CONTINUE);
    }
    public void searchId(String id){
        setTextToFieldNoEnter(SEARCH, id);
    }
    public String search(){
        return findElement(SEARCH).getText();}
    @Override
    public String expectedUrlPath() {
        return URL_ROOT + "/datamanagement/dataManagement.page";
    }
}