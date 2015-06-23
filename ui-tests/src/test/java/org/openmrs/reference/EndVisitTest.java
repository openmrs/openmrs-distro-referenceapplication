package org.openmrs.reference;

/**
 * Created by nata on 17.06.15.
 */
import org.junit.*;
import static org.junit.Assert.*;

import org.omg.PortableInterceptor.ACTIVE;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.PatientDashboardPage;
import org.openmrs.uitestframework.test.TestBase;
import org.openmrs.uitestframework.test.TestData;
import org.openqa.selenium.*;



public class EndVisitTest extends TestBase {
    private HomePage homePage;
    private PatientDashboardPage patientDashboardPage;
    private TestData.PatientInfo patient;
    private HeaderPage headerPage;



    @Before
    public void setUp() throws Exception {

        homePage = new HomePage(driver);
        loginPage.loginAsAdmin();
        assertPage(homePage);
        patientDashboardPage = new PatientDashboardPage(driver);
        headerPage = new HeaderPage(driver);


    }

    @Test
    public void EndVisitTest() throws Exception {

        homePage.GoToActiveVisitPatient();
        patientDashboardPage.endVisit();
        assertNotNull(By.id("referenceapplication.realTime.endVisit"));
        patientDashboardPage.waitForVisitLinkHidden();


    }

    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }

}