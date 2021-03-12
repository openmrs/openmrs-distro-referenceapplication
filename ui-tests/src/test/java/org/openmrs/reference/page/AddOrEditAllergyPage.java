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

public class AddOrEditAllergyPage extends Page {

    private static final By SAVE_ALLERGY = By.id("addAllergyBtn");
    private static By DRUG;
    private static String DRUG_XPATH = "//div[@id='allergens']/ul/li/label[text()=\"";
    private static String DRUG_REST_XPATH = "\"]";
    private static By REACTION;
    private static By DRUG_ID;
    private static String REACTION_XPATH = "//div[@id='reactions']/ul/li/label[text()=\"";
    private static By REACTION_ID;

    public AddOrEditAllergyPage(Page page) {
        super(page);
    }

    public AllergyPage clickOnSaveAllergy() {
        clickOn(SAVE_ALLERGY);
        return new AllergyPage(this);
    }

    public void enterDrug(String drug) {
        DRUG = By.xpath(DRUG_XPATH + drug + DRUG_REST_XPATH);
        findElement(DRUG);
    }

    public void enterReaction(String reaction) {
        REACTION = By.xpath(REACTION_XPATH + reaction + DRUG_REST_XPATH);
        findElement(REACTION);
    }

    public String getValue(By field) {
        return findElement(field).getAttribute("id");
    }

    public String getFor(By field) {
        return findElement(field).getAttribute("for");
    }

    public String getDrugValue() {
        return getValue(DRUG);
    }

    public String getReactionId() {
        return getFor(REACTION);
    }

    public void drugId() {
        DRUG_ID = By.id(getDrugValue());
        clickOn(DRUG_ID);
    }

    public void reactionId() {
        REACTION_ID = By.id(getReactionId());
        clickOn(REACTION_ID);
    }

    @Override
    public String getPageUrl() {
        return "/allergyui/allergy.page";
    }
}