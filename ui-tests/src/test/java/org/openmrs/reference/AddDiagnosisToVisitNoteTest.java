package org.openmrs.reference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by nata on 22.06.15.
 */
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.uitestframework.test.TestBase;


public class AddDiagnosisToVisitNoteTest extends TestBase {
    private HomePage homePage;
    private ClinicianFacingPatientDashboardPage patientDashboardPage;
    private HeaderPage headerPage;


    @Before
    public void setUp() throws Exception {
        homePage = new HomePage(page);
        assertPage(homePage);
        patientDashboardPage = new ClinicianFacingPatientDashboardPage(page);
        headerPage = new HeaderPage(driver);
    }

    @Ignore//ignored due to possible application logout
    @Test
    public void AddDiagnosisToVisitNoteTest() throws Exception {
        homePage.goToActiveVisitPatient();
        patientDashboardPage.visitNote();
        patientDashboardPage.enterDiagnosis("Pne");
        patientDashboardPage.enterSecondaryDiagnosis("Bleed");
        assertEquals("Pneumonia", patientDashboardPage.primaryDiagnosis());
        assertEquals("Bleeding", patientDashboardPage.secondaryDiagnosis());
        patientDashboardPage.save();
        assertNotNull(patientDashboardPage.visitLink());

    }

    @After
    public void tearDown ()throws Exception {
        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }
}
