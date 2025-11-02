package com.pharmacy.automation.base;

import com.pharmacy.automation.utils.ElementHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Base Page class with common page methods
 * All page objects should extend this class
 *
 * @author Delta Pharmacy Automation Team
 */
public class BasePage {

    protected WebDriver driver;
    protected ElementHelper elementHelper;
    protected static final Logger logger = LogManager.getLogger(BasePage.class);

    public BasePage() {
        this.driver = DriverFactory.getDriver();
        this.elementHelper = new ElementHelper();
    }

    /**
     * Navigate to URL
     */
    protected void navigateTo(String url) {
        logger.info("Navigating to: {}", url);
        driver.get(url);
        elementHelper.waitForPageLoad();
    }

    /**
     * Get current page URL
     */
    protected String getCurrentUrl() {
        String url = driver.getCurrentUrl();
        logger.debug("Current URL: {}", url);
        return url;
    }

    /**
     * Get page title
     */
    protected String getPageTitle() {
        String title = driver.getTitle();
        logger.debug("Page title: {}", title);
        return title;
    }

    /**
     * Refresh current page
     */
    protected void refreshPage() {
        logger.info("Refreshing page");
        driver.navigate().refresh();
        elementHelper.waitForPageLoad();
    }

    /**
     * Navigate back
     */
    protected void goBack() {
        logger.info("Navigating back");
        driver.navigate().back();
        elementHelper.waitForPageLoad();
    }

    /**
     * Navigate forward
     */
    protected void goForward() {
        logger.info("Navigating forward");
        driver.navigate().forward();
        elementHelper.waitForPageLoad();
    }

    /**
     * Check if element is displayed
     */
    protected boolean isElementDisplayed(By locator) {
        return elementHelper.isElementDisplayed(locator);
    }

    /**
     * Wait for element and click
     */
    protected void click(By locator) {
        elementHelper.click(locator);
    }

    /**
     * Wait for element and send keys
     */
    protected void sendKeys(By locator, String text) {
        elementHelper.sendKeys(locator, text);
    }

    /**
     * Get element text
     */
    protected String getText(By locator) {
        return elementHelper.getText(locator);
    }

    /**
     * Clear browser data
     */
    protected void clearBrowserData() {
        logger.info("Clearing browser data");
        elementHelper.clearBrowserData();
    }
}