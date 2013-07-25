package org.openmrs.reference.page;


import org.openqa.selenium.WebDriver;

public class DispensingMedicationPage extends AbstractBasePage{

    public DispensingMedicationPage(WebDriver driver) {
        super(driver);
    }

    static final String dispensingBar = "//ul[@id='formBreadcrumb']/li[1]/span";   //Once we get an id, this xpath should be replaced
    static final String confirmBar = "//ul[@id='formBreadcrumb']/li[2]/span";      //Once we get an id, this xpath should be replaced

    static final String informationBar = "ul#formBreadcrumb li:nth-of-type(1) li:nth-of-type(1)";  //Once we get an id, this css-selector should be replaced
    static final String medicationBar = "ul#formBreadcrumb li:nth-of-type(1) li:nth-of-type(2)";   //Once we get an id, this css-selector should be replaced

    static final String informationDatePicker = "//section[@id='information']//input";      //Once we get an id, this xpath should be replaced
    static final String prescriberDropDown = "select[name='prescriber']";                   //Once we get an id, this css-selector should be replaced

    static final String nameOfMedication = "";
    static final String frequencyCountTxtBox = "";
    static final String frequencyMeasureDropDown = "";
    static final String doseCountTxtBox = "";
    static final String doseMeasureDropDown = "";
    static final String durationTxtBox = "";
    static final String amountTxtBox = "";
    static final String amountMeasureDropDown = "";

    @Override
    public String expectedUrlPath() {
        return null;  // TODO
    }
}
