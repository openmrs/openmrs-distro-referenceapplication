package org.openmrs.reference.page;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ActiveVisitsPage extends SearchPage {

	static final By SEARCH = By.id("active-visits_filter");
	static final By TABLE = By.id("active-visits");
	
	public ActiveVisitsPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	public void search(String template) {
		WebElement searchInput = driver.findElement(SEARCH).findElement(By.xpath("id('active-visits_filter')/label/input"));
		searchInput.clear();
		searchInput.sendKeys(template);
	}
	
	@Override
	public List<String> getStringsFromColumn(int column) {
		// Takes all the strings of this column (e.g. 0 - Identifier, 1 - Name)
		List<String> list = new ArrayList<String>();
		WebElement tableElement = driver.findElement(TABLE);
		List<WebElement> tr_collection = tableElement.findElements(By.xpath("id('active-visits')/tbody/tr"));
		
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
		List<WebElement> tr_collection = tableElement.findElements(By.xpath("id('active-visits')/tbody/tr"));
		
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
		return URL_ROOT + "/coreapps/activeVisits.page";
	}

}
