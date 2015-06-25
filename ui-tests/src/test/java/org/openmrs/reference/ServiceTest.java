package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.ServicePage;
import org.openmrs.uitestframework.test.TestBase;
import org.openqa.selenium.By;

import static org.junit.Assert.assertTrue;

/**
 * Created by tomasz on 24.06.15.
 */
public class ServiceTest extends TestBase {

    private HomePage homePage;
    private HeaderPage headerPage;
    private ServicePage servicePage;
    private String[] editedValues = null;
    @Before
    public void before() {
        homePage = new HomePage(driver);
        headerPage = new HeaderPage(driver);
        servicePage = new ServicePage(driver);
        loginPage.loginAsAdmin();
        assertPage(homePage);
        servicePage.openManageServiceTypes();
    }

    @After
    public void tearDown() throws Exception {
        if(servicePage.serviceExists("Test2")) {
            servicePage.deleteService("Test2");
        }
        if(editedValues != null) {
            servicePage.addNewService();
            servicePage.putServiceData(editedValues[0],editedValues[1],editedValues[2]);
            editedValues = null;
        }
        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }

    @Test
    public void AddServiceTest() {
        servicePage.addNewService();
        assertPage(servicePage);
        servicePage.putServiceData("Test2", "10", "This is a Service Type added only for test purpose");
        servicePage.addNewService();
        servicePage.putServiceData("Test2", "10", "This is a Service Type added only for test purpose");
        assertTrue(driver.getPageSource().contains("Name is duplicated"));
        servicePage.cancel();
    }

    @Test
    public void EditServiceTest() {
        driver.findElement(By.className("editAppointmentType")).click();
        editedValues = new String[]{servicePage.getNameValue(),servicePage.getDurationValue(),servicePage.getDescriptionValue()};
        servicePage.editServiceName("");
        assertTrue(driver.getPageSource().contains("Invalid name"));
        servicePage.editServiceDuration("abc");
        assertTrue(driver.getPageSource().contains("Duration must be a positive number"));
        servicePage.putServiceData("Test2", "10", "This is a Service Type added only for test purpose");
        assertTrue(servicePage.serviceExists("Test2"));
    }
}
