package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HeaderPage extends AbstractBasePage {
	
	public HeaderPage(WebDriver driver) {
		super(driver);
	}
	
	final String LOGOUT_HREF = URL_ROOT + "/logout";
	static final String HOME_ICON = "logo";
	static final String HOME_LINK_TEXT = "Home";

	public void clickOnHomeIcon() throws InterruptedException {
        clickWhenVisible(By.className(HOME_ICON));
	}

    public void clickWhenVisible(By by) throws InterruptedException {
        Long startTime = System.currentTimeMillis();
        while((System.currentTimeMillis() - startTime) < 5000) {
            try {
                clickOn(by);
                break;
            } catch (Exception e) {
                Thread.sleep(100);
            }
        }

    }
	
	// TODO This is unused, do we really need it?
	public void clickOnHomeLink() {
		clickOn(By.linkText(HOME_LINK_TEXT));
	}
	
	public void logOut() {
		clickOnLinkFromHref(LOGOUT_HREF);
	}
	
	@Override
	public String expectedUrlPath() {
		return null;
	}
	
}
