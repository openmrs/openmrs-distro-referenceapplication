package org.openmrs.reference.page;

/**
 * Created by nata on 30.06.15.
 */


import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FindPatientPage extends AbstractBasePage {
    public FindPatientPage(WebDriver driver) {
        super(driver);
    }

    private static final By PATIENT_SEARCH = By.id("patient-search");
    public static final By PATIENT_SEARCH_RESULT = By.id("patient-search-results-table");
    public static final By PATIENT_NAME_SEARCH_RESULT = By.xpath("//table[@id='patient-search-results-table']/tbody/tr/td[2]");



    public void enterPatient(String patient) {
        setTextToFieldNoEnter(PATIENT_SEARCH, patient);

    }

    public WebElement nameSearchResult() {
        return findElement(PATIENT_NAME_SEARCH_RESULT);
    }
    

    @Override
    public String expectedUrlPath() {
        return URL_ROOT + "/findpatient/findPatient.page";
    }
}