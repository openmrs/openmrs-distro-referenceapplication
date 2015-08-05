package org.openmrs.reference;

/**
 * Created by nata on 21.07.15.
 */
import org.junit.*;
import static org.junit.Assert.*;
import org.openmrs.reference.page.*;
import org.openmrs.uitestframework.test.TestBase;



public class AddUserTest extends TestBase {
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

    @Test
    public void addUserTest() throws Exception {
        administrationPage.clickOnManageUsers();
        if (manageUserPage.userExists("super_nurse")) {
            manageUserPage.findUser("super_nurse");
            manageUserPage.deleteUser();
        }
        else {
            manageUserPage.clickOnAddUser();
            manageUserPage.createNewPerson();
            manageUserPage.saveUser();
            assertTrue(manageUserPage.errorDemographic().contains("You must define at least one name"));
            assertTrue(manageUserPage.errorGender().contains("Cannot be empty or null"));
            assertTrue(manageUserPage.errorUser().contains("User can log in with either Username or System Id"));
            manageUserPage.enterGivenFamily("Super", "Nurse");
            manageUserPage.saveUser();
            assertTrue(manageUserPage.errorUser().contains("User can log in with either Username or System Id"));
            assertTrue(driver.getPageSource().contains("Cannot be empty or null"));
            manageUserPage.clickOnFemale();
            manageUserPage.saveUser();
            assertTrue(driver.getPageSource().contains("User can log in with either Username or System Id"));
            manageUserPage.enterUsernamePassword("super_nurse", "supernurse", "supernurse123");
            manageUserPage.saveUser();
            assertFalse(manageUserPage.errorPassword().isEmpty());
            manageUserPage.enterUsernamePassword("super_nurse", "Nurse123", "Nurse123");
            manageUserPage.saveUser();
            settingPage.waitForMessage();
            assertTrue(driver.getPageSource().contains("User Saved"));
            headerPage.logOut();
            loginPage.login("super_nurse", "Nurse123");
            assertTrue(driver.getPageSource().contains("super_nurse"));
            headerPage.logOut();
            loginPage.loginAsAdmin();
            homePage.goToAdministration();
            administrationPage.clickOnManageUsers();
            manageUserPage.findUser("super_nurse");
            manageUserPage.deleteUser();
            settingPage.waitForMessage();
            assertTrue(driver.getPageSource().contains("Successfully deleted user."));
        }
    }

    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeLink();
        headerPage.logOut();
    }

}
