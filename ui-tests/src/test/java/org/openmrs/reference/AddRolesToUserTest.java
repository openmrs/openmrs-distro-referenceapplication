package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.reference.page.*;
import org.openmrs.uitestframework.test.TestBase;
import org.openmrs.uitestframework.test.TestData;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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


    private void reLoginAsUser() throws InterruptedException {
        manageUserPage.clickOnHomeLink();
        headerPage.logOut();
        loginPage.login("super_nurse","Nurse321");
    }

    private void reLoginAsAdmin() throws InterruptedException {
        headerPage.logOut();
        loginPage.loginAsAdmin();
    }
    @Test
    public void addRolesToUserTest() throws InterruptedException {
        homePage.goToAdministration();
        administrationPage.clickOnManageUsers();

        Map<String,Integer> roleModules = new HashMap();

        fillInRoleModules(roleModules);
        if(!manageUserPage.userExists("super_nurse")) {
            manageUserPage.clickOnAddUser();
            manageUserPage.createNewPerson();
            manageUserPage.fillInPersonName("Super", "Nurse", "super_nurse", "Nurse321");
        }
        String oldRole = null;
        for(Entry<String, Integer> role : roleModules.entrySet()) {
            manageUserPage.assignRolesToUser(oldRole,role.getKey(),"super_nurse");
            reLoginAsUser();
            assertThat(homePage.numberOfAppsPresent(), is(role.getValue()));
            reLoginAsAdmin();
            oldRole = role.getKey();
            homePage.goToAdministration();
            administrationPage.clickOnManageUsers();

        }

    }

    private void fillInRoleModules(Map<String, Integer> roleModules) {
        roleModules.put("roleStrings.Anonymous",0);
        roleModules.put("roleStrings.Application:ConfiguresAppointmentScheduling",1);
        roleModules.put("roleStrings.Application:EntersADTEvents",2);
        roleModules.put("roleStrings.Application:HasSuperUserPrivileges",8);
        roleModules.put("roleStrings.Application:RegistersPatients",3);
        roleModules.put("roleStrings.Application:SchedulesAndOverbooksAppointments",1);
        roleModules.put("roleStrings.Application:SeesAppointmentSchedule",1);
        roleModules.put("roleStrings.Application:UsesPatientSummary",1);
        roleModules.put("roleStrings.Authenticated",0);
        roleModules.put("roleStrings.Organizational:SystemAdministrator",2);
        roleModules.put("roleStrings.SystemDeveloper",8);
        roleModules.put("roleStrings.Application:AdministersSystem",1);
        roleModules.put("roleStrings.Application:ConfiguresForms",0);
        roleModules.put("roleStrings.Application:EntersVitals",2);
        roleModules.put("roleStrings.Application:ManagesAtlas",1);
        roleModules.put("roleStrings.Application:ConfiguresMetadata",0);
        roleModules.put("roleStrings.Application:ManagesProviderSchedules",1);
        roleModules.put("roleStrings.Application:RequestsAppointments",0);
        roleModules.put("roleStrings.Application:SchedulesAppointments",1);
        roleModules.put("roleStrings.Application:UsesCaptureVitalsApp",1);
        roleModules.put("roleStrings.Application:WritesClinicalNotes", 2);
        roleModules.put("roleStrings.Organizational:Doctor", 3);
        roleModules.put("roleStrings.Organizational:Nurse", 4);
        roleModules.put("roleStrings.Organizational:RegistrationClerk", 4);
    }
    @After
    public void tearDown() throws Exception {
        manageUserPage.removeUser("super_nurse");
        manageUserPage.clickOnHomeLink();
        headerPage.logOut();
    }
}
