package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.AdministrationPage;
import org.openmrs.reference.page.AppointmentBlocksPage;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.uitestframework.test.TestBase;

/**
 * Created by tomasz on 10.07.15.
 */
public class AddRolesToUserTest extends TestBase {

    private HomePage homePage;
    private HeaderPage headerPage;
    private AdministrationPage administrationPage;
    private ManageUserPage manageUserPage;

    @Before
    public void setUp() throws Exception {
        homePage = new HomePage(driver);

        loginPage.loginAsAdmin();
        assertPage(homePage);
        headerPage = new HeaderPage(driver);
        administrationPage = new AdministrationPage(driver);
        manageUserPage = new ManageUserPage(driver);
    }

    @Test
    public void addRolesToUserTest() throws InterruptedException {
//        driver.findElement(By.id("coreapps-systemadministration-homepageLink-coreapps-systemadministration-homepageLink-extension")).click();
//        driver.findElement(By.id("referenceapplication-legacyAdmin-app")).click();
        homePage.openLegacyAdministrationApp();
        administrationPage.clickOnManageUsers();
        driver.findElement(By.linkText("Add User")).click();
        driver.findElement(By.id("createNewPersonButton")).click();
        driver.findElement(By.name("person.names[0].givenName")).clear();
        driver.findElement(By.name("person.names[0].givenName")).sendKeys("Super");
        driver.findElement(By.name("person.names[0].familyName")).clear();
        driver.findElement(By.name("person.names[0].familyName")).sendKeys("Nurse");
        driver.findElement(By.name("userFormPassword")).clear();
        driver.findElement(By.name("userFormPassword")).sendKeys("Nurse321");
        driver.findElement(By.name("confirm")).clear();
        driver.findElement(By.name("confirm")).sendKeys("Nurse321");
        driver.findElement(By.id("saveButton")).click();
        driver.findElement(By.id("F")).click();
        driver.findElement(By.name("userFormPassword")).clear();
        driver.findElement(By.name("userFormPassword")).sendKeys("Nurse321");
        driver.findElement(By.name("confirm")).clear();
        driver.findElement(By.name("confirm")).sendKeys("Nurse321");
        driver.findElement(By.id("saveButton")).click();
        driver.findElement(By.name("name")).clear();
        driver.findElement(By.name("name")).sendKeys("super_nurse");
        driver.findElement(By.name("action")).click();
        driver.findElement(By.linkText("34-9")).click();
        driver.findElement(By.id("saveButton")).click();
        driver.findElement(By.name("name")).clear();
        driver.findElement(By.name("name")).sendKeys("super_nurse");
        driver.findElement(By.name("action")).click();
        driver.findElement(By.linkText("34-9")).click();
        driver.findElement(By.id("saveButton")).click();
        driver.findElement(By.linkText("Log out")).click();
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("super_nurse");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("Nurse321");
        driver.findElement(By.id("login-button")).click();
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("super_nurse");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("Nurse321");
        driver.findElement(By.id("login-button")).click();
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("Admin123");
        driver.findElement(By.id("login-button")).click();
        driver.findElement(By.name("name")).clear();
        driver.findElement(By.name("name")).sendKeys("super_nurse");
        driver.findElement(By.name("action")).click();
        driver.findElement(By.linkText("34-9")).click();
        driver.findElement(By.name("userFormPassword")).clear();
        driver.findElement(By.name("userFormPassword")).sendKeys("Nurse321");
        driver.findElement(By.name("confirm")).clear();
        driver.findElement(By.name("confirm")).sendKeys("Nurse321");
        driver.findElement(By.id("saveButton")).click();
        driver.findElement(By.linkText("Log out")).click();
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("super_nurse");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("Nurse321");
        driver.findElement(By.id("login-button")).click();
        driver.findElement(By.linkText("Logout")).click();
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("Admin123");
        driver.findElement(By.id("login-button")).click();
        driver.findElement(By.cssSelector("i.icon-cogs")).click();
        driver.findElement(By.id("referenceapplication-legacyAdmin-app")).click();
        driver.findElement(By.linkText("Manage Users")).click();
        driver.findElement(By.name("name")).clear();
        driver.findElement(By.name("name")).sendKeys("super_nurse");
        driver.findElement(By.name("action")).click();
        driver.findElement(By.linkText("34-9")).click();
        driver.findElement(By.id("roleStrings.Anonymous")).click();
        driver.findElement(By.id("roleStrings.Application:ConfiguresAppointmentScheduling")).click();
        driver.findElement(By.id("saveButton")).click();
        driver.findElement(By.linkText("Log out")).click();
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("super_nurse");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("Nurse321");
        driver.findElement(By.id("login-button")).click();
        driver.findElement(By.linkText("Home")).click();
        driver.findElement(By.linkText("Logout")).click();
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("Admin123");
        driver.findElement(By.id("login-button")).click();
        driver.findElement(By.id("coreapps-systemadministration-homepageLink-coreapps-systemadministration-homepageLink-extension")).click();
        driver.findElement(By.id("referenceapplication-legacyAdmin-app")).click();
        driver.findElement(By.linkText("Manage Users")).click();
        driver.findElement(By.name("name")).clear();
        driver.findElement(By.name("name")).sendKeys("super_nurse");
        driver.findElement(By.name("action")).click();
        driver.findElement(By.linkText("34-9")).click();
        driver.findElement(By.id("roleStrings.Application:ConfiguresAppointmentScheduling")).click();
        driver.findElement(By.id("roleStrings.Application:EntersADTEvents")).click();
        driver.findElement(By.id("saveButton")).click();
        driver.findElement(By.linkText("Home")).click();
        driver.findElement(By.linkText("Logout")).click();
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("super_nurse");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("Nurse321");
        driver.findElement(By.id("login-button")).click();
        driver.findElement(By.linkText("Logout")).click();
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("Admin123");
        driver.findElement(By.id("login-button")).click();
        driver.findElement(By.id("coreapps-systemadministration-homepageLink-coreapps-systemadministration-homepageLink-extension")).click();
        driver.findElement(By.id("referenceapplication-legacyAdmin-app")).click();
        driver.findElement(By.linkText("Manage Users")).click();
        driver.findElement(By.name("name")).clear();
        driver.findElement(By.name("name")).sendKeys("super_nurse");
        driver.findElement(By.name("action")).click();
        driver.findElement(By.linkText("34-9")).click();
        driver.findElement(By.id("roleStrings.Application:EntersADTEvents")).click();
        driver.findElement(By.id("roleStrings.Application:HasSuperUserPrivileges")).click();
        driver.findElement(By.id("saveButton")).click();
        driver.findElement(By.linkText("Home")).click();
        driver.findElement(By.linkText("Logout")).click();
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("super_nurse");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("Nurse321");
        driver.findElement(By.id("login-button")).click();
        driver.findElement(By.linkText("Logout")).click();
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("Admin123");
        driver.findElement(By.id("login-button")).click();
        driver.findElement(By.id("coreapps-systemadministration-homepageLink-coreapps-systemadministration-homepageLink-extension")).click();
        driver.findElement(By.id("referenceapplication-legacyAdmin-app")).click();
        driver.findElement(By.linkText("Manage Users")).click();
        driver.findElement(By.name("name")).clear();
        driver.findElement(By.name("name")).sendKeys("super_nurse");
        driver.findElement(By.name("action")).click();
        driver.findElement(By.linkText("34-9")).click();
        driver.findElement(By.id("roleStrings.Application:HasSuperUserPrivileges")).click();
        driver.findElement(By.id("roleStrings.Application:RegistersPatients")).click();
        driver.findElement(By.id("saveButton")).click();
        driver.findElement(By.linkText("Home")).click();
        driver.findElement(By.linkText("Logout")).click();
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("super_nurse");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("Nurse321");
        driver.findElement(By.id("login-button")).click();
        driver.findElement(By.linkText("Logout")).click();

    }

    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }
}
