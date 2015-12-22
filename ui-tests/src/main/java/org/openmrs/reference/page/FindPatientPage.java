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
    public static final int SLEEP_TIME = 1000; //1 second

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
    public String findFirstPatientId() {
        return findElement(PATIENT_ID_SEARCH_RESULT).getText();
    }


    

    @Override
    public String expectedUrlPath() {
        return URL_ROOT + "/findpatient/findPatient.page";
    }

    /**
     * waits for results appear in a table
     */
    public void waitForResultTable() throws InterruptedException {
        int sleepTime = SLEEP_TIME;
        while (sleepTime < SLEEP_TIME * 10) {
            Thread.sleep(sleepTime);
            if (findElement(PATIENT_NAME_SEARCH_RESULT) != null) {
                return;
            }
            sleepTime = sleepTime * 2;
        }
    }
}