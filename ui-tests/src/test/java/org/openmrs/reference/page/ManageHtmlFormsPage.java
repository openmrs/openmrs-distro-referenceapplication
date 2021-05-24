package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

public class ManageHtmlFormsPage extends Page {		
   private static final By NEW_HTML_FORM = By.cssSelector("a[href='htmlForm.form']");				
   public ManageHtmlFormsPage(Page page) {
	super(page);
   }			    
   public HtmlFormsPage clickOnNewHtmlForm(){
	 findElement(NEW_HTML_FORM).click();
	 return new HtmlFormsPage(this);
   }					   
   @Override
   public String getPageUrl() {
    return "/module/htmlformentry/htmlForms.list";
   }
}
