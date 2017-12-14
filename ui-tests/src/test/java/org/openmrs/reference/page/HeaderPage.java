package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HeaderPage extends Page {

	public HeaderPage(WebDriver driver) {
		super(driver);
	}

	static final String HOME_ICON = "logo";
	static final String HOME_LINK_TEXT = "Home";

	public void clickOnHomeIcon() throws InterruptedException {
		clickOn(By.className(HOME_ICON));
	}


	// TODO This is unused, do we really need it?
	public void clickOnHomeLink() {
		clickOn(By.linkText(HOME_LINK_TEXT));
	}

	public void logOut() throws InterruptedException{
		clickOnLinkFromHref(newContextPageUrl("/logout"));
	}

	@Override
	public String getPageUrl() {
		return null;
	}

}
