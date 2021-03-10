package org.openmrs.reference;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.openmrs.reference.groups.BuildTests;
import org.junit.experimental.categories.Category;
//import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.page.AdministrationPage;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.ManageFormsPage;



/**
 * Created by nata on 24.06.15.
 */
public class AddFormTest extends  ReferenceApplicationTestBase {

    private HeaderPage headerPage;
    private ManageFormsPage manageForm;
    private AdministrationPage administrationPage;


    @Before
    public void setUp() throws Exception {
    	 administrationPage = new AdministrationPage(page);
         assertPage(administrationPage);
         headerPage = new HeaderPage(driver);
         manageForm = new ManageFormsPage(driver);
    }

//    @Ignore //ignore due to moving forms functionality
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
        headerPage.clickOnHomeIcon();
        administrationPage.goToManageForms();
//        homePage.goToActiveVisitPatient();
        assertNotNull("Eye Report", manageForm.FORM_EXIST);
        headerPage.clickOnHomeIcon();
        administrationPage.goToManageForms();
//        homePage.goToManageForm();
        manageForm.deletePath();

    }
    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }
}
