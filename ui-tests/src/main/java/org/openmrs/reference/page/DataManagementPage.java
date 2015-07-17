package org.openmrs.reference.page;

/**
 * Created by nata on 30.06.15.
 */


import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DataManagementPage extends AbstractBasePage {
    public DataManagementPage(WebDriver driver) {
        super(driver);
    }

    private final static By MERGE_PATIENT_ELECTRONIC_RECORD = By.id("coreapps-mergePatientsHomepageLink-app");
    private final static By ID_PATIENT_1 = By.id("patient1-text");
    private final static By ID_PATIENT_2 = By.id("patient2-text");
    private final static By CONTINUE = By.id("confirm-button");
    private final static By MERGE_PATIENT = By.id("second-patient");

    public void goToMegrePatient(){ clickOn(MERGE_PATIENT_ELECTRONIC_RECORD);}
    public void enterPatient1( String patient1){
        setText(ID_PATIENT_1, patient1);
    }
    public void enterPatient2(String patient2){
        setText(ID_PATIENT_2, patient2);
    }
    public void clickOnContinue(){ clickOn(CONTINUE);}
    public void clickOnMergePatient(){ clickOn(MERGE_PATIENT);}


    @Override
    public String expectedUrlPath() {
        return URL_ROOT + "/datamanagement/dataManagement.page";
    }
}