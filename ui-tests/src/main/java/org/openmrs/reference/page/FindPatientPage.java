package org.openmrs.reference.page;

/**
 * Created by nata on 30.06.15.
 */


import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FindPatientPage extends AbstractBasePage {

    public static final String DATA_TABLES_EMPTY = ".dataTables_empty";
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
    public String findFirstPatientId() {
        return findElement(PATIENT_ID_SEARCH_RESULT).getText();
    }

    public WebElement nameSearchResult() {
        return findElement(PATIENT_NAME_SEARCH_RESULT);
    }

    

    @Override
    public String expectedUrlPath() {
        return URL_ROOT + "/findpatient/findPatient.page";
    }

    /**
     * waits for results appear in a table
     */
    public void waitForResultTable() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        //wait for table to be cleared from previous results
        wait.until(ExpectedConditions.visibilityOf(findElement(By.cssSelector(DATA_TABLES_EMPTY))));
        //wait for the first row of the table
        wait.until(ExpectedConditions.visibilityOf(findElement(PATIENT_NAME_SEARCH_RESULT)));
    }
}