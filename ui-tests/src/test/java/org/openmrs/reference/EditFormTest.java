package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.ManageFormsPage;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.page.AdministrationPage;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.uitestframework.test.TestBase;
import org.openqa.selenium.By;
import static org.junit.Assert.assertNotNull;


/**
 * Created by nata on 24.06.15.
 */
public class EditFormTest extends ReferenceApplicationTestBase {
//    private HomePage homePage;
 private AdministrationPage administrationPage;
    private HeaderPage headerPage;
    private ManageFormsPage manageForm;
    private ClinicianFacingPatientDashboardPage patientDashboardPage;

    @Before
    public void setUp() throws Exception {
    	administrationPage.goToManageForms();
//        homePage = new HomePage(page);
        assertPage(administrationPage);
        headerPage = new HeaderPage(driver);
        manageForm = new ManageFormsPage(driver);
//        patientDashboardPage = new ClinicianFacingPatientDashboardPage(page);
    }

//    @Ignore//ignore due to moving forms functionality
    @Test
    @Category(BuildTests.class)
    public void EditFormTest() throws Exception {
        administrationPage.goToManageForms();
        if(!manageForm.addPresent()) {
            manageForm.delete();
        }
        manageForm.add();
        manageForm.addLabel("Eye Report");
        manageForm.addIcon("icon-align-justify");
        manageForm.formIdFromUrl();
        manageForm.save();
        headerPage.clickOnHomeIcon();
        administrationPage.goToManageForms();
        manageForm.editPath();
        manageForm.addLabel("Eye Test");
        manageForm.save();
        headerPage.clickOnHomeIcon();
        administrationPage.goToManageForms();
//        homePage.goToActiveVisitPatient();
        assertNotNull("Eye Test", manageForm.FORM_EXIST);
//        Delete Form
        headerPage.clickOnHomeIcon();
       administrationPage.goToManageForms();
        manageForm.deletePath();
        assertNotNull("Add", manageForm.ADD_FORM);

    }
    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }
}

