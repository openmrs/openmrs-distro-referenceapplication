package org.openmrs.reference;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by nata on 25.06.15.
 */
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.reference.page.FindPatientPage;
import org.openmrs.reference.page.PatientVisitsDashboardPage;
import org.openmrs.uitestframework.test.TestData;


public class AdmitExitInpatientTest extends ReferenceApplicationTestBase {

    private static final String INPATIENT_WARD = "Inpatient Ward";

    private TestData.PatientInfo testPatient;

    @Before
    public void createTestData() throws Exception {
        testPatient = createTestPatient();
    }

    @Ignore
    public void admitExitInpatientTest() throws Exception {
        FindPatientPage findPatientPage = homePage.goToFindPatientRecord();
        findPatientPage.enterPatient(testPatient.identifier);
        PatientVisitsDashboardPage patientVisitsDashboardPage = findPatientPage.clickOnFirstPatient().startVisit();

        patientVisitsDashboardPage = (PatientVisitsDashboardPage) patientVisitsDashboardPage.goToAdmitToInpatient().confirm(INPATIENT_WARD);
        assertThat(patientVisitsDashboardPage.getEncountersCount(), is(1));

        patientVisitsDashboardPage.goToExitFromInpatient().confirm(INPATIENT_WARD);
        assertThat(patientVisitsDashboardPage.getEncountersCount(), is(2));
    }

    @After
    public void deleteTestPatient(){
        deletePatient(testPatient.uuid);
    }
}
