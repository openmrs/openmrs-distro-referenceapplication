package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.PatientDashboardPage;
import org.openmrs.reference.page.ServicePage;
import org.openmrs.uitestframework.test.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by tomasz on 24.06.15.
 */
public class ServiceTest extends TestBase {

    private HomePage homePage;
    private HeaderPage headerPage;
    private ServicePage servicePage;
    private PatientDashboardPage patientDashboardPage;
    private String[] editedValues = null;

    @Before
    public void before() {
        homePage = new HomePage(driver);
        headerPage = new HeaderPage(driver);
        servicePage = new ServicePage(driver);
        patientDashboardPage= new PatientDashboardPage(driver);
        loginPage.loginAsAdmin();
        assertPage(homePage);
        servicePage.openManageServiceTypes();
    }

    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }

    @Test
    public void addEditDeleteServiceTest() throws InterruptedException{
        servicePage.addNewService();
        if(servicePage.serviceExistsOnPage("ATest2")) {
            servicePage.deleteService("ATest2");
        }
        if(servicePage.serviceExistsOnPage("ATest3")) {
            servicePage.deleteService("ATest3");
        }
        assertPage(servicePage);
        servicePage.putServiceData("ATest2", "10", "This is a Service Type added only for test purpose");
        servicePage.waitForServiceMenu();
        assertTrue(servicePage.serviceExistsOnPage("ATest2"));
        servicePage.addNewService();
        servicePage.putServiceData("ATest2", "10", "This is a Service Type added only for test purpose");
        assertTrue(driver.getPageSource().contains("Name is duplicated"));
        servicePage.cancel();
        servicePage.waitForServiceMenu();
        servicePage.startEditService("ATest2");
        servicePage.editServiceName("");
        assertTrue(driver.getPageSource().contains("Invalid name"));
        servicePage.editServiceDuration("abc");
        assertTrue(driver.getPageSource().contains("Duration must be a positive number"));
        servicePage.putServiceData("ATest3", "20", "This is a Service Type edited only for test purpose");
        servicePage.waitForServiceMenu();
        assertFalse(servicePage.serviceExistsOnPage("ATest2"));
        assertTrue(servicePage.serviceExistsOnPage("ATest3"));
        servicePage.deleteService("ATest3");
        servicePage.waitForServiceMenu();
        patientDashboardPage.waitForVisitLinkHidden();
        assertFalse(servicePage.serviceExistsOnPage("ATest3"));
    }


}
