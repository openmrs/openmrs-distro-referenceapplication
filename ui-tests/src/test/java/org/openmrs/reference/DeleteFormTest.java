package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.ManageFormsPage;
import org.openmrs.uitestframework.test.TestBase;

import static org.junit.Assert.assertNotNull;


/**
 * Created by nata on 24.06.15.
 */
public class DeleteFormTest extends TestBase {
    private HomePage homePage;
    private HeaderPage headerPage;
    private ManageFormsPage manageForm;

    @Before
    public void setUp() throws Exception {
        homePage = new HomePage(page);    
        assertPage(homePage.waitForPage());
        headerPage = new HeaderPage(driver);
        manageForm = new ManageFormsPage(driver);
    }

    @Test
    @Category(BuildTests.class)
    public void deleteFormTest() throws Exception {
        homePage.goToManageForm();
        if (!manageForm.addPresent()) {
            manageForm.delete();
        }
        manageForm.add();
        manageForm.addLabel("Eye Report");
        manageForm.addIcon("icon-align-justify");
        manageForm.formIdFromUrl();
        manageForm.save();
        headerPage.clickOnHomeIcon();
        homePage.goToManageForm();
        manageForm.deletePath();
        assertNotNull("Add", manageForm.ADD);
    }

    @After
    public void tearDown() throws Exception {
	if(headerPage !=null) {
        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }
  }
}
