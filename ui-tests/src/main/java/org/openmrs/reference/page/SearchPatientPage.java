package org.openmrs.reference.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SearchPatientPage extends AbstractBasePage {

    public SearchPatientPage(WebDriver driver) {
        super(driver);
    }

    private void enterPatientIDOrName(String nameOrID) {
        setTextToField(By.id("patient-search"),nameOrID);
    }

    private void submitSearch(){
        clickOn(By.cssSelector("form#patient-search-form input:contains('patient-search-form')"));
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
