package org.openmrs.reference;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.page.AdministrationPage;
import org.openmrs.reference.page.ModulesPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CheckModulesTest extends ReferenceApplicationTestBase {

    /**
     * Check the list of modules to be sure they are all started.
     */
    @Test
    @Ignore("Waiting for https://github.com/openmrs/openmrs-module-legacyui/pull/62 to be merged")
    @Category(org.openmrs.reference.groups.BuildTests.class)
    public void checkModules() throws Exception {
        AdministrationPage administrationPage = homePage.goToAdministration();
        ModulesPage modulesPage = administrationPage.goToManageModulesPage();
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
