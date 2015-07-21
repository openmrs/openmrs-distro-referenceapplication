package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.uitestframework.test.TestBase;
import org.openmrs.uitestframework.test.TestData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class FindPatientRecordTest extends TestBase {

    private String idToSearch;
    private HeaderPage headerPage;
    private HomePage homePage;
    private TestData.PatientInfo patient;

    @Before
    public void before() {
        homePage = new HomePage(driver);
        headerPage = new HeaderPage(driver);
        patient = createTestPatient();
        idToSearch = patient.identifier;
        loginPage.loginAsAdmin();
        assertPage(homePage);
    }

    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        deletePatient(patient.uuid);
        headerPage.logOut();
    }

    /**
     * Test for RA-819
     */
    @Test
    public void testFindPatientRecord() throws InterruptedException {
        //navigate "find patient record" page
        homePage.clickOnFindPatientRecord();
        driver.findElement(By.id("patient-search")).clear();
        driver.findElement(By.id("patient-search")).sendKeys(idToSearch);

        Thread.sleep(3 * 1000); //TODO: replace by WebDriverWait or so on

//        WebDriverWait wait = new WebDriverWait(driver, 5);
//        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(cssLocator));

        By cssLocator = By.cssSelector("#patient-search-results-table > tbody > tr > td");
        WebElement searchResultTableFirstRowFirstColumn = driver.findElement(cssLocator);
        assertNotNull(searchResultTableFirstRowFirstColumn);
        assertTrue(searchResultTableFirstRowFirstColumn.getText().contains(idToSearch));

    }
}
