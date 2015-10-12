package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.page.FindPatientPage;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.uitestframework.test.TestBase;
import org.openmrs.uitestframework.test.TestData;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

/**
 * @author yulia 
 */
public class FindPatientRecordTest extends TestBase {

    private String idToSearch;
    private HeaderPage headerPage;
    private HomePage homePage;
    private FindPatientPage findPatientPage;
    private TestData.PatientInfo patient;

    @Before
    public void before() {
        homePage = new HomePage(driver);
        headerPage = new HeaderPage(driver);
        findPatientPage = new FindPatientPage(driver);
        patient = createTestPatient();
        idToSearch = patient.identifier;
        loginPage.loginAsAdmin();
        assertPage(homePage);
    }

    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        deletePatient(patient.uuid);
        headerPage.logOut();
    }

    @Test
    @Category(org.openmrs.reference.groups.BuildTests.class)
    public void testFindPatientRecord() throws InterruptedException {
        //navigate "find patient record" page
        homePage.clickOnFindPatientRecord();
        findPatientPage.enterPatient(idToSearch);
        findPatientPage.waitForResultTable();
        assertThat(findPatientPage.findFirstPatientId(), containsString(idToSearch));
    }
}
