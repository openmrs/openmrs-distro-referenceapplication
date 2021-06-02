package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.page.AdministrationPage;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.HtmlFormsPage;
import org.openmrs.reference.page.ManageFormsPage;
import org.openmrs.reference.page.ManageHtmlFormsPage;
import org.openmrs.uitestframework.test.TestBase;
import org.openqa.selenium.By;

import static org.junit.Assert.assertNotNull;

/**
 * Created by nata on 24.06.15.
 */
public class AddFormTest extends TestBase {
  private static String name = "newFormTest1";
  private static String description = "description of new form";
  private static String version = "1.2";
  private HomePage homePage;
  private HeaderPage headerPage;
  private ManageFormsPage manageForm;
  private AdministrationPage administrationPage;
  private ClinicianFacingPatientDashboardPage patientDashboardPage;
  private ManageHtmlFormsPage manageHtmlFormsPage;
  private HtmlFormsPage htmlFormsPage;

  @Before
  public void setUp() throws Exception {
	homePage = new HomePage(page);
	administrationPage = new AdministrationPage(page);
	headerPage = new HeaderPage(driver);
	htmlFormsPage = new HtmlFormsPage(page);
	manageHtmlFormsPage = new ManageHtmlFormsPage(page);
	manageForm = new ManageFormsPage(driver);
	patientDashboardPage = new ClinicianFacingPatientDashboardPage(page);
  }

  @Test
  @Category(BuildTests.class)
  public void addFormTest() throws Exception {
	manageHtmlFormsPage = homePage.goToAdministration().clickOnManageHtmlForms();
	if(manageHtmlFormsPage.getElementsIfExisting(By.xpath("//*[contains(text(), '" + name + "')]")).isEmpty()) {
	  manageHtmlFormsPage.clickOnNewHtmlForm();
	  htmlFormsPage.createNewFormTest(name, description, version);
    }	
	homePage.go();
	homePage.goToManageForm();
	if(!manageForm.addPresent()) {
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
	  if(headerPage != null) {
	    headerPage.clickOnHomeIcon();
		headerPage.logOut();
	  }
	}
}
