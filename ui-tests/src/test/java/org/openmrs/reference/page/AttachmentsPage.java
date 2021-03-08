package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

public class AttachmentsPage extends Page {

	private static final By ATTACH_FILE = By.className("dz-default dz-message ng-binding");
	private static final By ADD_CAPTION = By.className("ng-pristine ng-valid ng-empty ng-touched");
	private static final By UPLOAD_FILE = By.className("confirm ng-binding");
	private static final By CLEAR_FORMS = By.className("ng-binding");
	public static final By ICON_CAMERA = By.className("icon-camera");
	public static final By SAVE_BUTTON = By.className("icon-ok");

	public AttachmentsPage(Page page) {
		super(page);
	}

	public void clickOrDragAndDropAFile() {
		driver.findElement(ATTACH_FILE).click();
	}

	public String addCaption() {
		clickOn(ADD_CAPTION);
		return driver.findElement(ADD_CAPTION).getText();
	}
	
	public void clickOnUploadFile() {
		driver.findElement(UPLOAD_FILE).click();
	}

	public void clickClearForms() {
		driver.findElement(CLEAR_FORMS).click();
	}

	public void clickOnCameraIcon() {
		driver.findElement(ICON_CAMERA).click();
	}

	public void clickOnSaveButton() {
		driver.findElement(SAVE_BUTTON).click();
	}
	
	@Override
	public String getPageUrl() {
		return "/attachments/attachments.page";
	}
}