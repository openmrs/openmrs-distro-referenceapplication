package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class EditVitalsPage extends Page {

    public static final String URL_PATH = "/htmlformentryui/htmlform/editHtmlFormWithStandardUi.page";
    private static final By HEIGHT_FIELD = By.id("w8");
    private static final By WEIGHT_FIELD = By.id("w10");
    private static final By TEMPERATURE_FIELD = By.id("w12");
    private static final By PULSE_FIELD = By.id("w14");
    private static final By RESPIRATORY_FIELD = By.id("w16");
    private static final By BLOOD_PRESSURE_FIELD_1 = By.id("w18");
    private static final By BLOOD_PRESSURE_FIELD_2 = By.id("w20");
    private static final By BLOOD_OXYGEN_SATURATION_FIELD = By.id("w22");
    private static final By SAVE_CHANGES_BUTTON = By.xpath("//input[@type='button']");

    public EditVitalsPage(Page page) {
        super(page);
    }

    public void clearPatientHeight() {
        findElement(HEIGHT_FIELD).clear();
    }
    
    public void setHeightField(String value) {
        WebElement heightField = findElement(HEIGHT_FIELD);
        heightField.clear();
        heightField.sendKeys(value);
    }

    public void clearPatientWeight() {
        findElement(WEIGHT_FIELD).clear();
    }
    
    public void setWeightField(String value) {
        WebElement weightField = findElement(WEIGHT_FIELD);
        weightField.clear();
        weightField.sendKeys(value);
    }

    public void clearPatientTemperature() {
        findElement(TEMPERATURE_FIELD).clear();
    }
    
    public void setTemperatureField(String value) {
        WebElement temperatureField = findElement(TEMPERATURE_FIELD);
        temperatureField.clear();
        temperatureField.sendKeys(value);
    }

    public void clearPatientPulse() {
        findElement(PULSE_FIELD).clear();
    }
    
    public void setPulseField(String value) {
        WebElement pulseField = findElement(PULSE_FIELD);
        pulseField.clear();
        pulseField.sendKeys(value);
    }

    public void clearPatientRespiratoryRate() {
        findElement(RESPIRATORY_FIELD).clear();
    }
    
    public void setRespiratoryField(String value) {
        WebElement respiratoryField = findElement(RESPIRATORY_FIELD);
        respiratoryField.clear();
        respiratoryField.sendKeys(value);
    }

    public void clearPatientBloodPressure1() {
        findElement(BLOOD_PRESSURE_FIELD_1).clear();
    }
  
    public void clearPatientBloodPressure2() {
        findElement(BLOOD_PRESSURE_FIELD_2).clear();
    }
    
    public void setBloodPressureFields(String value1, String value2) {
        WebElement bloodPressureField1 = findElement(BLOOD_PRESSURE_FIELD_1);
        bloodPressureField1.clear();
        bloodPressureField1.sendKeys(value1);

        WebElement bloodPressureField2 = findElement(BLOOD_PRESSURE_FIELD_2);
        bloodPressureField2.clear();
        bloodPressureField2.sendKeys(value2);
    }

    public void clearPatientBloodOxygenSaturation() {
        findElement(BLOOD_OXYGEN_SATURATION_FIELD).clear();
    }
    
    public void setBloodOxygenSaturationField(String value) {
        WebElement bloodOxygenSaturationField = findElement(BLOOD_OXYGEN_SATURATION_FIELD);
        bloodOxygenSaturationField.clear();
        bloodOxygenSaturationField.sendKeys(value);
    }

    public void saveChanges() {
        clickOn(SAVE_CHANGES_BUTTON);
    }
    
    @Override
    public String getPageUrl() {
        return URL_PATH;
    }
}
