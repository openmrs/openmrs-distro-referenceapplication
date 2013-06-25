package org.openmrs.reference.page;


import org.openqa.selenium.WebDriver;

public class ModulesPage extends AbstractBasePage {

    public ModulesPage(WebDriver driver) {
        super(driver);
    }

	@Override
    public String expectedUrlPath() {
    	return "/openmrs/admin/modules/module.list";
    }
}

