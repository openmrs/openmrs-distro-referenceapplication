package org.openmrs.reference.page;


import org.openqa.selenium.WebDriver;

public class DispensingMedicationPage extends AbstractBasePage{

    public DispensingMedicationPage(WebDriver driver) {
        super(driver);
    }

    private String dispensingBar = "//ul[@id='formBreadcrumb']/li[1]/span";   //Once we get an id, this xpath should be replaced
    private String confirmBar = "//ul[@id='formBreadcrumb']/li[2]/span";      //Once we get an id, this xpath should be replaced

    private String informationBar = "ul#formBreadcrumb li:nth-of-type(1) li:nth-of-type(1)";  //Once we get an id, this css-selector should be replaced
    private String medicationBar = "ul#formBreadcrumb li:nth-of-type(1) li:nth-of-type(2)";   //Once we get an id, this css-selector should be replaced

    private String informationDatePicker = "//section[@id='information']//input";      //Once we get an id, this xpath should be replaced
    private String prescriberDropDown = "select[name='prescriber']";                   //Once we get an id, this css-selector should be replaced

    private String nameOfMedication = "";
    private String frequencyCountTxtBox = "";
    private String frequencyMeasureDropDown = "";
    private String doseCountTxtBox = "";
    private String doseMeasureDropDown = "";
    private String durationTxtBox = "";
    private String amountTxtBox = "";
    private String amountMeasureDropDown = "";



    @Override
    public String expectedUrlPath() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
