package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created by tomasz on 25.05.15.
 */
public class CaptureVitalsPage extends AbstractBasePage {

    private static final By PATIENT_SEARCH_FORM = By.id("patient-search-form");
    private static final By FIRST_FOUND_PATIENT = By.className("odd");
    public CaptureVitalsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public String expectedUrlPath() {
        return URL_ROOT + "/coreapps/findpatient/findPatient.page?app=referenceapplication.vitals";
    }

    public void search(String text) {
        WebElement searchField = findElement(PATIENT_SEARCH_FORM);
        try {
            searchField.sendKeys(text);
        } catch(Exception e) {
            e.printStackTrace();
        }

        clickOn(FIRST_FOUND_PATIENT);
    }


}
