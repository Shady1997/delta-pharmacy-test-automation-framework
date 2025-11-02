package com.pharmacy.automation.stepdefinitions;

import com.pharmacy.automation.base.DriverFactory;
import com.pharmacy.automation.config.ConfigReader;
import com.pharmacy.automation.utils.ScreenshotHelper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class Hooks {
    private static final Logger logger = LogManager.getLogger(Hooks.class);
    private WebDriver driver;

    @Before
    public void setUp(Scenario scenario) {
        logger.info("========================================");
        logger.info("Starting Scenario: {}", scenario.getName());
        logger.info("========================================");

        ConfigReader.initializeConfig();
        String browser = System.getProperty("browser", "chrome");
        driver = DriverFactory.initializeDriver(browser);
        driver.get(ConfigReader.getAppUrl());
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            logger.error("Scenario Failed: {}", scenario.getName());
            byte[] screenshot = ScreenshotHelper.captureScreenshotAsBytes(driver);
            scenario.attach(screenshot, "image/png", "failure_screenshot");
        }

        DriverFactory.quitDriver();
        logger.info("Scenario Completed: {} - Status: {}",
                scenario.getName(), scenario.getStatus());
    }
}
