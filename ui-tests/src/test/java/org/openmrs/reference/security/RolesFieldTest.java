package org.openmrs.reference.security;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.page.AddEditUserPage;
import org.openmrs.reference.page.AdministrationPage;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.ManageRolesPage;
import org.openmrs.reference.page.ManageUserPage;
import org.openmrs.reference.page.RoleFormPage;
import org.openmrs.uitestframework.test.TestBase;

public class RolesFieldTest extends TestBase {
	
	private HomePage homePage;
	private HeaderPage headerPage;
	private ManageRolesPage manageRolesPage;
	private AdministrationPage administrationPage;
	private RoleFormPage roleFormPage;
	private ManageUserPage manageUserPage;
	private AddEditUserPage addEditUserPage;
	private static String user = "<script>alert(1);</script>";
	private static String description = "some description";
	private static String findUser = "admin";
	
	@Before
	public void setUp() throws Exception {
		homePage = new HomePage(page);
		assertPage(homePage.waitForPage());
		headerPage = new HeaderPage(driver);
		manageRolesPage = new ManageRolesPage(page);
		roleFormPage = new RoleFormPage(page);
		addEditUserPage = new AddEditUserPage(page);
		manageUserPage = new ManageUserPage(page);
		administrationPage = new AdministrationPage(page);
	}
	
	@Test
	@Category(BuildTests.class)
	public void goToManageRoles() throws InterruptedException {
		homePage.goToAdministration().goToManageRolesPage();
		manageRolesPage.clickOnAddRole();
		roleFormPage.enterRoleTitle(user, description);
		roleFormPage.waitForPage();
		homePage.go();
		homePage.goToAdministration().clickOnManageUsers();
		manageUserPage.findUser(findUser);
		manageUserPage.clickOnFirstSearchResult();
		addEditUserPage.clickOnConfirmAdmin();
		addEditUserPage.saveUser();
	}
	
	@After
	public void tearDown() throws Exception {
		if (headerPage != null) {
			headerPage.clickOnHomeIcon();
			headerPage.logOut();
		}
	}
}
