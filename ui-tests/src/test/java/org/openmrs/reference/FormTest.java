package org.openmrs.reference;

import static org.junit.Assert.assertNotNull;

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

/**
 * Created by nata on 24.06.15.
 */
public class FormTest extends TestBase {
	
	private static String name = "newFormTest1";
	private static String description = "description of new form";
	private static String version = "1.2";
	private HomePage homePage;
	private AdministrationPage administrationPage;
	private HeaderPage headerPage;
	private ManageFormsPage manageForm;
	private ManageHtmlFormsPage manageHtmlFormsPage;
	private HtmlFormsPage htmlFormsPage;
	private ClinicianFacingPatientDashboardPage patientDashboardPage;
	
	@Before
	public void setUp() throws Exception {
		homePage = new HomePage(page);
		assertPage(homePage.waitForPage());
		headerPage = new HeaderPage(driver);
		administrationPage = new AdministrationPage(page);
		manageForm = new ManageFormsPage(driver);
		htmlFormsPage = new HtmlFormsPage(page);
		manageHtmlFormsPage = new ManageHtmlFormsPage(page);
		patientDashboardPage = new ClinicianFacingPatientDashboardPage(page);
	}
	
	private void initiateFormPresence() throws InterruptedException {
		manageHtmlFormsPage = homePage.goToAdministration().clickOnManageHtmlForms();
		if (manageHtmlFormsPage.getElementsIfExisting(By.xpath("//*[contains(text(), '" + name + "')]")).isEmpty()) {
			manageHtmlFormsPage.clickOnNewHtmlForm();
			htmlFormsPage.createNewFormTest(name, description, version);
		}
		homePage.go();
		homePage.goToManageForm();
		if (!manageForm.addPresent()) {
			manageForm.delete();
		}
	}
	
	@Test
	@Category(BuildTests.class)
	public void addFormTest() throws Exception {	
		initiateFormPresence();
		manageForm.add();
		manageForm.addLabel("Eye Report");
		manageForm.addIcon("icon-align-justify");
		manageForm.formIdFromUrl();
		manageForm.save();
		headerPage.clickOnHomeIcon();
		assertNotNull("Eye Report", patientDashboardPage.FORM_EXIST);
	}
	
	@Test
	@Category(BuildTests.class)
	public void editFormTest() throws Exception {
		addFormTest();
		homePage.goToManageForm();
		manageForm.waitForPage();
		manageForm.editPath();
		manageForm.addLabel("Eye Test");
		manageForm.save();
		headerPage.clickOnHomeIcon();
		assertNotNull("Eye Test", patientDashboardPage.FORM_EXIST);
	}
	
	@Test
	@Category(BuildTests.class)
	public void deleteFormTest() throws Exception {
		addFormTest();
		homePage.goToManageForm();
		manageForm.waitForPage();
		manageForm.deletePath();
		assertNotNull("Add", manageForm.ADD);
	}
	
	@After
	public void tearDown() throws Exception {
	       homePage.go();
	}
}
