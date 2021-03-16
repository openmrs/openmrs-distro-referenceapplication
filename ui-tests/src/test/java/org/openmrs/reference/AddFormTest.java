package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.openmrs.reference.groups.BuildTests;
import org.junit.experimental.categories.Category;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.page.AdministrationPage;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.ManageFormsPage;


import static org.junit.Assert.assertNotNull;


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

    @Test
    @Category(BuildTests.class)
    public void addFormTest() throws Exception {
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
        assertNotNull("Eye Report", manageForm.FORM_EXIST);
        headerPage.clickOnHomeIcon();
        administrationPage.goToManageForms();
        manageForm.deletePath();

    }

    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }
}
