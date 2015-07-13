package org.openmrs.reference.page;


import org.apache.commons.lang3.StringUtils;
import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;



public class ManageFormsPage extends AbstractBasePage{

    public static final By ADD = By.linkText("Add");
    private static final By SAVE = By.cssSelector("input[type=\"submit\"]");
    private static final By EXTENSION_FORM_LABEL = By.id("extensionForm.label");
    private static final By EXTENSION_FORM_ICON = By.id("extensionForm.icon");
    private static final By DELETE = By.cssSelector("i.icon-remove.delete-action");
    public String id;
    private static String EDIT_FORM = "//i[@onclick=\"location.href='forms/extension.page?formId=";
    private static String REST_PATH = "&extensionId=patientDashboard.overallActions.form.";
    private static String DELETE_FORM = "//i[@onclick=\"location.href='forms/deleteExtension.page?formId=";
    private static By EDIT_FORM_PATH;
    private static By DELETE_FORM_PATH;
    private static String REST_PATH_2 = "'\"]";

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

    public void save() throws InterruptedException {
        clickWhenVisible(SAVE);
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

    public void formIdFromUrl() {
        String url = driver.getCurrentUrl();
        id = StringUtils.substringAfter(url, "formId=");
    }


    public void editPath(){
        EDIT_FORM_PATH = By.xpath(EDIT_FORM + id + REST_PATH + id + REST_PATH_2);
        clickOn(EDIT_FORM_PATH);
    }

    public void deletePath(){
        DELETE_FORM_PATH = By.xpath(DELETE_FORM + id + REST_PATH + id + REST_PATH_2);
        clickOn(DELETE_FORM_PATH);
    }



    @Override
    public String expectedUrlPath() {
        return URL_ROOT + "/formentryapp/forms/extension.page";
    }


}
