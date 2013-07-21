package org.openmrs.reference;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.SystemUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openmrs.reference.page.GenericPage;
import org.openmrs.reference.page.Page;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestBase {
    protected static WebDriver driver;

    @BeforeClass
    public static void startWebDriver() {
        driver = setupFirefoxDriver(); //setupChromeDriver(); // setupFirefoxDriver();
        currentPage().gotoPage("/login.htm");
    }

    @AfterClass
    public static void stopWebDriver() {
        driver.quit();
    }
    
    static WebDriver setupFirefoxDriver() {
    	driver = new FirefoxDriver();
    	return driver;
    }

    static WebDriver setupChromeDriver() {
        URL resource = null;
        ClassLoader classLoader = TestBase.class.getClassLoader();

        if(SystemUtils.IS_OS_MAC_OSX) {
            resource = classLoader.getResource("chromedriver/mac/chromedriver");
        } else if(SystemUtils.IS_OS_LINUX) {
            resource = classLoader.getResource("chromedriver/linux/chromedriver");
        } else if(SystemUtils.IS_OS_WINDOWS) {
            resource = classLoader.getResource("chromedriver/windows/chromedriver.exe");
        }
        System.setProperty("webdriver.chrome.driver", resource.getPath());
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(3000, TimeUnit.MILLISECONDS);
        return driver;
    }

    /**
     * Return a Page that represents the current page, so that all the convenient
     * methods in Page can be used.
     * 
     * @return a Page
     */
    public static Page currentPage() {
    	return new GenericPage(driver);
    }

    /**
     * Assert we're on the expected page. For now, it just checks the <title> tag.
     * 
     * @param expected page
     */
	public void assertPage(Page expected) {
	    assertEquals(expected.expectedUrlPath(), currentPage().urlPath());
    }


}
