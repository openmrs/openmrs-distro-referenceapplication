package org.openmrs.reference;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.page.AdmitToInpatientPage;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.reference.page.FindPatientPage;
import org.openmrs.reference.page.PatientVisitsDashboardPage;
import org.openmrs.uitestframework.test.TestData;

public class AdmitPatientTest extends ReferenceApplicationTestBase{
	
	private static final String INPATIENT_WARD = "Inpatient Ward";
    private TestData.PatientInfo testPatient;

    @Before
    public void createTestData() throws Exception {
        testPatient = createTestPatient();
        createTestPatient();
    }

    @Test
    @Category(BuildTests.class)
    public void admitPatientTest() {
    	FindPatientPage findPatientPage = homePage.goToFindPatientRecord();
        findPatientPage.enterPatient(testPatient.identifier);
        findPatientPage.waitForPageToLoad();
        assertThat(findPatientPage.getFirstPatientIdentifier(), containsString(testPatient.identifier));
        ClinicianFacingPatientDashboardPage dashboardPage = findPatientPage.clickOnFirstPatient();
        PatientVisitsDashboardPage patientVisitsDashboardPage = dashboardPage.startVisit();
        AdmitToInpatientPage admitToInpatientPage = patientVisitsDashboardPage.goToAdmitToInpatient();
        admitToInpatientPage.confirm(INPATIENT_WARD);	
    }

    @After
    public void deleteTestPatient() {
        deletePatient(testPatient);
    }
}


