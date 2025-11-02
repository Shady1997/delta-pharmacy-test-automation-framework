package com.pharmacy.automation.base;

import com.pharmacy.automation.config.ConfigReader;
import com.pharmacy.automation.constants.FrameworkConstants;
import com.pharmacy.automation.utils.ScreenshotHelper;
import com.pharmacy.automation.utils.VideoRecorder;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.FileInputStream;

/**
 * Base Test class for all test classes
 * Handles setup, teardown, and common test operations
 *
 * @author Delta Pharmacy Automation Team
 */
public class BaseTest {

    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected WebDriver driver;
    protected VideoRecorder videoRecorder;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        logger.info("========================================");
        logger.info("Test Suite Starting");
        logger.info("========================================");
        ConfigReader.initializeConfig();
        logger.info("Configuration loaded successfully");
    }

    @BeforeClass(alwaysRun = true)
    @Parameters({"browser", "env"})
    public void setUp(@Optional("chrome") String browser, @Optional("testing") String env) {
        logger.info("========================================");
        logger.info("Setting up test class: {}", this.getClass().getSimpleName());
        logger.info("========================================");
        logger.info("Browser: {}", browser);
        logger.info("Environment: {}", env);

        // Set environment property if provided
        if (env != null && !env.isEmpty()) {
            System.setProperty("env", env);
            ConfigReader.reloadConfig();
        }

        // Initialize driver
        driver = DriverFactory.initializeDriver(browser);

        // Navigate to application URL
        String appUrl = ConfigReader.getAppUrl();
        logger.info("Navigating to: {}", appUrl);
        driver.get(appUrl);

        // Initialize video recorder if enabled
        if (FrameworkConstants.ENABLE_VIDEO_RECORDING) {
            videoRecorder = new VideoRecorder();
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.info("========================================");
        logger.info("Starting Test: {}", testName);
        logger.info("========================================");

        // Start video recording
        if (videoRecorder != null) {
            try {
                videoRecorder.startRecording(testName);
                logger.info("Video recording started for test: {}", testName);
            } catch (Exception e) {
                logger.error("Failed to start video recording", e);
            }
        }

        // Clear browser data before each test
        if (FrameworkConstants.CLEAR_CACHE) {
            DriverFactory.clearBrowserData();
            logger.debug("Browser data cleared");
        }
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String status = getTestStatus(result);

        logger.info("Test '{}' finished with status: {}", testName, status);

        // Take screenshot on failure or if configured
        if (result.getStatus() == ITestResult.FAILURE && FrameworkConstants.TAKE_SCREENSHOT_ON_FAIL) {
            captureScreenshot(testName);
        } else if (result.getStatus() == ITestResult.SUCCESS && FrameworkConstants.TAKE_SCREENSHOT_ON_PASS) {
            captureScreenshot(testName);
        }

        // Stop video recording
        if (videoRecorder != null) {
            try {
                videoRecorder.stopRecording(result.getStatus() == ITestResult.SUCCESS);
                logger.info("Video recording stopped for test: {}", testName);
            } catch (Exception e) {
                logger.error("Failed to stop video recording", e);
            }
        }

        // Log test result details
        if (result.getStatus() == ITestResult.FAILURE) {
            logger.error("Test Failed: {}", testName);
            logger.error("Failure Reason: {}", result.getThrowable().getMessage());
        } else if (result.getStatus() == ITestResult.SKIP) {
            logger.warn("Test Skipped: {}", testName);
        } else {
            logger.info("Test Passed: {}", testName);
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        logger.info("========================================");
        logger.info("Tearing down test class: {}", this.getClass().getSimpleName());
        logger.info("========================================");

        if (driver != null) {
            DriverFactory.quitDriver();
            logger.info("WebDriver quit successfully");
        }
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        logger.info("========================================");
        logger.info("Test Suite Completed");
        logger.info("========================================");
    }

    /**
     * Capture screenshot and attach to report
     */
    protected void captureScreenshot(String testName) {
        try {
            String screenshotPath = ScreenshotHelper.captureScreenshot(driver, testName);
            logger.info("Screenshot captured: {}", screenshotPath);

            // Attach to Allure report
            try (FileInputStream fis = new FileInputStream(screenshotPath)) {
                Allure.addAttachment("Screenshot - " + testName, "image/png", fis, ".png");
            }
        } catch (Exception e) {
            logger.error("Failed to capture screenshot", e);
        }
    }

    /**
     * Get test status as string
     */
    private String getTestStatus(ITestResult result) {
        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                return "PASSED";
            case ITestResult.FAILURE:
                return "FAILED";
            case ITestResult.SKIP:
                return "SKIPPED";
            default:
                return "UNKNOWN";
        }
    }

    /**
     * Get current WebDriver instance
     */
    protected WebDriver getDriver() {
        return driver;
    }
}