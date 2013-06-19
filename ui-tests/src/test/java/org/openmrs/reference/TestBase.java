package org.openmrs.reference;

import org.apache.commons.lang3.SystemUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openmrs.reference.page.AbstractBasePage;
import org.openqa.selenium.chrome.ChromeDriver;

import java.net.URL;
import java.util.concurrent.TimeUnit;

public class TestBase {
    protected static ChromeDriver driver;
    private static AbstractBasePage abstractBasePage;

    @BeforeClass
    public static void startWebDriver() {
        driver = setupChromeDriver();
//        driver = new ChromeDriver();
        abstractBasePage = new AbstractBasePage(driver);
//        driver.manage().timeouts().implicitlyWait(3000, TimeUnit.MILLISECONDS);
        abstractBasePage.gotoPage("/login.htm");
    }

    @AfterClass
    public static void stopWebDriver() {
        driver.quit();
    }



    private static ChromeDriver setupChromeDriver() {
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
}
