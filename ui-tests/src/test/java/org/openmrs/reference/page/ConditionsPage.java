package org.openmrs.reference.page;

import java.util.List;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ConditionsPage extends Page {
	
    public static final By ADD_NEW_CONDITION = By.id("conditionui-addNewCondition");
    public static final By SET_ACTIVE = By.xpath("//button[text()='Set Active']");
    public static final By SET_INACTIVE = By.xpath("//button[text()='Set Inactive']");
    private static final By RETURN = By.cssSelector(".actions .cancel");
    private static final By TAB_ACTIVE = By.cssSelector("a[href='#ACTIVE']");
    private static final By TAB_INACTIVE = By.cssSelector("a[href='#INACTIVE']");
    private static final By FIRST_CONDITION_NAME = By.xpath("//table/tbody[2]/tr[1]/td[1]");
    private static final By EDIT = By.cssSelector("i[title='Edit Condition: ']");
    private static final By DELETE = By.cssSelector("i[title='Delete']");
    private static final By CONDITIONS_LIST = By.cssSelector("tr.clickable-tr");
    private static final By CONFIRM_DELETE_CONDITION_BUTTON = By.cssSelector("#remove-condition-dialog > div.dialog-content > button.confirm.right");

    public ConditionsPage(ClinicianFacingPatientDashboardPage clinicianFacingPatientDashboardPage) {
        super(clinicianFacingPatientDashboardPage);
    }

    public ConditionsPage(ConditionPage conditionPage) {
        super(conditionPage);
    }

    @Override
    public String getPageUrl() {
        return "/coreapps/conditionlist/manageConditions.page";
    }

    public ClinicianFacingPatientDashboardPage clickReturn() {
        clickOn(RETURN);
        return new ClinicianFacingPatientDashboardPage(this);
    }

    public String getFirstConditionName() {
        try {
            waiter.until(ExpectedConditions.elementToBeClickable(FIRST_CONDITION_NAME));
            return driver.findElement(FIRST_CONDITION_NAME).getAttribute(
                    "innerText");
        } catch (Exception e) {
            return null;
        }
    }

    public void clickActiveTab() {
        clickOn(TAB_ACTIVE);
    }

    public void clickInActiveTab() {
    	waiter.until(ExpectedConditions.elementToBeClickable(TAB_INACTIVE));
        clickOn(TAB_INACTIVE);
    }

    public void setFirstActive() {
        clickOn(SET_ACTIVE);
    }

    public void setFirstInActive() {
        clickOn(SET_INACTIVE);
    }

    public ConditionPage editFirstActive() {
        clickOn(EDIT);
        return new ConditionPage(this);
    }

    public ConditionPage editFirstInActive() {
        clickOn(EDIT);
        return new ConditionPage(this);
    }

    public void deleteFirstActive() {
        clickOn(DELETE);
    }

    public void deleteFirstInActive() {
        clickInActiveTab();
        clickOn(DELETE);
    }

    public ConditionPage clickOnAddNewCondition() {
        clickOn(ADD_NEW_CONDITION);
        return new ConditionPage(this);
    }
    
    public List<WebElement> getConditionsList() {
        return findElements(CONDITIONS_LIST);
    }

    public void confirmDeleteCondition(){
        clickOn(CONFIRM_DELETE_CONDITION_BUTTON);
    }
}
