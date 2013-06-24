package org.openmrs.reference.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * A web page.
 */
public interface Page {

	void gotoPage(String address);

    // TODO Should this be named findElement instead?
	WebElement getElement(By by);
	
	// TODO Should this be named findElementById instead?
	WebElement getElementById(String id);
	
	String getText(By by);
	
	void setTextToField(By by, String text);
	
	void setTextToFieldInsideSpan(String spanId, String text);
	
	void clickOn(By by);
	
	void hoverOn(By by);

    void selectFromCombo(By by, String value);

	/**
	 * Return the actual title of the page.
	 * 
	 * @return the page title.
	 */
	String title();

	/**
	 * Return the path portion of the page's current URL.
	 * 
	 * @return the path portion of the URL.
	 */
	String urlPath();
	
	/**
	 * Real pages supply their expected URL path.
	 * 
	 * @return The path portion of the url of the page.
	 */
	String expectedUrlPath();

}