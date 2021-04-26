package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.reference.page.AdministrationPage;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.ManageFormsPage;
import org.openmrs.uitestframework.test.TestBase;
import org.openqa.selenium.By;

import static org.junit.Assert.assertNotNull;


/**
 * Created by nata on 24.06.15.
 */
public class DeleteFormTest extends TestBase {
    private HomePage homePage;
    private HeaderPage headerPage;
    private ManageFormsPage manageForm;
    private static String name = "newFormTest";
    private static String description = "description of new form";
    private static  String version = "1.2";
    private static final By NEW_HTML_FORM = By.cssSelector("a[href='htmlForm.form']");
    private AdministrationPage administrationPage;
    //    private ManageHtmlFormsPage manageHtmlFormsPage;
//    private HtmlFormsPage htmlFormsPage;
    @Before
    public void setUp() throws Exception {

        homePage = new HomePage(page);
//        assertPage(homePage);
        administrationPage = new AdministrationPage(page);
        headerPage = new HeaderPage(driver);
        manageForm = new ManageFormsPage(driver);
    }

    @Test
    public void deleteFormTest() throws Exception {
        //        manageHtmlFormsPage =  homePage.goToAdministration().clickOnManageHtmlForms();

//        if (manageHtmlFormsPage.getElementsIfExisting(By.xpath("//*[contains(text(), '"+ name+ "')]")).isEmpty()) {
//            manageHtmlFormsPage.clickOnNewHtmlForm();
//            HtmlFormsPage newHtmlForms = new HtmlFormsPage(manageHtmlFormsPage);
//            newHtmlForms.CreateNewFormTest(name, description, version);
//        }
        homePage.go();
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
        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }
}
