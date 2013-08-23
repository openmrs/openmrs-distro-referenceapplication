package org.openmrs.reference.page;


import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.WebDriver;

public class ModulesPage extends AbstractBasePage {

    private static final String ADMIN_MODULE_LIST_PATH = "/admin/modules/module.list";

	public ModulesPage(WebDriver driver) {
        super(driver);
    }
	
	@Override
    public String expectedUrlPath() {
    	return URL_ROOT + ADMIN_MODULE_LIST_PATH;
    }
}

