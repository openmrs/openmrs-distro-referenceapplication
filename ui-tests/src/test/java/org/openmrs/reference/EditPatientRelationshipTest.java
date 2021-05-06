package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.page.*;

public class EditPatientRelationshipTest  extends ReferenceApplicationTestBase{
    private static String name = "patients";
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
        clinicianFacingPatientDashboardPage.goToRegistrationSummary();

        RegistrationSummaryPage registrationSummary = new RegistrationSummaryPage(page);
        registrationSummary.goToEditPatientRelationship();

        EditPatientRelationshipPage editPatientRelationshipPage = new EditPatientRelationshipPage(registrationSummaryPage);
        editPatientRelationshipPage.clickOnSelectRelationshipType();
        editPatientRelationshipPage.getPageUrl();

    }

    @After
    public void tearDown() throws Exception {
        if (headerPage != null) {
            headerPage.clickOnHomeIcon();
            headerPage.logOut();
        }
    }
}
