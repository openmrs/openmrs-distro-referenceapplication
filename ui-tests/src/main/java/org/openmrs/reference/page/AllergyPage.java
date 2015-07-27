package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class AllergyPage extends AbstractBasePage{

    private static final By ADD_NEW_ALLERGY = By.cssSelector("#content > button.confirm");
    private static By DRUG;
    private static String DRUG_XPATH = "//div[@id='allergens']/ul/li/label[text()=\"";
    private static String DRUG_REST_XPATH = "\"]";
    private static final By SAVE_ALLERGY = By.id("addAllergyBtn");
    private static final By EDIT_ALLERGY = By.cssSelector("i.icon-pencil.edit-action");
    private static By REACTION;
    private static final By DELETE_ALLERGY = By.cssSelector("i.icon-remove.delete-action");
    public static final By REMOVE_ALLERGY_MESSAGE = By.id("removeAllergyMessage");
    private static final By CONFIRM_DELETE_ALLERGY = By.cssSelector("#allergyui-remove-allergy-dialog .confirm");
    private static By DRUG_ID;
    private static String REACTION_XPATH = "//div[@id='reactions']/ul/li/label[text()=\"";
    private static By REACTION_ID;



    public AllergyPage(WebDriver driver) {
        super(driver);
    }

    public void clickOnAddNewAllergy(){clickOn(ADD_NEW_ALLERGY);}

    public void clickOnSaveAllergy(){clickOn(SAVE_ALLERGY);}

    public void clickOnEditAllergy(){clickOn(EDIT_ALLERGY);}

    public void clickOnDeleteAllergy(){clickOn(DELETE_ALLERGY);}

    public void clickOnConfirmDeleteAllergy(){clickOn(CONFIRM_DELETE_ALLERGY);}

    public WebElement removeAllergyMessage() {return findElement(REMOVE_ALLERGY_MESSAGE);}


    public void enterDrug(String drug) {
        DRUG = By.xpath(DRUG_XPATH + drug + DRUG_REST_XPATH);
        findElement(DRUG);
    }
    public void enterReaction(String reaction){
        REACTION = By.xpath(REACTION_XPATH + reaction + DRUG_REST_XPATH);
        findElement(REACTION);
    }

    public boolean editPresent(){
        try {
            return findElement(EDIT_ALLERGY) != null;
        }
        catch (Exception ex) {
            return false;
        }
    }




    public boolean deletePresent(){
        try {
            return findElement(DELETE_ALLERGY) != null;
        }
        catch (Exception ex) {
            return false;
        }
    }

    public String getValue(By field) {
        return findElement(field).getAttribute("id");
    }

    public String getFor(By field){ return findElement(field).getAttribute("for");}

    public String getDrugValue() {
       return getValue(DRUG);
    }

    public String getReactionId(){ return getFor(REACTION);}

    public void drugId(){
        DRUG_ID = By.id(getDrugValue());
        clickOn(DRUG_ID);
    }

    public void reactionId(){
        REACTION_ID = By.id(getReactionId());
        clickOn(REACTION_ID);
    }

    @Override
    public String expectedUrlPath() {
        return URL_ROOT + "/allergyui/allergy.page";
    }
}