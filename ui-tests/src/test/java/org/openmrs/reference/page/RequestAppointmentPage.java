package org.openmrs.reference.page;

import java.util.List;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class RequestAppointmentPage extends Page {

    private static final By MIN_FRAME_VALUE = By.id("min-time-frame-value");
    private static final By MAX_FRAME_VALUE = By.id("max-time-frame-value");
    private static final By MIN_FRAME_UNITS = By.id("min-time-frame-units");
    private static final By MAX_FRAME_UNITS = By.id("max-time-frame-units");
    private static final By APPOINTMENT_REQUESTS_LIST = By.id("miniPatientAppointmentRequests");
    private static final By APPOINTMENT_REQUEST_NOTE = By.id("notes");
    private static final By SAVE_REQUEST = By.id("save-button");
    private static final By APPOINTMENT_TYPE = By.id("appointment-type");
    private static final By SERVICE_DROPDOWN = By.cssSelector("a.ng-scope.ng-binding");

    public RequestAppointmentPage(Page page) {
        super(page);
    }

    public void enterAppointmentType(String type) {
        setTextToFieldNoEnter(APPOINTMENT_TYPE, type);
        waitForElement(SERVICE_DROPDOWN);
        clickOn(SERVICE_DROPDOWN);
    }

    public void enterMinimumValue(String value) {
        setText(MIN_FRAME_VALUE, value);
    }

    public void selectMinimumUnits(String units) {
        selectFrom(MIN_FRAME_UNITS, units);
    }

    public void enterMaximumValue(String value) {
        setText(MAX_FRAME_VALUE, value);
    }
    
    public void selectMaximumUnits(String units) {
        selectFrom(MAX_FRAME_UNITS, units);
    }
    
    public void addAppointmentRequestNote(String note) {
        findElement(APPOINTMENT_REQUEST_NOTE).clear();
        setText(APPOINTMENT_REQUEST_NOTE, note); 	
    }
    
    public ClinicianFacingPatientDashboardPage saveRequest() {
        clickOn(SAVE_REQUEST);
        return new ClinicianFacingPatientDashboardPage(this);
    }

    public List<WebElement> getAppointmentRequestsList() {
        return findElements(APPOINTMENT_REQUESTS_LIST); 
    }
    
    @Override
    public String getPageUrl() {
        return "/appointmentschedulingui/requestAppointment.page";
    }
}
