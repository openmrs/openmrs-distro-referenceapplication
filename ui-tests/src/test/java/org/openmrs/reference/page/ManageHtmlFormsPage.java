package org.openmrs.reference.page;


import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

/**This is different from manageFormsPage , For ManageFormsPage we are inheriting from configure metadata however
 * we are first adding a form to be reflected from the manageForm from the configure metatada
*/
public class ManageHtmlFormsPage extends Page  {
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
