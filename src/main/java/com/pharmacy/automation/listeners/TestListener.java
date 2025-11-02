package com.pharmacy.automation.listeners;

import com.pharmacy.automation.utils.ScreenshotHelper;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.*;

import java.io.ByteArrayInputStream;

public class TestListener implements ITestListener, ISuiteListener {
    private static final Logger logger = LogManager.getLogger(TestListener.class);

    @Override
    public void onStart(ISuite suite) {
        logger.info("========================================");
        logger.info("Test Suite Started: {}", suite.getName());
        logger.info("========================================");
    }

    @Override
    public void onFinish(ISuite suite) {
        logger.info("========================================");
        logger.info("Test Suite Finished: {}", suite.getName());
        logger.info("========================================");
    }

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Test Started: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test Passed: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test Failed: {}", result.getMethod().getMethodName());
        logger.error("Failure Reason: {}", result.getThrowable().getMessage());

        try {
            WebDriver driver = (WebDriver) result.getTestClass().getRealClass()
                    .getDeclaredField("driver").get(result.getInstance());
            byte[] screenshot = ScreenshotHelper.captureScreenshotAsBytes(driver);
            Allure.addAttachment("Failure Screenshot", "image/png",
                    new ByteArrayInputStream(screenshot), ".png");
        } catch (Exception e) {
            logger.error("Failed to capture screenshot for failed test", e);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test Skipped: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.warn("Test Failed but within success percentage: {}", result.getMethod().getMethodName());
    }
}