package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.*;
import org.openmrs.uitestframework.test.TestBase;
import org.openqa.selenium.By;

import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertNotNull;


/**
 * Created by nata on 24.06.15.
 */
public class AddFormTest extends TestBase {
    private HomePage homePage;
    private static String name = "newFormTest";
    private static String description = "description of new form";
    private static  String version = "1.2";
    private HeaderPage headerPage;
    private ManageFormsPage manageForm;
    private ClinicianFacingPatientDashboardPage patientDashboardPage;
    private AdministrationPage administrationPage;
    private ManageHtmlFormsPage manageHtmlFormsPage;
    private HtmlFormsPage htmlFormsPage;

    @Before
    public void setUp() throws Exception {
        homePage = new HomePage(page);
        headerPage = new HeaderPage(driver);
        administrationPage = new AdministrationPage(page);
        manageForm = new ManageFormsPage(driver);
        patientDashboardPage = new ClinicianFacingPatientDashboardPage(page);

    }

    @Test
    public void addFormTest() throws Exception {
     manageHtmlFormsPage =  homePage.goToAdministration().clickOnManageHtmlForms();

     if (manageHtmlFormsPage.getElementsIfExisting(By.xpath("//*[contains(text(), '"+ name + "')]")).isEmpty()){
         manageHtmlFormsPage.clickOnNewHtmlForm();
         HtmlFormsPage newHtmlForms = new HtmlFormsPage(manageHtmlFormsPage);
         newHtmlForms.createNewFormTest(name,description,version);
     }
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
        assertNotNull("Eye Report", patientDashboardPage.FORM_EXIST);
        headerPage.clickOnHomeIcon();
        homePage.goToManageForm();
        manageForm.deletePath();

    }

    @After
    public void tearDown() throws Exception {
        if(headerPage !=null) {
            headerPage.clickOnHomeIcon();
            headerPage.logOut();
        }
    }
}
