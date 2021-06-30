package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

public class RequestAppointmentPage extends Page {

    private static final By FRAME_VALUE = By.id("min-time-frame-value");
    private static final By FRAME_UNITS = By.id("min-time-frame-units");
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

    public void enterValue(String value) {
        setText(FRAME_VALUE, value);
    }

    public void selectUnits(String units) {
        selectFrom(FRAME_UNITS, units);
    }

    public ClinicianFacingPatientDashboardPage saveRequest() {
        clickOn(SAVE_REQUEST);
        return new ClinicianFacingPatientDashboardPage(this);
    }

    @Override
    public String getPageUrl() {
        return "/appointmentschedulingui/requestAppointment.page";
    }
}

