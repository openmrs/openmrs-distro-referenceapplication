package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

public class EditReportPage extends Page {

   private static final By PREVIEW_BUTTON = By.cssSelector("#previewButton");
   private static final By START_DATE_FIELD = By.cssSelector("#startDate");
   private static final By END_DATE_FIELD = By.cssSelector("#endDate");
   private static final By RUN_BUTTON = By.cssSelector("#preview-parameterizable-button");
   private static final By CLOSE_TAB = By.xpath("//span[contains(text(),'close')]");
   private static final String START_DATE = "05/07/2008";
   private static final String END_DATE = "20/06/2020";

   public EditReportPage(Page parent) {
      super(parent);
   }

   public void clickOnPreviewButton() {
      clickOn(PREVIEW_BUTTON);
   }

   public void enterStartDate(String START_DATE) {
      setTextToFieldNoEnter(START_DATE_FIELD, START_DATE);
   }
 
   public void enterEndDate(String END_DATE) {
       setTextToFieldNoEnter(END_DATE_FIELD, END_DATE);
   }
 
   public void clickOnRun() {
      clickOn(RUN_BUTTON);
   }
 
   public void clickOnCloseTab() {
      clickOn(CLOSE_TAB);
   }

   @Override
   public String getPageUrl() {
	  return "/reporting/reports/reportEditor.form?uuid=b39c4c4c-4881-11e7-a919-92ebcb67fe33";
   }
}
