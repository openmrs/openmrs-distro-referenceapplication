package org.openmrs.reference.page;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FindPatientRecordPage extends SearchPage {

	static final By SEARCH = By.id("patient-search");
	static final By TABLE = By.id("patient-search-results-table");
	
	public FindPatientRecordPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void search(String template) {
		setTextToFieldNoEnter(SEARCH, template);
	}
	
	@Override
	public List<String> getStringsFromColumn(int column) {
		// Takes all the strings of this column (e.g. 0 - Identifier, 1 - Name)
		List<String> list = new ArrayList<String>();
		WebElement tableElement = driver.findElement(TABLE);
		List<WebElement> tr_collection = tableElement.findElements(By.xpath("id('patient-search-results-table')/tbody/tr"));
		
        for(WebElement trElement : tr_collection) {
            List<WebElement> td_collection = trElement.findElements(By.xpath("td"));
            String patient = td_collection.get(column).getText();
            list.add(patient);
        }
        
        return list;
	}
	
	@Override
	public boolean verifyTable(int column, String template) throws Exception {
		Thread.sleep(1500); 	// A little hack - we have to wait until the table is fully loaded
		WebElement tableElement = driver.findElement(TABLE);
		List<WebElement> tr_collection = tableElement.findElements(By.xpath("id('patient-search-results-table')/tbody/tr"));
		
        for(WebElement trElement : tr_collection) {
            List<WebElement> td_collection = trElement.findElements(By.xpath("td"));
            String patient = td_collection.get(column).getText();
            
            if(patient.indexOf(template) == -1) {
            	return false;
            }
        }
        
        return true;
	}
	
	@Override
	public String expectedUrlPath() {
		return URL_ROOT + "/coreapps/findpatient/findPatient.page";
	}

}
