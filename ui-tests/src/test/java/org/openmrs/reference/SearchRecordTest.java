package org.openmrs.reference;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.ActiveVisitsPage;
import org.openmrs.reference.page.FindPatientRecordPage;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.uitestframework.test.TestBase;

public class SearchRecordTest extends TestBase {
	private HeaderPage headerPage;
    private HomePage homePage;
    private FindPatientRecordPage findPatientRecordPage;
    private ActiveVisitsPage activeVisitsPage;
	private Random rand;

    @Before
    public void setUp() {
    	headerPage = new HeaderPage(driver);
        homePage = new HomePage(driver);
        findPatientRecordPage = new FindPatientRecordPage(driver);
        activeVisitsPage = new ActiveVisitsPage(driver);
        rand = new Random();
    }
    
    @Test
    public void testFindPatientName() throws Exception {
    	login();
    	assertPage(homePage);
    	
    	currentPage().gotoPage("/coreapps/findpatient/findPatient.page?app=coreapps.findPatient");
    	assertPage(findPatientRecordPage);
    	
    	// Takes all the names
    	List<String> names = findPatientRecordPage.getStringsFromColumn(1);
    	for(int i = 0; i < names.size(); i++) {
    		// Should cut the full name before the first name
    		names.set(i, names.get(i).split(" ")[0]);
    	}
    	
    	for(int i = 0; i < 5; i++) {
    		// Must enter a random name and check the table (column 1)
	    	String template = names.get(rand.nextInt(names.size()));
	    	findPatientRecordPage.search(template);
	    	assertTrue(findPatientRecordPage.verifyTable(1, template));
    	}
    	
    	homePage.go();
    	assertPage(homePage);
    	
    	headerPage.logOut();
    	assertPage(loginPage);
    }
    
    @Test
    public void testFindPatientIdentifier() throws Exception {
    	login();
    	assertPage(homePage);
    	
    	currentPage().gotoPage("/coreapps/findpatient/findPatient.page?app=coreapps.findPatient");
    	assertPage(findPatientRecordPage);
    	
    	// Takes all the identifiers
    	List<String> ids = findPatientRecordPage.getStringsFromColumn(0);
    	for(int i = 0; i < ids.size(); i++) {
    		// Should cut the green "Recent" tag (e.g. "100R9J Recent" -> "100R9J")
    		ids.set(i, ids.get(i).split(" ")[0]);
    	}
    	
    	for(int i = 0; i < 5; i++) {
    		// Must enter a random id and check the table (column 0)
	    	String template = ids.get(rand.nextInt(ids.size()));
	    	findPatientRecordPage.search(template);
	    	assertTrue(findPatientRecordPage.verifyTable(0, template));
    	}
    	
    	homePage.go();
    	assertPage(homePage);
    	
    	headerPage.logOut();
    	assertPage(loginPage);
    }
    
    @Test
    public void testActiveVisitsName() throws Exception {
    	login();
    	assertPage(homePage);
    	
    	currentPage().gotoPage("/coreapps/activeVisits.page?app=coreapps.activeVisits");
    	assertPage(activeVisitsPage);
    	
    	// Takes all the names
    	List<String> names = activeVisitsPage.getStringsFromColumn(1);
    	for(int i = 0; i < names.size(); i++) {
    		// Should cut the full name before the first name
    		names.set(i, names.get(i).split(" ")[0]);
    	}
    	
    	for(int i = 0; i < 5; i++) {
    		// Must enter a random name and check the table (column 1)
	    	String template = names.get(rand.nextInt(names.size()));
	    	activeVisitsPage.search(template);
	    	assertTrue(activeVisitsPage.verifyTable(1, template));
    	}
    	
    	homePage.go();
    	assertPage(homePage);
    	
    	headerPage.logOut();
    	assertPage(loginPage);
    }
    
    @Test
    public void testActiveVisitsIdentifier() throws Exception {
    	login();
    	assertPage(homePage);
    	
    	currentPage().gotoPage("/coreapps/activeVisits.page?app=coreapps.activeVisits");
    	assertPage(activeVisitsPage);
    	
    	// Takes all the identifiers
    	List<String> ids = activeVisitsPage.getStringsFromColumn(0);
    	for(int i = 0; i < ids.size(); i++) {
    		// Should cut the "OpenMRS ID" tag (e.g. "OpenMRS ID: 100R9J" -> "100R9J")
    		ids.set(i, ids.get(i).split(" ")[2]);
    	}
    	
    	for(int i = 0; i < 5; i++) {
    		// Must enter a random id and check the table (column 0)
	    	String template = ids.get(rand.nextInt(ids.size()));
	    	activeVisitsPage.search(template);
	    	assertTrue(activeVisitsPage.verifyTable(0, template));
    	}
    	
    	homePage.go();
    	assertPage(homePage);
    	
    	headerPage.logOut();
    	assertPage(loginPage);
    }
    
    @Test
    public void testCaptureVitalsName() throws Exception {
    	// This page very similar to the "Find Patient" page (differs by link after '?' symbol)
    	// so we can use FindPatientRecordPage
    	login();
    	assertPage(homePage);
    	
    	currentPage().gotoPage("/coreapps/findpatient/findPatient.page?app=referenceapplication.vitals");
    	assertPage(findPatientRecordPage);
    	
    	// Takes all the names
    	List<String> names = findPatientRecordPage.getStringsFromColumn(1);
    	for(int i = 0; i < names.size(); i++) {
    		// Should cut the full name before the first name
    		names.set(i, names.get(i).split(" ")[0]);
    	}
    	
    	for(int i = 0; i < 5; i++) {
    		// Must enter a random name and check the table (column 1)
	    	String template = names.get(rand.nextInt(names.size()));
	    	findPatientRecordPage.search(template);
	    	assertTrue(findPatientRecordPage.verifyTable(1, template));
    	}
    	
    	homePage.go();
    	assertPage(homePage);
    	
    	headerPage.logOut();
    	assertPage(loginPage);
    }
    
    @Test
    public void testCaptureVitalsIdentifier() throws Exception {
    	// This page very similar to the "Find Patient" page (differs by link after '?' symbol)
    	// so we can use FindPatientRecordPage
    	login();
    	assertPage(homePage);
    	
    	currentPage().gotoPage("/coreapps/findpatient/findPatient.page?app=referenceapplication.vitals");
    	assertPage(findPatientRecordPage);
    	
    	// Takes all the identifiers
    	List<String> ids = findPatientRecordPage.getStringsFromColumn(0);
    	for(int i = 0; i < ids.size(); i++) {
    		// Should cut the green "Recent" tag (e.g. "100R9J Recent" -> "100R9J")
    		ids.set(i, ids.get(i).split(" ")[0]);
    	}
    	
    	for(int i = 0; i < 5; i++) {
    		// Must enter a random id and check the table (column 0)
	    	String template = ids.get(rand.nextInt(ids.size()));
	    	findPatientRecordPage.search(template);
	    	assertTrue(findPatientRecordPage.verifyTable(0, template));
    	}
    	
    	homePage.go();
    	assertPage(homePage);
    	
    	headerPage.logOut();
    	assertPage(loginPage);
    }
}
