package org.openmrs.reference;

/**
 * Created by nata on 22.07.15.
 */
import org.junit.*;
import static org.junit.Assert.*;
import org.openmrs.reference.page.*;
import org.openmrs.uitestframework.test.TestBase;



public class PasswordRequiresDigitTest extends TestBase {
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
    public void passwordRequiresDigitTest() throws Exception {
        settingPage.clickOnSetting();
        settingPage.clickOnSecurity();
        settingPage.chooseFalseDigit();
        settingPage.saveProperties();
        headerPage.clickOnHomeLink();
        homePage.goToAdministration();
        administrationPage.clickOnManageUsers();
        manageUserPage.checkUser("dr_house");
        if (manageUserPage.userExist("dr_house")) {
            manageUserPage.clickOnUser();
            manageUserPage.deleteUser();
        }
            manageUserPage.clickOnAddUser();
            manageUserPage.createNewPerson();
            manageUserPage.enterUserMale("doctor", "House", "dr_house", "Doktorhouse");
            manageUserPage.chooseRole();
            manageUserPage.saveUser();
            manageUserPage.findUser("dr_house");
            manageUserPage.deleteUser();
            headerPage.clickOnHomeLink();
            homePage.goToAdministration();
            settingPage.clickOnSetting();
            settingPage.clickOnSecurity();
            settingPage.chooseTrueDigit();
            settingPage.waitForMessage();
            assertTrue(driver.getPageSource().contains("Global properties saved"));
        }

    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeLink();
        headerPage.logOut();
    }

}

