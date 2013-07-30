package org.openmrs.reference;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openmrs.reference.helper.TestProperties;
import org.openmrs.reference.page.GenericPage;
import org.openmrs.reference.page.LoginPage;
import org.openmrs.reference.page.Page;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestBase {
	
	public enum WebDriverType {chrome, firefox};	// only these two for now
	
    protected static WebDriver driver;

    @BeforeClass
    public static void startWebDriver() {
        final TestProperties properties = TestProperties.instance();
        final String webDriverName = properties.getWebDriver();
        final WebDriverType webDriverType = WebDriverType.valueOf(webDriverName);
        switch (webDriverType) {
			case chrome:
				driver = setupChromeDriver();
				break;
			case firefox:
				driver = setupFirefoxDriver();
				break;
			default:
				// shrug, choose chrome as default
				driver = setupChromeDriver();
				break;
		}
        goToLoginPage();
    }

    @AfterClass
    public static void stopWebDriver() {
        driver.quit();
    }
    
    public static void goToLoginPage() {
    	currentPage().gotoPage(LoginPage.LOGIN_PATH);
    }
    
    // This takes a screen (well, browser) snapshot whenever there's a failure
    // and stores it in a "screenshots" directory.
    @Rule
    public TestRule testWatcher = new TestWatcher() {
        @Override
        public void failed(Throwable t, Description test) {
            File tempFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(tempFile, new File("target/screenshots/" + test.getDisplayName() + ".png"));
            } catch (IOException e) {
            }
        }
    };
    
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
        driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
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
