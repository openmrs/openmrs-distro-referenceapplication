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

import java.util.List;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class PatientVisitsDashboardPage extends Page {
	
    private static final By CAPTURE_VITALS = By.id("referenceapplication.realTime.vitals");
    private static final By VISIT_LIST = By.cssSelector("#visits-list li.menu-item.viewVisitDetails span.menu-date");
    private static final By END_VISIT = By.className("icon-off");
    private static final By END_VISIT_CONFIRM = By.cssSelector("#end-visit-dialog button[class='confirm right']");
    private static final By ADMIT_TO_INPATIENT = By.id("referenceapplication.realTime.simpleAdmission");
    private static final By TRANSFER_TO_WARD = By.id("referenceapplication.realTime.simpleTransfer");
    private static final By EXIT_FROM_INPATIENT = By.id("referenceapplication.realTime.simpleDischarge");
    private static final By ACTIONS_DROPDOWN = By.cssSelector("#content span.dropdown-name");
    private static final By MERGE_VISITS = By.cssSelector("#content div.actions.dropdown ul li:nth-child(2) > a");
    private static final By FAMILY_NAME = By.cssSelector(".patient-header .demographics .name .PersonName-familyName");
    private static final By VISIT_NOTE_ENCOUNTER = By.xpath("//div[@id='visit-details']/ul/li/ul/li/div/strong/span[text()='Visit Note']");
    private static final By VISIT_NOTE = By.id("referenceapplication.realTime.simpleVisitNote");
    private static final By RETURN_TO_DASHBOARD = By.xpath("//*[@id='breadcrumbs']/li[2]/a");
    private static final By EDIT_VITALS_ICON = By.cssSelector("#encountersList i.editEncounter.edit-action.icon-pencil");
    private static final By VIEW_VITALS_ICON  = By.cssSelector("#encountersList i.viewEncounter.view-action.icon-file-alt");
    private static final By DELETE_VITALS_ICON = By.cssSelector("#encountersList i.deleteEncounterId.delete-action.icon-remove");
    private static final By COMFIRM_DELETE_BUTTON  = By.cssSelector("#delete-encounter-dialog button.confirm.right");

    public PatientVisitsDashboardPage(Page parent) {
        super(parent);
    }

    public PatientVisitsDashboardPage(Page parent, WebElement waitForStaleness) {
        super(parent, waitForStaleness);
    }

    @Override
    public String getPageUrl() {
        return "coreapps/patientdashboard/patientDashboard.page";
    }
    
    public PatientCaptureVitalsPage goToPatientCaptureVitalsPage() {
        waiter.until(ExpectedConditions.presenceOfElementLocated(CAPTURE_VITALS));
        clickOn(CAPTURE_VITALS);
        return new PatientCaptureVitalsPage(this);
    }
    
    public EditVitalsPage goToEditVitalsPage() {
        waiter.until(ExpectedConditions.presenceOfElementLocated(EDIT_VITALS_ICON));
        clickOn(EDIT_VITALS_ICON);
        return new EditVitalsPage(this);
    }

    public void goToViewVitalsPage() {
        waiter.until(ExpectedConditions.presenceOfElementLocated(VIEW_VITALS_ICON));
        clickOn(VIEW_VITALS_ICON);
    }

    public void deleteVitals() {
        waiter.until(ExpectedConditions.presenceOfElementLocated(DELETE_VITALS_ICON));
        clickOn(DELETE_VITALS_ICON);
        waitForElement(COMFIRM_DELETE_BUTTON);
        clickOn(COMFIRM_DELETE_BUTTON);
    }
    
    public void goToVisitNote() {
        findElement(VISIT_NOTE).click();
    }

    public WebElement getActiveVisit() {
        for (WebElement webElement : findElements(VISIT_LIST)) {
            if (webElement.getText().contains("active")) {
                return webElement;
            }
        }
        return null;
    }

    public List<WebElement> getVisitList() {
        return findElements(VISIT_LIST);
    }

    public void endVisit() {
        clickOn(END_VISIT);
        waitForElement(END_VISIT_CONFIRM);
        clickOn(END_VISIT_CONFIRM);
    }

    public AdmitToInpatientPage goToAdmitToInpatient() {
        clickOn(ADMIT_TO_INPATIENT);
        return new AdmitToInpatientPage(this);
    }

    public ExitFromInpatientPage goToExitFromInpatient() {
        clickOn(EXIT_FROM_INPATIENT);
        return new ExitFromInpatientPage(this);
    }

    public TransferToWardServicePage goToTransferToWardServicePage() {
    	clickOn(TRANSFER_TO_WARD);
    	return new TransferToWardServicePage(this);
    }
    
    public void clickOnActions() {
        clickOn(ACTIONS_DROPDOWN);
    }

    public MergeVisitsPage clickOnMergeVisits() {
        clickOn(MERGE_VISITS);
        return new MergeVisitsPage(this);
    }

    public ClinicianFacingPatientDashboardPage goToPatientDashboard() {
        clickOn(RETURN_TO_DASHBOARD);
        return new ClinicianFacingPatientDashboardPage(this);
    }
    
    public void deleteVisitNote() {
        String visitNoteId = findElement(VISIT_NOTE_ENCOUNTER).getAttribute("data-encounter-id");
        clickOn(By.xpath("//div[@id='visit-details']/ul/li/span/i[@data-encounter-id='" + visitNoteId + "'][2]"));
    }

    public EditVisitNotePage goToEditVisitNote() throws InterruptedException {
        String visitNoteId = findElement(VISIT_NOTE_ENCOUNTER).getAttribute("data-encounter-id");
        clickOn(By.xpath("//*[@id=\"encountersList\"]/li/span/i[2]"));
        return new EditVisitNotePage(this);
    }

    /**
     * Before using it, note that it will take full timeout time if encountersList is empty
     */
    public int getEncountersCount() {
        try {
            return findElements(By.xpath("//*[@id='encountersList']/li")).size();
        } catch (TimeoutException e) {
            return 0;
        }
    }
    
    public String getPatientFamilyName() {
        String patientFamilyName = findElement(FAMILY_NAME).getText();
        return patientFamilyName;
    }
}
