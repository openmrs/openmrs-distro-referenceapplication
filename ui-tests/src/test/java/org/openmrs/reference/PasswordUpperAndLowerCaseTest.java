package org.openmrs.reference;

/**
 * Created by nata on 22.07.15.
 */
import java.util.Random;

import org.junit.*;

import static org.junit.Assert.*;

import org.openmrs.reference.page.*;
import org.openmrs.uitestframework.test.TestBase;

public class PasswordUpperAndLowerCaseTest extends TestBase {
	
	public static final String DOCTOR_USERNAME = "dr_house" + new Random().nextInt(1024);

	private HomePage homePage;
	
	private HeaderPage headerPage;
	
	private SettingPage settingPage;
	
	private AdministrationPage administrationPage;
	
	private ManageUserPage manageUserPage;
	
	@Before
	public void setUp() throws Exception {
		loginPage.loginAsAdmin();
		homePage = new HomePage(driver);
		assertPage(homePage);
		headerPage = new HeaderPage(driver);
		settingPage = new SettingPage(driver);
		administrationPage = new AdministrationPage(driver);
		manageUserPage = new ManageUserPage(driver);
		homePage.goToAdministration();
	}
	
	@Test
	public void passwordUpperAndLowerCaseTest() throws Exception {
		settingPage.clickOnSetting();
		settingPage.clickOnSecurity();
		settingPage.chooseFalseCase();
		settingPage.saveProperties();
		headerPage.clickOnHomeLink();
		homePage.goToAdministration();
		administrationPage.clickOnManageUsers();
		manageUserPage.checkUser(DOCTOR_USERNAME);
		if (manageUserPage.userExist(DOCTOR_USERNAME)) {
			manageUserPage.clickOnUser();
			manageUserPage.deleteUser();
		}
		manageUserPage.clickOnAddUser();
		manageUserPage.createNewPerson();
		manageUserPage.enterUserMale("doctor", "House", DOCTOR_USERNAME, "drhouse1");
		manageUserPage.chooseRole();
		manageUserPage.saveUser();
		manageUserPage.findUser(DOCTOR_USERNAME);
		manageUserPage.deleteUser();
		headerPage.clickOnHomeLink();
		homePage.goToAdministration();
		settingPage.clickOnSetting();
		settingPage.clickOnSecurity();
		settingPage.chooseTrueCase1();
		settingPage.waitForMessage();
		assertTrue(driver.getPageSource().contains("Global properties saved"));
	}
	
	@After
	public void tearDown() throws Exception {
		headerPage.clickOnHomeLink();
		homePage.goToAdministration();
		administrationPage.clickOnManageUsers();
		manageUserPage.checkUser(DOCTOR_USERNAME);
		if (manageUserPage.userExist(DOCTOR_USERNAME)) {
			manageUserPage.clickOnUser();
			manageUserPage.deleteUser();
		}
		headerPage.clickOnHomeLink();
		headerPage.logOut();
	}
	
}
