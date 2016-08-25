package org.openmrs.reference;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.page.FindPatientPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.uitestframework.test.TestBase;
import org.openmrs.uitestframework.test.TestData;

/**
 * @author yulia
 */
public class FindPatientRecordTest extends ReferenceApplicationTestBase {

    private String idToSearch;
    private TestData.PatientInfo patient;

    @Before
    public void setup() {
        patient = createTestPatient();
        idToSearch = patient.identifier;
    }

    @After
    public void tearDown() throws Exception {
        deletePatient(patient.uuid);
    }

    @Test
    @Category(BuildTests.class)
    public void testFindPatientRecord() throws InterruptedException {
        FindPatientPage findPatientPage = homePage.clickOnFindPatientRecord();
        findPatientPage.enterPatient(idToSearch);
        findPatientPage.waitForResultTable();
        assertThat(findPatientPage.findFirstPatientId(), containsString(idToSearch));
    }
}
