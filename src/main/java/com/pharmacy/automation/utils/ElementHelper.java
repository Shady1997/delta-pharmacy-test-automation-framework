package com.pharmacy.automation.utils;

import com.pharmacy.automation.base.DriverFactory;
import com.pharmacy.automation.constants.FrameworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Element Helper with retry mechanism and professional wait strategies
 * Attempts to relocate elements up to 3 times before throwing exceptions
 *
 * @author Delta Pharmacy Automation Team
 */
public class ElementHelper {

    private static final Logger logger = LogManager.getLogger(ElementHelper.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    public ElementHelper() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, FrameworkConstants.EXPLICIT_WAIT);
    }

    /**
     * Find element with retry mechanism (up to 3 attempts)
     * @param locator Element locator
     * @return WebElement
     */
    public WebElement findElementWithRetry(By locator) {
        WebElement element = null;
        int attempts = 0;

        while (attempts < FrameworkConstants.ELEMENT_RETRY_COUNT) {
            try {
                element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
                logger.debug("Element found on attempt {}: {}", attempts + 1, locator);
                return element;
            } catch (StaleElementReferenceException | NoSuchElementException e) {
                attempts++;
                logger.warn("Attempt {} to find element failed: {}. Retrying...", attempts, locator);

                if (attempts >= FrameworkConstants.ELEMENT_RETRY_COUNT) {
                    String errorMsg = String.format("Failed to locate element after %d attempts: %s",
                            FrameworkConstants.ELEMENT_RETRY_COUNT, locator);
                    logger.error(errorMsg);
                    throw new NoSuchElementException(errorMsg, e);
                }

                waitForMilliseconds(FrameworkConstants.RETRY_INTERVAL_MS);
            } catch (TimeoutException e) {
                String errorMsg = String.format("Element not found within timeout: %s", locator);
                logger.error(errorMsg);
                throw new TimeoutException(errorMsg, e);
            }
        }

        return element;
    }

    /**
     * Click element with retry mechanism
     */
    public void click(By locator) {
        int attempts = 0;
        boolean clicked = false;

        while (attempts < FrameworkConstants.ELEMENT_RETRY_COUNT && !clicked) {
            try {
                WebElement element = waitForElementToBeClickable(locator);
                element.click();
                logger.info("Successfully clicked element: {}", locator);
                clicked = true;
            } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
                attempts++;
                logger.warn("Click attempt {} failed for element: {}. Retrying...", attempts, locator);

                if (attempts >= FrameworkConstants.ELEMENT_RETRY_COUNT) {
                    logger.error("Failed to click element after {} attempts: {}",
                            FrameworkConstants.ELEMENT_RETRY_COUNT, locator);
                    // Try JavaScript click as last resort
                    clickWithJavaScript(locator);
                    clicked = true;
                }

                waitForMilliseconds(FrameworkConstants.RETRY_INTERVAL_MS);
            }
        }
    }

    /**
     * Click using JavaScript (fallback method)
     */
    public void clickWithJavaScript(By locator) {
        try {
            WebElement element = findElementWithRetry(locator);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);
            logger.info("Clicked element using JavaScript: {}", locator);
        } catch (Exception e) {
            String errorMsg = String.format("Failed to click element with JavaScript: %s", locator);
            logger.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    /**
     * Send keys with retry mechanism
     */
    public void sendKeys(By locator, String text) {
        int attempts = 0;
        boolean sent = false;

        while (attempts < FrameworkConstants.ELEMENT_RETRY_COUNT && !sent) {
            try {
                WebElement element = waitForElementToBeVisible(locator);
                element.clear();
                element.sendKeys(text);
                logger.info("Successfully sent keys to element: {}", locator);
                sent = true;
            } catch (StaleElementReferenceException | InvalidElementStateException e) {
                attempts++;
                logger.warn("SendKeys attempt {} failed for element: {}. Retrying...", attempts, locator);

                if (attempts >= FrameworkConstants.ELEMENT_RETRY_COUNT) {
                    String errorMsg = String.format("Failed to send keys after %d attempts: %s",
                            FrameworkConstants.ELEMENT_RETRY_COUNT, locator);
                    logger.error(errorMsg);
                    throw new RuntimeException(errorMsg, e);
                }

                waitForMilliseconds(FrameworkConstants.RETRY_INTERVAL_MS);
            }
        }
    }

    /**
     * Get text with retry mechanism
     */
    public String getText(By locator) {
        int attempts = 0;
        String text = null;

        while (attempts < FrameworkConstants.ELEMENT_RETRY_COUNT) {
            try {
                WebElement element = waitForElementToBeVisible(locator);
                text = element.getText();
                logger.debug("Retrieved text from element: {}", locator);
                return text;
            } catch (StaleElementReferenceException e) {
                attempts++;
                logger.warn("GetText attempt {} failed for element: {}. Retrying...", attempts, locator);

                if (attempts >= FrameworkConstants.ELEMENT_RETRY_COUNT) {
                    String errorMsg = String.format("Failed to get text after %d attempts: %s",
                            FrameworkConstants.ELEMENT_RETRY_COUNT, locator);
                    logger.error(errorMsg);
                    throw new RuntimeException(errorMsg, e);
                }

                waitForMilliseconds(FrameworkConstants.RETRY_INTERVAL_MS);
            }
        }

        return text;
    }

    /**
     * Wait for element to be clickable
     */
    public WebElement waitForElementToBeClickable(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            logger.debug("Element is clickable: {}", locator);
            return element;
        } catch (TimeoutException e) {
            String errorMsg = String.format("Element not clickable within timeout: %s", locator);
            logger.error(errorMsg);
            throw new TimeoutException(errorMsg, e);
        }
    }

    /**
     * Wait for element to be visible
     */
    public WebElement waitForElementToBeVisible(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            logger.debug("Element is visible: {}", locator);
            return element;
        } catch (TimeoutException e) {
            String errorMsg = String.format("Element not visible within timeout: %s", locator);
            logger.error(errorMsg);
            throw new TimeoutException(errorMsg, e);
        }
    }

    /**
     * Wait for element to be invisible
     */
    public boolean waitForElementToBeInvisible(By locator) {
        try {
            boolean invisible = wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            logger.debug("Element is invisible: {}", locator);
            return invisible;
        } catch (TimeoutException e) {
            logger.warn("Element still visible after timeout: {}", locator);
            return false;
        }
    }

    /**
     * Wait for text to be present in element
     */
    public boolean waitForTextToBePresentInElement(By locator, String text) {
        try {
            boolean present = wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
            logger.debug("Text '{}' is present in element: {}", text, locator);
            return present;
        } catch (TimeoutException e) {
            logger.warn("Text '{}' not present in element: {}", text, locator);
            return false;
        }
    }

    /**
     * Check if element is displayed
     */
    public boolean isElementDisplayed(By locator) {
        try {
            WebElement element = findElementWithRetry(locator);
            boolean displayed = element.isDisplayed();
            logger.debug("Element displayed status: {} for locator: {}", displayed, locator);
            return displayed;
        } catch (NoSuchElementException | TimeoutException e) {
            logger.debug("Element not displayed: {}", locator);
            return false;
        }
    }

    /**
     * Check if element is enabled
     */
    public boolean isElementEnabled(By locator) {
        try {
            WebElement element = findElementWithRetry(locator);
            boolean enabled = element.isEnabled();
            logger.debug("Element enabled status: {} for locator: {}", enabled, locator);
            return enabled;
        } catch (NoSuchElementException | TimeoutException e) {
            logger.debug("Element not found or not enabled: {}", locator);
            return false;
        }
    }

    /**
     * Check if element is selected (for checkboxes/radio buttons)
     */
    public boolean isElementSelected(By locator) {
        try {
            WebElement element = findElementWithRetry(locator);
            boolean selected = element.isSelected();
            logger.debug("Element selected status: {} for locator: {}", selected, locator);
            return selected;
        } catch (NoSuchElementException | TimeoutException e) {
            logger.debug("Element not found or not selected: {}", locator);
            return false;
        }
    }

    /**
     * Select dropdown by visible text
     */
    public void selectByVisibleText(By locator, String text) {
        try {
            WebElement element = findElementWithRetry(locator);
            Select select = new Select(element);
            select.selectByVisibleText(text);
            logger.info("Selected dropdown option by text '{}': {}", text, locator);
        } catch (NoSuchElementException e) {
            String errorMsg = String.format("Failed to select dropdown option '%s' in element: %s", text, locator);
            logger.error(errorMsg);
            throw new RuntimeException(errorMsg, e);
        }
    }

    /**
     * Select dropdown by value
     */
    public void selectByValue(By locator, String value) {
        try {
            WebElement element = findElementWithRetry(locator);
            Select select = new Select(element);
            select.selectByValue(value);
            logger.info("Selected dropdown option by value '{}': {}", value, locator);
        } catch (NoSuchElementException e) {
            String errorMsg = String.format("Failed to select dropdown value '%s' in element: %s", value, locator);
            logger.error(errorMsg);
            throw new RuntimeException(errorMsg, e);
        }
    }

    /**
     * Hover over element
     */
    public void hoverOverElement(By locator) {
        try {
            WebElement element = findElementWithRetry(locator);
            Actions actions = new Actions(driver);
            actions.moveToElement(element).perform();
            logger.info("Hovered over element: {}", locator);
        } catch (Exception e) {
            String errorMsg = String.format("Failed to hover over element: %s", locator);
            logger.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    /**
     * Scroll to element
     */
    public void scrollToElement(By locator) {
        try {
            WebElement element = findElementWithRetry(locator);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
            waitForMilliseconds(500); // Wait for scroll animation
            logger.info("Scrolled to element: {}", locator);
        } catch (Exception e) {
            String errorMsg = String.format("Failed to scroll to element: %s", locator);
            logger.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    /**
     * Get all elements matching locator
     */
    public List<WebElement> findElements(By locator) {
        try {
            List<WebElement> elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
            logger.debug("Found {} elements matching locator: {}", elements.size(), locator);
            return elements;
        } catch (TimeoutException e) {
            String errorMsg = String.format("No elements found matching locator: %s", locator);
            logger.error(errorMsg);
            throw new TimeoutException(errorMsg, e);
        }
    }

    /**
     * Get attribute value
     */
    public String getAttribute(By locator, String attribute) {
        try {
            WebElement element = findElementWithRetry(locator);
            String value = element.getAttribute(attribute);
            logger.debug("Retrieved attribute '{}' value: {} from element: {}", attribute, value, locator);
            return value;
        } catch (Exception e) {
            String errorMsg = String.format("Failed to get attribute '%s' from element: %s", attribute, locator);
            logger.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    /**
     * Wait for page to load completely
     */
    public void waitForPageLoad() {
        try {
            wait.until(driver -> ((JavascriptExecutor) driver)
                    .executeScript("return document.readyState").equals("complete"));
            logger.debug("Page loaded completely");
        } catch (TimeoutException e) {
            logger.warn("Page load timeout reached");
        }
    }

    /**
     * Wait for specified milliseconds
     */
    private void waitForMilliseconds(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            logger.error("Thread sleep interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Execute JavaScript
     */
    public Object executeScript(String script, Object... args) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            Object result = js.executeScript(script, args);
            logger.debug("Executed JavaScript: {}", script);
            return result;
        } catch (Exception e) {
            String errorMsg = String.format("Failed to execute JavaScript: %s", script);
            logger.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    /**
     * Clear browser cache and storage
     */
    public void clearBrowserData() {
        try {
            // Clear cookies
            driver.manage().deleteAllCookies();

            // Clear local storage
            executeScript("window.localStorage.clear();");

            // Clear session storage
            executeScript("window.sessionStorage.clear();");

            logger.info("Browser data cleared (cookies, localStorage, sessionStorage)");
        } catch (Exception e) {
            logger.error("Failed to clear browser data", e);
        }
    }
}