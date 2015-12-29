package org.openmrs.reference;

import static junit.framework.Assert.assertTrue;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.uitestframework.test.TestBase;
import org.openmrs.uitestframework.test.TestData;

/**
 * Created by tomasz on 23.07.15.
 */
public class MergeVisitsTest extends TestBase {

    private HomePage homePage;
    private HeaderPage headerPage;
    private ClinicianFacingPatientDashboardPage patientDashboardPage;
    private TestData.PatientInfo patient;

    @Before
    public void setUp() {
        patient = createTestPatient();

        homePage = new HomePage(page);
        assertPage(homePage);
        patientDashboardPage = new ClinicianFacingPatientDashboardPage(page);
        headerPage = new HeaderPage(driver);
    }

    @After
    public void tearDown() throws InterruptedException {
        headerPage.clickOnHomeIcon();
        deletePatient(patient.uuid);
        headerPage.logOut();
    }

    @Ignore //ignored due to problems in enter date algorithm
    @Test
    public void mergeVisitsTest() {
    	patientDashboardPage.go(patient.uuid);
        assertPage(patientDashboardPage);
        patientDashboardPage.startVisit();
        Assert.assertTrue(patientDashboardPage.hasActiveVisit());
        patientDashboardPage.back();
        patientDashboardPage.addPastVisit();
        if(patientDashboardPage.errorPresent()) {
            patientDashboardPage.clickChangeDate();
            patientDashboardPage.enterDate();
        }
        patientDashboardPage.back();
        assertTrue(patientDashboardPage.mergeVisits().contains("Visits merged successfully"));

    }
}
