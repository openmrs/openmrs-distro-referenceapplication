package org.openmrs.reference.page;

import java.util.List;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class AttachmentsPage extends Page {

    private static final String URL = "/attachments/attachments.page";
    private static final By ATTACHMENTS_LIST = By.cssSelector("span.att_thumbnail-extension");
    private static final By ATTACHMENT_NOTE = By.cssSelector("textarea.ng-pristine");
    private static final By UPLOAD_FILE_BUTTON = By.cssSelector("button[ng-click='uploadFile()']");
    private static final By CLEAR_FORMS = By.cssSelector("button[ng-click='clearForms()']");
    private static final By CAMERA_ICON = By.cssSelector("button[ng-click='snap()']");
    private static final By SAVE_IMAGE = By.cssSelector("button[ng-click='finalise()']");
    private static final By RETURN_TO_DASHBOARD = By.xpath("//*[@id='breadcrumbs']/li[2]/a");

    public AttachmentsPage(Page page) {
        super(page);
    }

    public void setFileUrl(String fileUrl) {
        WebElement element = driver.findElement(By.xpath("//input[@type='file']"));
        element.sendKeys(fileUrl);
    }
    
    public void addAttachmentNote(String note) {
        findElement(ATTACHMENT_NOTE).clear();
        setText(ATTACHMENT_NOTE, note); 
    }
    
    public void clickOnUploadFile() { 
        clickOn(UPLOAD_FILE_BUTTON); 
    }
    
    public void clickOnClearForms() { 
        clickOn(CLEAR_FORMS); 
    }
    
    public void clickOnCameraIcon() { 
        clickOn(CAMERA_ICON); 
    }
    
    public void clickOnSaveButton() { 
        clickOn(SAVE_IMAGE); 
    }
    
    public List<WebElement> getAttachmentsList() { 
        return findElements(ATTACHMENTS_LIST);
    }
    
    public ClinicianFacingPatientDashboardPage goToPatientDashboardPage() { 
        clickOn(RETURN_TO_DASHBOARD); 
        return new ClinicianFacingPatientDashboardPage(this);
    }
    
    @Override
    public String getPageUrl() {
        return URL;
    }   
}
