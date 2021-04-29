package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.page.*;

import java.util.concurrent.TimeUnit;

public class EditPatientRelationshipTest  extends ReferenceApplicationTestBase {
    private HomePage homePage;
    private ClinicianFacingPatientDashboardPage clinicianFacingPatientDashboardPage;
    private RegistrationSummaryPage registrationSummaryPage;
    private HeaderPage headerPage;
    private EditPatientRelationshipPage editPatientRelationshipPage;
    private FindPatientPage findPatientPage;

    @Before
    public  void  setUp() throws Exception {
        homePage = new HomePage(page);
        headerPage = new HeaderPage(driver);
        clinicianFacingPatientDashboardPage = new ClinicianFacingPatientDashboardPage(page);
        registrationSummaryPage = new RegistrationSummaryPage(page);
        editPatientRelationshipPage =  new EditPatientRelationshipPage(registrationSummaryPage);
    }

        @Test
        @Category(BuildTests.class)
        public void editPatientRelationshipTest() throws Exception {
        FindPatientPage findPatientPage = homePage.goToFindPatientRecord();
        ClinicianFacingPatientDashboardPage clinicianFacingPatientDashboardPage = findPatientPage.clickOnFirstPatient();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        clinicianFacingPatientDashboardPage.goToRegistrationSummary();
            RegistrationSummaryPage registrationSummary = new RegistrationSummaryPage(page);
            registrationSummary.goToEditPatientRelationship();
            assertPage(editPatientRelationshipPage);

        }
    @After
    public void tearDown() throws Exception {

    }
    }


