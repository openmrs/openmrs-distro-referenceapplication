package org.openmrs.reference.page;


import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

/**
 *
 */
public class VisitTypePage extends Page {

    private static final By SAVE_VISIT_TYPE = By.cssSelector("#content input[name=\"save\"]");
    private static final By DELETE_VISIT_TYPE = By.cssSelector("#purge input[name=\"purge\"]");
    private static final By RETIRE_VISIT_TYPE = By.cssSelector("#content input[name=\"retire\"]");
    private static final By NAME_TEXT_FIELD = By.cssSelector("#content input[name='name']");
    private static final By DESCRIPTION_TEXT_FIELD = By.cssSelector("#content textarea[name='description']");
    private static final By RETIRE_REASON_TEXT_FIELD = By.cssSelector("#content input[name=\"retireReason\"]");

    public VisitTypePage(Page parent) {
        super(parent);
    }

    public VisitTypeListPage save(){
        findElement(SAVE_VISIT_TYPE).click();
        return new VisitTypeListPage(this);
    }

    public void setName(String name){
        findElement(NAME_TEXT_FIELD).clear();
        findElement(NAME_TEXT_FIELD).sendKeys(name);
    }

    public void setDescription(String description){
        findElement(DESCRIPTION_TEXT_FIELD).clear();
        findElement(DESCRIPTION_TEXT_FIELD).sendKeys(description);
    }

    public VisitTypeListPage delete() {
        findElement(DELETE_VISIT_TYPE).click();
        acceptAlert();
        return new VisitTypeListPage(this);
    }



    public VisitTypeListPage retire() {
        findElement(RETIRE_VISIT_TYPE).click();
        return new VisitTypeListPage(this);
    }

    @Override
    public String getPageUrl() {
        return "/admin/visits/visitType.form";
    }

    public void setRetireReason(String retireReason) {
        findElement(RETIRE_REASON_TEXT_FIELD).clear();
        findElement(RETIRE_REASON_TEXT_FIELD).sendKeys(retireReason);
    }
}
