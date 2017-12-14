package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.LoginPage;
import org.openqa.selenium.WebDriver;

public class ReferenceApplicationLoginPage extends LoginPage {

    public ReferenceApplicationLoginPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void go() {
        goToPage("/appui/header/logout.action?successUrl=openmrs");
    }
}
