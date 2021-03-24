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

public class AllergyPage extends Page {

    private static final By ADD_NEW_ALLERGY = By.id("allergyui-addNewAllergy");
    private static final By EDIT_ALLERGY = By.cssSelector("i.icon-pencil.edit-action");
    private static final By DELETE_ALLERGY = By.cssSelector("i.icon-remove.delete-action");
    private static final By CONFIRM_DELETE_ALLERGY = By.cssSelector("#allergyui-remove-allergy-dialog .confirm");
    private static final By REACTION = By.cssSelector(".reaction");
    private static final By ALLERGEN = By.cssSelector(".allergen");
    private static final By ALLERGY_STATUS = By.cssSelector(".allergyStatus");
    private static final By ADD_NO_KNOWN = By
            .xpath("//*[contains(text(),'No Known Allergy')]");
    private static final By DELETE_NO_KNOWN = By.className("delete-action");
    public static final By CHOOSEDRUG = By.id("allergen-162299");
    public static final By CHOOSEREACTION = By.id("reaction-121629");
    public static final By SELECTSEVERITY = By.id("severity-1498");
    public static final By COMMENT = By.id("allergy-comment");
    public static final By SAVE = By.id("addAllergyBtn");
    public static final By CANCEL = By.className("cancel");
    public static final By ALLERGYTYPE = By.id("types");
    public static final By AllERGEN = By.id("allergens");
    public static final By SEVERTIES = By.name("severity");

    public AllergyPage(Page page) {
        super(page);
    }

    public AddOrEditAllergyPage clickOnAddNewAllergy() {
        clickOn(ADD_NEW_ALLERGY);
        return new AddOrEditAllergyPage(this);
    }

    public AddOrEditAllergyPage clickOnEditAllergy() {
        clickOn(EDIT_ALLERGY);
        return new AddOrEditAllergyPage(this);
    }

    public void clickOnDeleteAllergy() {
        clickOn(DELETE_ALLERGY);
    }

    public void clickOnConfirmDeleteAllergy() {
        clickOn(CONFIRM_DELETE_ALLERGY);
    }

    public String getAllergen() {
        return findElement(ALLERGEN).getText();
    }

    public String getReaction() {
        return findElement(REACTION).getText();
    }

    public String getAllergyStatus() {
        return findElement(ALLERGY_STATUS).getText();
    }

    public void addNoKnownAllergy() {
        driver.findElement(ADD_NO_KNOWN).click();
    }

    public void removeNoKnownAllergy() {
        driver.findElement(DELETE_NO_KNOWN).click();
    }

    public  void clickOnAddNewAllergy(){
        driver.findElement(ADD_NEW_ALLERGY).click();
    }

    public void chooseDrug(){
        driver.findElement(CHOOSEDRUG).click();
    }

    public void chooseReaction(){
        driver.findElement(CHOOSEREACTION).click();
    }

    public void selectSeverity(){
        driver.findElement(SELECTSEVERITY).click();

    public void allergyComment(){
        setText(COMMENT,"I have malaria");
    }

    public void clickOnSaveButon(){
        driver.findElement(SAVE).click();
    }

    public void clickOnCancelButon(){
        driver.findElement(CANCEL).click();
    }


    public void clickOnEdditAllergy(){
        driver.findElement(EDIT_ALLERGY).click();
    }

    public void selectAllergyType(int allergytype){
        driver.findElement(ALLERGYTYPE).getTagName("label")[allergytype].click();
    }

    public String getSeclectedAllergernclass(int allergytype){
        WebElement l = driver.findElement(AllERGEN).getTagName("ul")[allergytype];
        return l.getAttribute('class');
    }

    public void selectalleregn(int allergytype){
        WebElement l = driver.findElement(AllERGEN).getTagName("ul")[allergytype];
        int allergen = (int)(Math.random()*(3) + 0);
        l.getTagName('li')[allergen];
        l.isSelected();
    }

    public void selectReaction(){
        WebElement l = driver.findElement(REACTION).getTagName("li")[5];
        l.isSelected();
    }

    public void Selectseverity(int severity){
        WebElement l = driver.findElement(SEVERTIES)[severity];
        l.isSelected();
    }
    public void addComment(String comment){
        driver.findElement(COMMENT).sendKeys(comment);
    }


    @Override
    public String getPageUrl() {
        return "/allergyui/allergies.page";
    }
}