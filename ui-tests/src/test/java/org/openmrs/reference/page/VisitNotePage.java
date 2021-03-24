/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class VisitNotePage extends Page {

    private static final By DIAGNOSIS_SEARCH = By.id("diagnosis-search");
    private static final By CODE = By.className("code");
    private static final By UI_ID_1 = By.id("ui-id-1");
    private static final By UI_MENU_ITEM = By.className("ui-menu-item");
    private static final By PRIMARY_DIAGNOSIS_ELEMENT = By.cssSelector("#display-encounter-diagnoses-container > ul:nth-child(3) > li > span > div > strong");
    private static final By SECONDARY_DIAGNOSIS_ELEMENT = By.cssSelector("#display-encounter-diagnoses-container > ul:nth-child(7) > li > span > div > strong");
    private static final By SAVE_VISIT_NOTE = By.cssSelector(".submitButton.confirm");
    private static final By NOTE = By.id("w10");
    private static final By PROVIDER = By.id("w1");
    private static final By LOCATION = By.id("w3");
    private static final By DEL_DIAGNOSIS = By.cssSelector("#delete-button");
    private static final By EDIT_DIAGNOSIS = By.cssSelector("#edit-button");
    private static final By DELETE_SECONDARY_DIAGNOSIS = By.cssSelector("#display-encounter-diagnoses-container > ul:nth-child(7) > li > span > i");

    public VisitNotePage(Page page) {
        super(page);
    }

    @Override
    public String getPageUrl() {
        return "htmlformentryui/htmlform/enterHtmlFormWithStandardUi.page";
    }

    public void enterDiagnosis(String diag) {
        setTextToFieldNoEnter(DIAGNOSIS_SEARCH, diag);
        clickOn(CODE);
    }

    public void addDiagnosis(String diag) {
        WebElement diagnosisElement = findElement(DIAGNOSIS_SEARCH);
        diagnosisElement.click();
        enterDiagnosis(diag);
        diagnosisElement.clear();
        diagnosisElement.click();

    }

    public void addNote(String note) {
        findElement(NOTE).clear();
        setText(NOTE, note);
    }

    public void selectProviderAndLocation() {
        new Select(findElement(PROVIDER)).selectByVisibleText("Super User");
        new Select(findElement(LOCATION)).selectByVisibleText("Isolation Ward");
    }

    public void addSecondaryDiagnosis(String diag) {
        WebElement diagnosisElement = findElement(DIAGNOSIS_SEARCH);
        diagnosisElement.click();
        enterSecondaryDiagnosis(diag);
        diagnosisElement.clear();
        diagnosisElement.click();

    }

    public void enterSecondaryDiagnosis(String diag) {
        setTextToFieldNoEnter(DIAGNOSIS_SEARCH, diag);
        waitForElement(UI_ID_1);
        clickOn(UI_MENU_ITEM);
    }

    public void deleteDiagnosis() {
        clickOn(DEL_DIAGNOSIS);
    }
    public void editDiagnosis() {
        clickOn(EDIT_DIAGNOSIS);
    }
    public void deleteSecondaryDiagnosis() {
        clickOn(DELETE_SECONDARY_DIAGNOSIS);
    }
    public String primaryDiagnosis() {
        return findElement(PRIMARY_DIAGNOSIS_ELEMENT).getText().trim();
    }

    public String secondaryDiagnosis() {
        return findElement(SECONDARY_DIAGNOSIS_ELEMENT).getText();
    }

    public ClinicianFacingPatientDashboardPage save() {
        clickOn(SAVE_VISIT_NOTE);
        return new ClinicianFacingPatientDashboardPage(this);
    }

}
