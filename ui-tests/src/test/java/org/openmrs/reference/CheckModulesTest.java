package org.openmrs.reference;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.page.AdministrationPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.ModulesPage;
import org.openmrs.reference.page.SystemAdministrationPage;
import org.openmrs.uitestframework.test.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CheckModulesTest extends ReferenceApplicationTestBase {

    /**
     * Check the list of modules to be sure they are all started.
     */
    @Test
    @Category(org.openmrs.reference.groups.BuildTests.class)
    public void checkModules() throws Exception {
        AdministrationPage administrationPage = homePage.goToAdministration();
        ModulesPage modulesPage = administrationPage.goToManageModulesPage();
        Thread.sleep(500);
        assertPage(modulesPage);
        // Get the modulesListing <div>, which contains the table of modules.
        WebElement moduleListing = modulesPage.findElementById("moduleListing");
        // Grab all the <input> elements from the first column of the table.
        List<WebElement> firstColumn = moduleListing.findElements(By.xpath("table/tbody/tr/td[1]/input"));
        for (WebElement eachModule : firstColumn) {
        	// The name attr on the <input> elements should all be "stop" which indicates the module is correctly started.
        	// If not, then grab the text from the 3rd column to show which module is not started.
	        Assert.assertEquals("module not ready: " + eachModule.findElement(By.xpath("../../td[3]")).getText(), "stop", eachModule.getAttribute("name"));
        }
    }
}
