package org.openmrs.reference;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
//import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.page.AdministrationPage;
//import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.reference.page.HeaderPage;
//import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.ManageFormsPage;


/**
 * Created by nata on 24.06.15.
 */
public class AddFormTest extends ReferenceApplicationTestBase  {
    private HeaderPage headerPage;
    private ManageFormsPage manageForm;
    private AdministrationPage administrationPage;
//    private ClinicianFacingPatientDashboardPage patientDashboardPage;
    
    @Before
    public void setUp() throws Exception {
//        homePage = new HomePage(page);
    	  administrationPage = new AdministrationPage(page);
          assertPage(administrationPage);
          headerPage = new HeaderPage(driver);
          manageForm = new ManageFormsPage(driver);
//        patientDashboardPage = new ClinicianFacingPatientDashboardPage(page);
    }

//  @Ignore //ignore due to moving forms functionality
    @Test
    @Category(BuildTests.class)
    public void addFormTest() throws Exception {
//        homePage.goToManageForm();
    	administrationPage.goToManageForms();
    	assertPage(administrationPage);
        if(!manageForm.addPresent()) {
            manageForm.delete();
        }
        manageForm.add();
        manageForm.addLabel("Eye Report");
        manageForm.addIcon("icon-align-justify");
        manageForm.formIdFromUrl();
        manageForm.save();
        administrationPage.goToManageForms();
        headerPage.clickOnHomeIcon();
        assertNotNull("Eye Report", manageForm.FORM_EXIST);
        headerPage.clickOnHomeIcon();
//        homePage.goToManageForm();
        manageForm.deletePath();

    }
    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }
}
