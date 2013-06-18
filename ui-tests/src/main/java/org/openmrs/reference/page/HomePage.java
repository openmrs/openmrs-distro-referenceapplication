package org.openmrs.reference.page;

import org.openqa.selenium.WebDriver;


public class HomePage extends AbstractBasePage {

	public HomePage(WebDriver driver) {
	    super(driver);
    }
	
    @Override
    public String expectedTitle() {
    	return "Home";
    }

}
