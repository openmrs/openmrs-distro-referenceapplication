
package org.openmrs.reference.page;

/**
 * Created by nata on 22.07.15.
 */


import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ManageAppointmentPage extends AbstractBasePage {

    private static final By APPOINTMENT_SCHEDULING = By.id("appointmentschedulingui-homeAppLink-appointmentschedulingui-homeAppLink-extension");
    private static final By MANAGE_APPOINTMENT = By.id("appointmentschedulingui-manageAppointments-app");
    private static final By DELETE_APPOINTMENT = By.cssSelector("i.delete-item.icon-remove");
    private static final By YES_DELETE = By.cssSelector("button.button.confirm.right");
    private static final By ENTER_SERVICE = By.xpath("//div[@id='selectAppointmentType']/input");
    private static final By SEARCH = By.cssSelector("#searchButtons > button.confirm");
    private static final By APPOINTMENT = By.xpath("//table[@id='appointmentTable']/div[2]/div/div/div/div[2]/div[2]/div");
    private static final By NEXT = By.cssSelector("#selectAppointment > button.confirm");
    private static final By SAVE = By.xpath("//div[@id='confirmAppointment']/div[2]/button[2]");
    private static final By SERVICE_DROPDOWN = By.cssSelector("a.ng-scope.ng-binding");

    public void goToManageAppointment(){
        clickOn(APPOINTMENT_SCHEDULING);
        clickOn(MANAGE_APPOINTMENT);
    }
    public boolean deletePresent(){
        try {
            return driver.findElement(DELETE_APPOINTMENT) != null;
        }
        catch (Exception ex) {
            return false;
        }
    }
    public void deleteAppointment(){
        clickOn(DELETE_APPOINTMENT);
        clickOn(YES_DELETE);
    }
    public void searchAppointment(){
        clickOn(SEARCH);
        clickOn(APPOINTMENT);
    }
    public void saveAppointment(){
        clickOn(NEXT);
        clickOn(SAVE);
    }
    public void enterService(String service){
        setTextToFieldNoEnter(ENTER_SERVICE, service);
        waitForElement(SERVICE_DROPDOWN);
        clickOn(SERVICE_DROPDOWN);
    }

    public ManageAppointmentPage(WebDriver driver) {super(driver);}






    @Override
    public String expectedUrlPath() {
        return URL_ROOT + "/appointmentschedulingui/scheduleProviders.page";
    }
}

