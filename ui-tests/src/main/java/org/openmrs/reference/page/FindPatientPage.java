/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class FindPatientPage extends Page {

    private static final By PATIENT_NAME_SEARCH_RESULT = By.cssSelector("#patient-search-results-table tr:first-child td:nth-child(2)");
    private static final By PATIENT_ID_SEARCH_RESULT = By.cssSelector("#patient-search-results-table tr:first-child td:first-child");
    private static final By PATIENT_SEARCH = By.id("patient-search");

    public FindPatientPage(Page page) {
        super(page);
    }


    public void enterPatient(String patient) {
        setTextToFieldNoEnter(PATIENT_SEARCH, patient);
    }

    public void clickOnFirstPatient(){ clickOn(PATIENT_NAME_SEARCH_RESULT);}

    /**
     * Finds first record from the result table
     * @return patient id
     */
    public String getFirstPatientIdentifier() {
        //let's wait for the name to appear as the identifier selector is ambiguous and may select the loading image
        getFirstPatientName();
        return findElement(PATIENT_ID_SEARCH_RESULT).getText();
    }
    
    public String getFirstPatientName() {
        return findElement(PATIENT_NAME_SEARCH_RESULT).getText();
    }


    @Override
    public String getPageUrl() {
        return "/findpatient/findPatient.page";
    }
}