package org.openmrs.reference;

/**
 * Created by nata on 20.07.15.
 */
import org.junit.*;
import static org.junit.Assert.*;
import org.openmrs.reference.page.*;
import org.openmrs.uitestframework.test.TestBase;


public class EditConceptTest extends TestBase {
    private HomePage homePage;
    private PatientDashboardPage patientDashboardPage;
    private HeaderPage headerPage;
    private ConceptFormPage conceptFormPage;



    @Before
    public void setUp() throws Exception {
        homePage = new HomePage(driver);
        loginPage.loginAsAdmin();
        assertPage(homePage);
        patientDashboardPage = new PatientDashboardPage(driver);
        headerPage = new HeaderPage(driver);
        conceptFormPage = new ConceptFormPage(driver);
        homePage.goToAdministration();
    }

    @Ignore
    @Test
    public void editConceptTest() throws Exception {
        conceptFormPage.clickOnViewConceptDictionary();
        conceptFormPage.clickOnAddNewConcept();
        conceptFormPage.enterFullyName("test drug");
        conceptFormPage.editConcept();
        conceptFormPage.selectClass("Drug");
        conceptFormPage.saveEdit();
        headerPage.clickOnHomeLink();
        homePage.goToAdministration();
        conceptFormPage.clickOnViewConceptDictionary();
        conceptFormPage.findAddedConcept(conceptFormPage.CONCEPT);
        conceptFormPage.clickOnAddedConcept();
        conceptFormPage.editConcept();
        conceptFormPage.retire();
        assertTrue(driver.getPageSource().contains("Concept Unretired Successfully"));
        conceptFormPage.deleteConcept();
        assertTrue(conceptFormPage.closeAlertAndGetItsText().matches("^Are you sure you want to delete this ENTIRE CONCEPT[\\s\\S]$"));

    }

    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeLink();
        headerPage.logOut();
    }

}
