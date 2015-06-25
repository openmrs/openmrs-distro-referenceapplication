package org.openmrs.reference.page;


import org.apache.commons.lang3.StringUtils;
import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;



public class ManageFormsPage extends AbstractBasePage{

    private static final By ADD = By.linkText("Add");
    private static final By SAVE = By.cssSelector("input[type=\"submit\"]");
    private static final By EXTENSION_FORM_LABEL = By.id("extensionForm.label");
    private static final By EXTENSION_FORM_ICON = By.id("extensionForm.icon");
    private static final By DELETE = By.cssSelector("i.icon-remove.delete-action");


    public ManageFormsPage(WebDriver driver) {
        super(driver);
    }

    public void add (){
        clickOn(ADD);
    }

    public void addLabel(String lab){
        driver.findElement(EXTENSION_FORM_LABEL).clear();
        driver.findElement(EXTENSION_FORM_LABEL).sendKeys(lab);
    }

    public void save(){
        clickOn(SAVE);
    }

    public void addIcon(String icon){
        driver.findElement(EXTENSION_FORM_ICON).clear();
        driver.findElement(EXTENSION_FORM_ICON).sendKeys(icon);
    }

    public boolean addPresent(){
        try {
            return driver.findElement(ADD) != null;
        }
        catch (Exception ex) {
            return false;
        }
    }

    public void delete(){
        clickOn(DELETE);
    }

    public String formIdFromUrl() {
        String url = driver.getCurrentUrl();
        return StringUtils.substringAfter(url, "formId=");
    }


    @Override
    public String expectedUrlPath() {
        return URL_ROOT + "/formentryapp/forms/extension.page";
    }


}
