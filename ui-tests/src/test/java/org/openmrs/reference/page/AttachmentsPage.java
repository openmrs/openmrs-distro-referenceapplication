package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

public class AttachmentsPage extends Page {
	
	private static final By ATTACH_FILE = By.cssSelector("#visit-documents-dropzone");
	private static final By ADD_CAPTION = By.cssSelector("textarea.ng-pristine");
	private static final By UPLOAD_FILE = By.cssSelector("button[ng-click='uploadFile()']");
	private static final By CLEAR_FORMS = By.cssSelector("button[ng-click='clearForms()']");
	public static final By CAMERA_ICON = By.className("icon-camera");
	private static final By CAMERA_BUTTON = By.cssSelector("button[ng-click='snap()']");
	public static final By SAVE_IMAGE = By.cssSelector("button[ng-click='finalise()']");
	
	public AttachmentsPage(ClinicianFacingPatientDashboardPage clinicianFacingPatientDashboardPage) {
		super(clinicianFacingPatientDashboardPage);
	}

	public void clickOrDragAndDropAFile() {
		driver.findElement(ATTACH_FILE).click();
	}

	public String addUserCaption() {
		try {
			clickOn(ADD_CAPTION);
			return driver.findElement(ADD_CAPTION).getText();
		}
		catch (Exception ex) {
			return null;
		}
	}

	public void clickOnUploadFile() {
		clickOn(UPLOAD_FILE);
	}

	public void clickClearForms() {
		driver.findElement(CLEAR_FORMS).click();
	}

	public void clickOnCameraIcon() {
		driver.findElement(CAMERA_ICON).click();
	}

	public void clickOnCameraButton() {
		driver.findElement(CAMERA_BUTTON).click();
	}
	
	public void clickOnSaveButton() {
		driver.findElement(SAVE_IMAGE).click();
	}
	
	@Override
	public String getPageUrl() {
		return "/attachments/attachments.page";
	}
}