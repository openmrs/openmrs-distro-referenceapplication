package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

public class SystemAdministrationPage extends Page {

    static final By ADVANCED_ADMINISTRATION = By.id("referenceapplication-legacyAdmin-app");

    public SystemAdministrationPage(Page parent) {
        super(parent);
    }

    @Override
    public String getPageUrl() {
        return "coreapps/systemadministration/systemAdministration.page";
    }

    public AdministrationPage goToAdvancedAdministration(){
        clickOn(ADVANCED_ADMINISTRATION);
        return new AdministrationPage(driver);
    }
}
