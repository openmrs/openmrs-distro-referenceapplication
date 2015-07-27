package org.openmrs.reference;

/**
 * Created by nata on 21.07.15.
 */
import org.junit.*;
import static org.junit.Assert.*;
import org.openmrs.reference.page.*;
import org.openmrs.uitestframework.test.TestBase;



public class PasswordCannotMatchUsernameTest extends TestBase {
    private HomePage homePage;
    private HeaderPage headerPage;
    private SettingPage settingPage;
    private AdministrationPage administrationPage;
    private ManageUserPage manageUserPage;


    @Before
    public void setUp() throws Exception {

        homePage = new HomePage(driver);
        loginPage.loginAsAdmin();
        assertPage(homePage);
        headerPage = new HeaderPage(driver);
        settingPage = new SettingPage(driver);
        administrationPage = new AdministrationPage(driver);
        manageUserPage = new ManageUserPage(driver);
        homePage.goToAdministration();
    }

    @Ignore
    @Test
    public void passwordCannotMatchUsernameTest() throws Exception {
        settingPage.clickOnSetting();
        settingPage.clickOnSecurity();
        settingPage.chooseFalse();
        settingPage.saveProperties();
        headerPage.clickOnHomeLink();
        homePage.goToAdministration();
        administrationPage.clickOnManageUsers();
        manageUserPage.clickOnAddUser();
        manageUserPage.createNewPerson();
        manageUserPage.enterUserMale("dr_house", "House", "dr_house", "Dr_house123");
        manageUserPage.chooseRole();
        manageUserPage.saveUser();
        assertTrue(driver.getPageSource().contains("User Saved"));
        manageUserPage.findUser("dr_house");
        manageUserPage.deleteUser();
        assertTrue(driver.getPageSource().contains("Successfully deleted user."));
        headerPage.clickOnHomeLink();
        homePage.goToAdministration();
        settingPage.clickOnSetting();
        settingPage.clickOnSecurity();
        settingPage.chooseTrue();
        assertTrue(driver.getPageSource().contains("Global properties saved"));
    }

    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeLink();
        headerPage.logOut();
    }

}
