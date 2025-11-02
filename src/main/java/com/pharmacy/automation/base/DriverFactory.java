package com.pharmacy.automation.base;

import com.pharmacy.automation.config.ConfigReader;
import com.pharmacy.automation.constants.FrameworkConstants;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumNetworkConditions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Driver Factory for creating and managing WebDriver instances
 * Supports Chrome, Firefox, Edge with various configurations
 * Implements ThreadLocal for parallel execution
 *
 * @author Delta Pharmacy Automation Team
 */
public class DriverFactory {

    private static final Logger logger = LogManager.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private DriverFactory() {
        // Private constructor
    }

    /**
     * Initialize WebDriver based on browser type
     * @param browser Browser name (chrome/firefox/edge)
     * @return WebDriver instance
     */
    public static WebDriver initializeDriver(String browser) {
        logger.info("Initializing WebDriver for browser: {}", browser);

        WebDriver webDriver;

        if (FrameworkConstants.USE_SELENIUM_GRID) {
            webDriver = initializeRemoteDriver(browser);
        } else {
            webDriver = initializeLocalDriver(browser);
        }

        setupDriver(webDriver);
        driver.set(webDriver);

        logger.info("WebDriver initialized successfully for browser: {}", browser);
        return webDriver;
    }

    /**
     * Initialize local WebDriver
     */
    private static WebDriver initializeLocalDriver(String browser) {
        WebDriver webDriver;

        switch (browser.toLowerCase()) {
            case FrameworkConstants.CHROME:
                WebDriverManager.chromedriver().setup();
                webDriver = new ChromeDriver(getChromeOptions());
                break;

            case FrameworkConstants.FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                webDriver = new FirefoxDriver(getFirefoxOptions());
                break;

            case FrameworkConstants.EDGE:
                WebDriverManager.edgedriver().setup();
                webDriver = new EdgeDriver(getEdgeOptions());
                break;

            default:
                logger.error("Invalid browser specified: {}. Defaulting to Chrome", browser);
                WebDriverManager.chromedriver().setup();
                webDriver = new ChromeDriver(getChromeOptions());
        }

        return webDriver;
    }

    /**
     * Initialize Remote WebDriver for Selenium Grid
     */
    private static WebDriver initializeRemoteDriver(String browser) {
        WebDriver webDriver;

        try {
            URL gridUrl = new URL(FrameworkConstants.SELENIUM_GRID_URL);

            switch (browser.toLowerCase()) {
                case FrameworkConstants.CHROME:
                    webDriver = new RemoteWebDriver(gridUrl, getChromeOptions());
                    break;

                case FrameworkConstants.FIREFOX:
                    webDriver = new RemoteWebDriver(gridUrl, getFirefoxOptions());
                    break;

                case FrameworkConstants.EDGE:
                    webDriver = new RemoteWebDriver(gridUrl, getEdgeOptions());
                    break;

                default:
                    logger.error("Invalid browser for Grid: {}", browser);
                    webDriver = new RemoteWebDriver(gridUrl, getChromeOptions());
            }
        } catch (MalformedURLException e) {
            logger.error("Invalid Selenium Grid URL", e);
            throw new RuntimeException("Failed to initialize Remote WebDriver", e);
        }

        return webDriver;
    }

    /**
     * Get Chrome Options
     */
    private static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();

        // Performance settings
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.setAcceptInsecureCerts(true);

        // Arguments
        if (ConfigReader.isHeadless()) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--start-maximized");
        options.addArguments("--remote-allow-origins=*");

        // Preferences
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.default_directory", FrameworkConstants.PROJECT_PATH + "/downloads");
        options.setExperimentalOption("prefs", prefs);

