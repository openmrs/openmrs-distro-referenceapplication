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
public class DeleteFormTest extends ReferenceApplicationTestBase {
//    private HomePage homePage;
	private AdministrationPage administrationPage;
    private HeaderPage headerPage;
    private ManageFormsPage manageForm;

    @Before
    public void setUp() throws Exception {
        administrationPage = new AdministrationPage(administrationPage);
//        homePage = new HomePage(page);
        assertPage(homePage);
        headerPage = new HeaderPage(driver);
        manageForm = new ManageFormsPage(driver);
    }

//    @Ignore//ignore due to moving forms functionality
    @Test
    @Category(BuildTests.class)
    public void deleteFormTest() throws Exception {
    	administrationPage.goToManageForms();
//        homePage.goToManageForm();
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
        manageForm.deletePath();
        assertNotNull("Add", manageForm.ADD_FORM);

    }
    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }
}
