package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by tomasz on 25.05.15.
 */
public class CaptureVitalsPage extends Page {

    private static final By PATIENT_SEARCH = By.id("patient-search");
    private static final By FIRST_FOUND_PATIENT = By.cssSelector("i.icon-vitals");

    public CaptureVitalsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public String getPageUrl() {
        return "/coreapps/findpatient/findPatient.page?app=referenceapplication.vitals";
    }

//    public void search(String text) {
//        WebElement searchField = findElement(PATIENT_SEARCH);
//        try {
//            searchField.sendKeys(text);
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//        clickOn(FIRST_FOUND_PATIENT);
//    }
}
