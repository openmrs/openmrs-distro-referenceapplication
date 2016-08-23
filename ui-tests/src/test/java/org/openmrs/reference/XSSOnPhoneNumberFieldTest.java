package org.openmrs.reference;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.page.ActiveVisitsPage;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.reference.page.RegistrationEditSectionPage;
import org.openmrs.uitestframework.test.RestClient;
import org.openmrs.uitestframework.test.TestData;

import static org.junit.Assert.assertTrue;


/**
 * Created by nata on 20.07.15.
 */
public class XSSOnPhoneNumberFieldTest extends ReferenceApplicationTestBase {

    private static final String VISIT_TYPE_UUID = "7b0f5697-27e3-40c4-8bae-f4049abfb4ed";
    private static final String LOCATION_UUID = "8d6c993e-c2cc-11de-8d13-0010c6dffd0f";

    private ClinicianFacingPatientDashboardPage patientDashboardPage;
    private RegistrationEditSectionPage registrationEditSectionPage;
    private ActiveVisitsPage activeVisitsPage;
    private TestData.PatientInfo patient;

    @Before
    public void setup(){
        patient = createTestPatient();
        createTestVisit();
    }

    @Test
    @Category(BuildTests.class)
    public void  XSSOnPhoneNumberFieldTest() throws Exception {
        activeVisitsPage = homePage.goToActiveVisitsSearch();
        activeVisitsPage.search(patient.identifier);
        patientDashboardPage = activeVisitsPage.goToPatientDashboardOfLastActiveVisit();
        patientDashboardPage.clickOnShowContact();
        registrationEditSectionPage = patientDashboardPage.clickOnEditContact();
        registrationEditSectionPage.clickOnPhoneNumberEdit();
        registrationEditSectionPage.clearPhoneNumber();
        registrationEditSectionPage.enterPhoneNumber("<script>alert(0)</script>");
        registrationEditSectionPage.clickOnConfirmEdit();
        assertTrue(driver.getPageSource().contains("Must be a valid phone number (with +, -, numbers or parentheses)"));
        registrationEditSectionPage.clearPhoneNumber();
        registrationEditSectionPage.enterPhoneNumber("111111111");
        registrationEditSectionPage.clickOnConfirmEdit();
        patientDashboardPage = registrationEditSectionPage.confirmPatient();
        assertTrue(driver.getPageSource().contains("111111111\n        <em>Telephone Number</em>"));

    }

    @After
    public void teardown(){
        deletePatient(patient.uuid);
    }

    private void createTestVisit(){
        JsonNode visit = RestClient.post("visit", new TestData.TestVisit(patient.uuid, VISIT_TYPE_UUID, LOCATION_UUID));
    }
}

