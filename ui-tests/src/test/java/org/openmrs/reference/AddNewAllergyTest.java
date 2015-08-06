package org.openmrs.reference;

/**
 * Created by nata on 25.06.15.
 */
import org.junit.*;
import static org.junit.Assert.*;
import org.openmrs.reference.page.AllergyPage;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.PatientDashboardPage;
import org.openmrs.uitestframework.test.TestBase;


public class AddNewAllergyTest extends TestBase {
    private HomePage homePage;
    private PatientDashboardPage patientDashboardPage;
    private HeaderPage headerPage;
    private AllergyPage allergyPage;




    @Before
    public void setUp() throws Exception {

        loginPage.loginAsAdmin();
        homePage = new HomePage(driver);
        assertPage(homePage);
        patientDashboardPage = new PatientDashboardPage(driver);
        headerPage = new HeaderPage(driver);
        allergyPage = new AllergyPage(driver);
        homePage.goToActiveVisitPatient();


    }

    @Test
    public void addNewAllergyTest() throws Exception {

        patientDashboardPage.clickOnAddAllergy();
        if (allergyPage.editPresent()){
            allergyPage.deletePresent();
            allergyPage.clickOnDeleteAllergy();
            allergyPage.clickOnConfirmDeleteAllergy();

        }
        else {
            allergyPage.clickOnAddNewAllergy();
            allergyPage.enterDrug("Aspirin");
            allergyPage.drugId();
            allergyPage.clickOnSaveAllergy();
            assertTrue(patientDashboardPage.visitLink().getText().contains("New Allergy Saved Successfully"));

        }
    }


    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }

}
