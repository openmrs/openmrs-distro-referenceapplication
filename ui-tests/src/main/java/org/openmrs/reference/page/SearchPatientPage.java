package org.openmrs.reference.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SearchPatientPage extends AbstractBasePage {

    public SearchPatientPage(WebDriver driver) {
        super(driver);
    }

    private String searchTxtBox = "patient-search";
    private String searchBtn = "form#patient-search-form input:contains('patient-search-form')";      //css-selector will be replaced with id once it is implemented in the app


    private void enterPatientIDOrName(String nameOrID) {
        setTextToField(By.id(searchTxtBox),nameOrID);
    }

    private void submitSearch(){
        clickOn(By.cssSelector(searchBtn));
    }

    public void searchPatient(String nameOrID){
        enterPatientIDOrName(nameOrID);
        submitSearch();
    }

	@Override
    public String expectedUrlPath() {
	    return "openmrs/coreapps/findpatient/findPatient.page?app=coreapps.findPatient";
    }
}
