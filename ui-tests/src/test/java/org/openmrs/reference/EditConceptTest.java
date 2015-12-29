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
    private ClinicianFacingPatientDashboardPage patientDashboardPage;
    private HeaderPage headerPage;
    private ConceptFormPage conceptFormPage;
    private SettingPage settingPage;


    @Before
    public void setUp() throws Exception {
        loginPage.loginAsAdmin();
        homePage = new HomePage(driver);
        assertPage(homePage);
        patientDashboardPage = new ClinicianFacingPatientDashboardPage(driver);
        headerPage = new HeaderPage(driver);
        conceptFormPage = new ConceptFormPage(driver);
        settingPage = new SettingPage(driver);
        homePage.goToAdministration();
    }

    @Test
    public void editConceptTest() throws Exception {
        conceptFormPage.clickOnViewConceptDictionary();
        conceptFormPage.findAddedConcept("test drug");
        if (conceptFormPage.conceptExist("test drug")) {
            conceptFormPage.clickOnAddedConcept();
            conceptFormPage.editConcept();
            conceptFormPage.deleteConcept();
            for (int second = 0; ; second++) {
                if (second >= 60) fail("timeout");
                try {
                    if (conceptFormPage.closeAlertAndGetItsText().matches("^Are you sure you want to delete this ENTIRE CONCEPT[\\s\\S]$"))
                        break;
                } catch (Exception e) {
                }
            }
        }
        conceptFormPage.clickOnAddNewConcept();
        conceptFormPage.enterFullyName("test drug");
        conceptFormPage.editConcept();
        conceptFormPage.selectClass("Drug");
        conceptFormPage.saveEdit();
        settingPage.waitForMessage();
        assertTrue(driver.getPageSource().contains("Concept saved successfully"));
        headerPage.clickOnHomeLink();
        homePage.goToAdministration();
        conceptFormPage.clickOnViewConceptDictionary();
        conceptFormPage.findAddedConcept(conceptFormPage.CONCEPT);
        conceptFormPage.clickOnAddedConcept();
        conceptFormPage.editConcept();
        conceptFormPage.retire();
        settingPage.waitForMessage();
        assertTrue(driver.getPageSource().contains("Concept Unretired Successfully"));
        conceptFormPage.deleteConcept();
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (conceptFormPage.closeAlertAndGetItsText().matches("^Are you sure you want to delete this ENTIRE CONCEPT[\\s\\S]$"))
                    break;
            } catch (Exception e) {
            }
        }
    }

    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeLink();
        headerPage.logOut();
    }

}