        // Exclude automation switches
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);

        logger.debug("Chrome options configured successfully");
        return options;
    }

    /**
     * Get Firefox Options
     */
    private static FirefoxOptions getFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();

        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.setAcceptInsecureCerts(true);

        if (ConfigReader.isHeadless()) {
            options.addArguments("--headless");
        }
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");

        // Preferences
        options.addPreference("dom.webnotifications.enabled", false);
        options.addPreference("browser.download.folderList", 2);
        options.addPreference("browser.download.dir", FrameworkConstants.PROJECT_PATH + "/downloads");
        options.addPreference("browser.helperApps.neverAsk.saveToDisk", "application/pdf");

        logger.debug("Firefox options configured successfully");
        return options;
    }

    /**
     * Get Edge Options
     */
    private static EdgeOptions getEdgeOptions() {
        EdgeOptions options = new EdgeOptions();

        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.setAcceptInsecureCerts(true);

        if (ConfigReader.isHeadless()) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        options.addArguments("--remote-allow-origins=*");

        // Preferences
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        logger.debug("Edge options configured successfully");
        return options;
    }

    /**
     * Setup WebDriver with common configurations
     */
    private static void setupDriver(WebDriver webDriver) {
        // Set timeouts
        webDriver.manage().timeouts().implicitlyWait(FrameworkConstants.IMPLICIT_WAIT);
        webDriver.manage().timeouts().pageLoadTimeout(FrameworkConstants.PAGE_LOAD_TIMEOUT);
        webDriver.manage().timeouts().scriptTimeout(FrameworkConstants.SCRIPT_TIMEOUT);

        // Maximize window
        if (FrameworkConstants.MAXIMIZE_WINDOW && !ConfigReader.isHeadless()) {
            webDriver.manage().window().maximize();
            logger.debug("Browser window maximized");
        }

        // Delete cookies
        if (FrameworkConstants.DELETE_COOKIES) {
            webDriver.manage().deleteAllCookies();
            logger.debug("All cookies deleted");
        }
    }

    /**
     * Set mobile view dimensions
     * @param dimensions Format: "widthxheight" (e.g., "375x667")
     */
    public static void setMobileView(String dimensions) {
        WebDriver webDriver = getDriver();
        String[] size = dimensions.split("x");
        int width = Integer.parseInt(size[0]);
        int height = Integer.parseInt(size[1]);

        webDriver.manage().window().setSize(new org.openqa.selenium.Dimension(width, height));
        logger.info("Mobile view set to: {}x{}", width, height);
    }

    /**
     * Set network conditions (Selenium 4 - Chrome only)
     * @param condition Network condition preset
     */
    public static void setNetworkConditions(String condition) {
        WebDriver webDriver = getDriver();

        if (webDriver instanceof ChromeDriver) {
            ChromeDriver chromeDriver = (ChromeDriver) webDriver;
            ChromiumNetworkConditions networkConditions = new ChromiumNetworkConditions();

            switch (condition.toLowerCase()) {
                case FrameworkConstants.NETWORK_OFFLINE:
                    networkConditions.setOffline(true);
                    break;

                case FrameworkConstants.NETWORK_SLOW_2G:
                    networkConditions.setDownloadThroughput(50 * 1024); // 50 Kbps
                    networkConditions.setUploadThroughput(20 * 1024);   // 20 Kbps
                    networkConditions.setLatency(2000);                  // 2000ms
                    break;

                case FrameworkConstants.NETWORK_2G:
                    networkConditions.setDownloadThroughput(250 * 1024);
                    networkConditions.setUploadThroughput(50 * 1024);
                    networkConditions.setLatency(300);
                    break;

                case FrameworkConstants.NETWORK_3G:
                    networkConditions.setDownloadThroughput(750 * 1024);
                    networkConditions.setUploadThroughput(250 * 1024);
                    networkConditions.setLatency(100);
                    break;

                case FrameworkConstants.NETWORK_4G:
                    networkConditions.setDownloadThroughput(4 * 1024 * 1024);
                    networkConditions.setUploadThroughput(3 * 1024 * 1024);
                    networkConditions.setLatency(20);
                    break;

                default:
                    networkConditions.setOffline(false);
                    logger.info("Network conditions reset to online");
                    break;
            }

            chromeDriver.setNetworkConditions(networkConditions);
            logger.info("Network conditions set to: {}", condition);
        } else {
            logger.warn("Network conditions only supported on Chrome");
        }
    }

    /**
     * Get current WebDriver instance
     */
    public static WebDriver getDriver() {
        return driver.get();
    }

    /**
     * Quit WebDriver and clean up
     */
    public static void quitDriver() {
        if (driver.get() != null) {
            try {
                driver.get().quit();
                logger.info("WebDriver quit successfully");
            } catch (Exception e) {
                logger.error("Error quitting WebDriver", e);
            } finally {
                driver.remove();
            }
        }
    }

    /**
     * Clear browser cache and storage
     */
    public static void clearBrowserData() {
        WebDriver webDriver = getDriver();

        // Clear cookies
        webDriver.manage().deleteAllCookies();
        logger.debug("Cookies cleared");

        // Clear local storage and session storage via JavaScript
        ((org.openqa.selenium.JavascriptExecutor) webDriver).executeScript("window.localStorage.clear();");
        ((org.openqa.selenium.JavascriptExecutor) webDriver).executeScript("window.sessionStorage.clear();");
        logger.debug("Local storage and session storage cleared");
    }
}