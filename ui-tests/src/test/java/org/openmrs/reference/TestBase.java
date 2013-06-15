package org.openmrs.reference;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.SystemUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openmrs.reference.page.AbstractBasePage;
import org.openmrs.reference.page.GenericPage;
import org.openmrs.reference.page.TestProperties;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestBase {
    protected static ChromeDriver driver;

    @BeforeClass
    public static void startWebDriver() {
        setupChromeDriver();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(3000, TimeUnit.MILLISECONDS);
        driver.get(new TestProperties().getWebAppUrl());
    }

    @AfterClass
    public static void stopWebDriver() {
        driver.quit();
    }
    private static void setupChromeDriver() {
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
    }

    /**
     * Return a Page that represents the current page, so that all the convenient
     * methods in AbstractBasePage can be used.
     * 
     * @return a Page
     */
    public GenericPage currentPage() {
    	return new GenericPage(driver);
    }

    /**
     * Assert we're on the expected page. For now, it just checks the <title> tag.
     * 
     * @param expected page
     */
	public void assertPage(AbstractBasePage expected) {
	    assertEquals(expected.expectedTitle(), currentPage().title());
    }

}
