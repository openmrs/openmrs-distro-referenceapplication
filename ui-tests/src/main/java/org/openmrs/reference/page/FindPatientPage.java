package org.openmrs.reference.page;

/**
 * Created by nata on 30.06.15.
 */


import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class FindPatientPage extends Page {

    public static final By PATIENT_SEARCH_RESULT = By.id("patient-search-results-table");
    public static final By PATIENT_NAME_SEARCH_RESULT = By.xpath("//table[@id='patient-search-results-table']/tbody/tr/td[2]");
    public static final By PATIENT_ID_SEARCH_RESULT = By.xpath("//table[@id='patient-search-results-table']/tbody/tr/td[1]");
    private static final By PATIENT_SEARCH = By.id("patient-search");
    private static final By PATIENT = By.xpath("//table[@id='patient-search-results-table']/tbody/tr/td[2]");

    public FindPatientPage(WebDriver driver) {
        super(driver);
    }


    public void enterPatient(String patient) {
        setTextToFieldNoEnter(PATIENT_SEARCH, patient);
    }

    public void clickOnFirstPatient(){ clickOn(PATIENT);}

    /**
     * Finds first record from the result table
     * @return patient id
     */
    public String getFirstPatientIdentifier() {
        return findElement(PATIENT_ID_SEARCH_RESULT).getText();
    }
    
    public String findFirstPatientName() {
        return findElement(PATIENT_NAME_SEARCH_RESULT).getText();
    }


    @Override
    public String getPageUrl() {
        return "/findpatient/findPatient.page";
    }
}