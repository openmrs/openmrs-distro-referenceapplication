package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

public class StylesGuidePage extends Page {

    public static By STYLES_GUIDE_HEADER = By.id("style-guide-header");

    public StylesGuidePage(SystemAdministrationPage systemAdministrationPage) {
        super(systemAdministrationPage);
    }

    public void pressBack() {
        driver.navigate().back();
    }

    @Override
    public String getPageUrl() {
        return "/uicommons/styleGuide.page";
    }
}
