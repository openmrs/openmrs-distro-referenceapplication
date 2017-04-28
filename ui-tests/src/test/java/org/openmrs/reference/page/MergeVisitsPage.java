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
import org.openqa.selenium.WebElement;

import java.util.List;

public class MergeVisitsPage extends Page{

    private static final By RETURN = By.cssSelector("#content form div input.cancel");
    private static final By MERGE_SELECTED_VISITS = By.id("mergeVisitsBtn");
    private static final By FIRST_CHECKBOX = By.cssSelector("#active-visits tbody tr:nth-child(1) td:nth-child(1) input");
    private static final By SECOND_CHECKBOX = By.cssSelector("#active-visits tbody tr:nth-child(2) td:nth-child(1) input");
    private static final By VISITS = By.cssSelector("#active-visits tbody tr");

    public MergeVisitsPage(Page parent) {
        super(parent);
    }

    public MergeVisitsPage(Page parent, WebElement waitForStaleness) {
        super(parent, waitForStaleness);
    }

    public void checkFirstVisit(){
        clickOn(FIRST_CHECKBOX);
    }

    public void checkSecondVisit(){
        clickOn(SECOND_CHECKBOX);
    }

    public MergeVisitsPage clickOnMergeSelecetdVisits(){
        WebElement mergeBtn = findElement(MERGE_SELECTED_VISITS);
        clickOn(MERGE_SELECTED_VISITS);
        return new MergeVisitsPage(this, mergeBtn);
    }

    public List<WebElement> getAllVisit(){
        return findElements(VISITS);
    }

    @Override
    public String getPageUrl() {
        return "/coreapps/mergeVisits.page";
    }
}
