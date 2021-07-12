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
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ActiveVisitsPage extends Page {

    private static final By ACTIVE_PATIENT = By.xpath("//td[2]/a");
    private By SEARCH_INPUT = By.tagName("input");
    private By FIRST_ACTIVE_VISIT = By.className("odd");

    public ActiveVisitsPage(HomePage homePage) {
        super(homePage);
    }

    @Override
    public String getPageUrl() {
        return "/coreapps/activeVisits.page";
    }

    public String getPatientNameOfLastActiveVisit() {
        return findElement(FIRST_ACTIVE_VISIT).findElements(By.xpath("//td/a")).get(0).getText().trim();
    }

    public String getPatientIdOfLastActiveVisit() {
        return findElement(FIRST_ACTIVE_VISIT).findElements(By.xpath("//td")).get(0).getText().trim();
    }

    public String getPatientLastSeenValueOfLastActiveVisit() {
        return findElement(FIRST_ACTIVE_VISIT).findElements(By.xpath("//td[3]")).get(0).getText().trim();
    }

    public void search(String text) {
        setText(SEARCH_INPUT, text);
    }

    public ClinicianFacingPatientDashboardPage goToPatientDashboardOfLastActiveVisit() {
        waiter.until(ExpectedConditions.visibilityOfElementLocated(ACTIVE_PATIENT));
        clickOn(ACTIVE_PATIENT); 
        return new ClinicianFacingPatientDashboardPage(this);
    }
}
