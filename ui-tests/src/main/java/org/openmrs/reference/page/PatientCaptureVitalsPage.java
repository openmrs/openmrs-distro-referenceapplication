package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by tomasz on 26.05.15.
 */
public class PatientCaptureVitalsPage extends AbstractBasePage {

    private static final By HEIGHT_FIELD = By.id("height");
    private static final By WEIGHT_FIELD = By.id("weight");
    private static final By TEMPERATURE_FIELD = By.id("temperature");
    private static final By PULSE_FIELD = By.id("heart_rate");
    private static final By RESPIRATORY_FIELD = By.id("respiratory_rate");
    private static final By BLOOD_PRESSURE_FIELD_1 = By.id("bp_systolic");
    private static final By BLOOD_PRESSURE_FIELD_2 = By.id("bp_diastolic");
    private static final By BLOOD_OXYGEN_SATURATION_FIELD = By.id("o2_sat");
    private static final By CONFIRM_BUTTON = By.className("submitButton");
    private static final By CONFIRM_BUTTON_2 = By.id("coreapps-vitals-confirm");

    public PatientCaptureVitalsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public String expectedUrlPath() {
        return URL_ROOT + "htmlformentryui/htmlform/enterHtmlFormWithSimpleUi.page?patientId=631&visitId=598&definitionUiResource=referenceapplication%3Ahtmlforms%2Fvitals.xml&returnUrl=%2Fopenmrs%2Fcoreapps%2Ffindpatient%2FfindPatient.page%3Fapp%3Dreferenceapplication.vitals&breadcrumbOverride=[{\"label\"%3A\"Capture Vitals\"%2C\"link\"%3A\"%2Fopenmrs%2Fcoreapps%2Ffindpatient%2FfindPatient.page%3Fapp%3Dreferenceapplication.vitals\"}%2C{\"label\"%3A\"Smith%2C Bob\"%2C\"link\"%3A\"%2Fopenmrs%2Fcoreapps%2Fvitals%2Fpatient.page%3FpatientId%3Dcee4c4ec-678d-4871-a3a6-fa5f18e7a6c9%26\"}]&";
    }

    public void setHeightField(String value) {
        WebElement heightField = findElement(HEIGHT_FIELD);
        try {
            heightField.sendKeys(value);
        } catch(Exception e) {

        }
    }

    public void setWeightField(String value) {
        WebElement weightField = findElement(WEIGHT_FIELD);
        try {
            weightField.sendKeys(value);
        } catch(Exception e) {

        }
    }

    public void setTemperatureField(String value) {
        WebElement temperatureField = findElement(TEMPERATURE_FIELD);
        try {
            temperatureField.sendKeys(value);
        } catch(Exception e) {

        }
    }

    public void setPulseField(String value) {
        WebElement heightField = findElement(PULSE_FIELD);
        heightField.sendKeys(value);
    }

    public void setRespiratoryField(String value) {
        WebElement respiratoryField = findElement(RESPIRATORY_FIELD);
        try {
            respiratoryField.sendKeys(value);
        } catch (Exception e) {

        }
    }

    public void setBloodPressureFields(String value1, String value2) {
        WebElement bloodPressureField1 = findElement(BLOOD_PRESSURE_FIELD_1);
        try {
            bloodPressureField1.sendKeys(value1);
        } catch(Exception e) {

        }

        WebElement bloodPressureField2 = findElement(BLOOD_PRESSURE_FIELD_2);
        try {
            bloodPressureField2.sendKeys(value2);
        } catch(Exception e) {

        }
    }

    public void setBloodOxygenSaturationField(String value) {
        WebElement bloodOxygenSaturationField = findElement(BLOOD_OXYGEN_SATURATION_FIELD);
        try {
            bloodOxygenSaturationField.sendKeys(value);
        } catch(Exception e) {

        }
    }

    public void confirm() {

        WebElement confirmButton = findElement(CONFIRM_BUTTON);
        confirmButton.click();

    }

    public void checkIfRightPatient() {
        WebElement confirmButton2 = findElement(CONFIRM_BUTTON_2);
        //WebDriverWait waiter = new WebDriverWait(driver, 30L);
        if(confirmButton2 != null) {
            //waiter.until(ExpectedConditions.elementToBeClickable(CONFIRM_BUTTON_2));
            confirmButton2.click();
        }
    }
}
