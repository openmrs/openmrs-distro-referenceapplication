package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

public class ManageHtmlFormsPage extends Page{
	    private ManageHtmlFormsPage manageHtmlFormsPage;
	    private static String name = "newFormTest";
	    private static String description = "description of new form";
	    private static  String version = "1.2";
	    private static final By NEW_HTML_FORM = By.cssSelector("a[href='htmlForm.form']");
	    

	    public ManageHtmlFormsPage(Page page) {
	        super(page);
	    }

	    public HtmlFormsPage clickOnNewHtmlForm(){
	       findElement(NEW_HTML_FORM).click();
	       return new HtmlFormsPage(this);
	    }
	    
	    public boolean overrideAddButtonElement() {
	        try {
	        	manageHtmlFormsPage.getElementsIfExisting(By.xpath("//*[contains(text(), '"+ name + "')]")).isEmpty();
	        } catch (Exception ex) {
	            return false;
	        }
	        return false;
	    }
	    @Override
	    public String getPageUrl() {
	        return "/module/htmlformentry/htmlForms.list";
	    }
}
