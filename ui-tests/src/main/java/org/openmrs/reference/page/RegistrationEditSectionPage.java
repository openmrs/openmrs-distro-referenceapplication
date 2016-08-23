package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

/**
 *
 */
public class RegistrationEditSectionPage extends Page {

    static final By PHONE_NUMBER = By.name("phoneNumber");
    static final By CONFIRM_EDIT = By.xpath("//ul[@id='formBreadcrumb']/li[2]/span");
    static final By CONFIRM = By.cssSelector("input[value='Confirm']");
    private static final By PHONE_NUMBER_EDIT = By.xpath("//ul[@id='formBreadcrumb']/li/ul/li[2]/span");

    public RegistrationEditSectionPage(Page parent) {
        super(parent);
    }

    @Override
    public String getPageUrl() {
        return "/registrationapp/editSection.page";
    }

    public void clickOnPhoneNumberEdit(){clickOn(PHONE_NUMBER_EDIT);}

    public void clearPhoneNumber(){findElement(PHONE_NUMBER).clear();}

    public void clickOnConfirmEdit(){
        clickOn(CONFIRM_EDIT);
    }

    public void enterPhoneNumber(String phone) {
        setText(PHONE_NUMBER, phone);
    }

    public ClinicianFacingPatientDashboardPage confirmPatient() throws InterruptedException{
        clickOn(CONFIRM);
        return new ClinicianFacingPatientDashboardPage(this);
    }
}
