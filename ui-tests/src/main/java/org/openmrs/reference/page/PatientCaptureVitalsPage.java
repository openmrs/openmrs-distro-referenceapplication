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
    private static final By HEIGHT_FIELD = By.id("w8");
    private static final By WEIGHT_FIELD = By.id("w10");
    private static final By TEMPERATURE_FIELD = By.id("w12");
    private static final By PULSE_FIELD = By.id("w14");
    private static final By RESPIRATORY_FIELD = By.id("w16");
    private static final By BLOOD_PRESSURE_FIELD_1 = By.id("w18");
    private static final By BLOOD_PRESSURE_FIELD_2 = By.id("w20");
    private static final By BLOOD_OXYGEN_SATURATION_FIELD = By.id("w22");
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
            heightField.clear();
            heightField.sendKeys(value);
            findElement(By.xpath("//ul[@id='formBreadcrumb']/li/ul/li[2]")).click();
        } catch(Exception e) {

        }
    }

    public void setWeightField(String value) {
        WebElement weightField = findElement(WEIGHT_FIELD);
        try {
            weightField.clear();
            weightField.sendKeys(value);
            findElement(By.xpath("//ul[@id='formBreadcrumb']/li/ul/li[4]")).click();
        } catch(Exception e) {

        }
    }

    public void setTemperatureField(String value) {
        WebElement temperatureField = findElement(TEMPERATURE_FIELD);
        try {
            temperatureField.clear();
            temperatureField.sendKeys(value);
            findElement(By.xpath("//ul[@id='formBreadcrumb']/li/ul/li[5]")).click();
        } catch(Exception e) {

        }
    }

    public void setPulseField(String value) {
        WebElement pulseField = findElement(PULSE_FIELD);
        try {
            pulseField.clear();
            pulseField.sendKeys(value);
            findElement(By.xpath("//ul[@id='formBreadcrumb']/li/ul/li[6]")).click();
        } catch(Exception e) {

        }
    }

    public void setRespiratoryField(String value) {
        WebElement respiratoryField = findElement(RESPIRATORY_FIELD);
        try {
            respiratoryField.clear();
            respiratoryField.sendKeys(value);
            findElement(By.xpath("//ul[@id='formBreadcrumb']/li/ul/li[7]")).click();
        } catch(Exception e) {

        }
    }

    public void setBloodPressureFields(String value1, String value2) {
        WebElement bloodPressureField1 = findElement(BLOOD_PRESSURE_FIELD_1);
        try {
            bloodPressureField1.clear();
            bloodPressureField1.sendKeys(value1);
        } catch(Exception e) {

        }

        WebElement bloodPressureField2 = findElement(BLOOD_PRESSURE_FIELD_2);
        try {
            bloodPressureField2.clear();
            bloodPressureField2.sendKeys(value2);
            findElement(By.xpath("//ul[@id='formBreadcrumb']/li/ul/li[8]")).click();
        } catch(Exception e) {

        }
    }

    public void setBloodOxygenSaturationField(String value) {
        WebElement bloodOxygenSaturationField = findElement(BLOOD_OXYGEN_SATURATION_FIELD);
        try {
            bloodOxygenSaturationField.clear();
            bloodOxygenSaturationField.sendKeys(value);
            findElement(By.xpath("//ul[@id='formBreadcrumb']/li/ul/li[2]")).click();
        } catch (Exception e) {

        }
    }
    public void confirm() {
        try {
            WebElement confirmButton = findElement(CONFIRM_BUTTON);
            confirmButton.click();
        } catch(Exception e) {

        }
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
