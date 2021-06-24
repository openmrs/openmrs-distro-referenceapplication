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

public class EditVisitNotePage extends VisitNotePage {
	
    private static final By NOTE = By.id("w10");
    private static final String NOTES = "This is edited note";
    private static final By REMOVE_DIAGNOSIS = By.cssSelector(".icon-remove.delete-item");
    
    public EditVisitNotePage(Page page) {
        super(page);
    }
    
    public void clearNote() {
        findElement(NOTE).clear();
        setTextToFieldNoEnter(NOTE, NOTES);
    }
    
    public void removeDiagnosis() {
        clickOn(REMOVE_DIAGNOSIS);
    }

    @Override
    public String getPageUrl() {
        return "htmlformentryui/htmlform/editHtmlFormWithStandardUi.page";
    }
}
