package org.openmrs.reference.page;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * A web page.
 */
public interface Page {

	void gotoPage(String address);

	WebElement findElement(By by);
	
	WebElement findElementById(String id);
	
	String getText(By by);
	
	void setTextToField(By by, String text);
	
	void setTextToFieldInsideSpan(String spanId, String text);
	
	void clickOn(By by);
	
	void hoverOn(By by);

    void selectFrom(By by, String value);

    List<WebElement> findElements(By by);

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