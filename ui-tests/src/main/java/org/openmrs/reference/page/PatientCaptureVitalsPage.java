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

    public static final String URL_PATH = "/coreapps/vitals/patient.page";
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
        return URL_ROOT + URL_PATH;
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
