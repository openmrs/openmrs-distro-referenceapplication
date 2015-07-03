package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.ProviderPage;
import org.openmrs.uitestframework.test.TestBase;

/**
 * Created by tomasz on 02.07.15.
 */
public class ProviderTest extends TestBase {

    private HeaderPage headerPage;
    private HomePage homePage;
    private ProviderPage providerPage;

    @Before
    public void setUp() {
        headerPage = new HeaderPage(driver);
        homePage = new HomePage(driver);
        providerPage = new ProviderPage(driver);
        login();
    }

    @After
    public void tearDown() {
        providerPage.clickOnHomeLink();
        headerPage.logOut();
    }

    @Test
    public void addProviderTest() {
        homePage.goToAdministration();
        providerPage.manageProviders();
        providerPage.addProvider();
        providerPage.saveProvider();
        providerPage.fillInIdentifier("super_nurse");
        providerPage.fillInPerson("Super Nurse");
        //driver.findElement(By.cssSelector("#ui-active-menuitem > span.autocompleteresult > span.hit")).click();
        providerPage.saveProvider();
    }
}
