package com.pharmacy.automation.utils;

import com.pharmacy.automation.base.DriverFactory;
import com.pharmacy.automation.constants.FrameworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.function.Function;

public class WaitHelper {
    private static final Logger logger = LogManager.getLogger(WaitHelper.class);
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final FluentWait<WebDriver> fluentWait;

    public WaitHelper() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, FrameworkConstants.EXPLICIT_WAIT);
        this.fluentWait = new FluentWait<>(driver)
                .withTimeout(FrameworkConstants.EXPLICIT_WAIT)
                .pollingEvery(FrameworkConstants.POLLING_INTERVAL)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }

    public WebElement waitForElementVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForElementClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public boolean waitForElementInvisible(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public boolean waitForTextPresent(By locator, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public WebElement fluentWaitForElement(By locator) {
        return fluentWait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return driver.findElement(locator);
            }
        });
    }

    public void waitForPageLoad() {
        wait.until(driver -> ((JavascriptExecutor) driver)
                .executeScript("return document.readyState").equals("complete"));
    }

    public void waitForAjax() {
        wait.until(driver -> (Boolean) ((JavascriptExecutor) driver)
                .executeScript("return jQuery.active == 0"));
    }
}