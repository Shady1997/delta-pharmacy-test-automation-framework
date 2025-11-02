package com.pharmacy.automation.listeners;

import com.pharmacy.automation.constants.FrameworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private static final Logger logger = LogManager.getLogger(RetryAnalyzer.class);
    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < FrameworkConstants.MAX_RETRY_COUNT) {
            retryCount++;
            logger.warn("Retrying test '{}' - Attempt {} of {}",
                    result.getMethod().getMethodName(),
                    retryCount,
                    FrameworkConstants.MAX_RETRY_COUNT);

            try {
                Thread.sleep(FrameworkConstants.RETRY_INTERVAL_MS);
            } catch (InterruptedException e) {
                logger.error("Retry sleep interrupted", e);
                Thread.currentThread().interrupt();
            }

            return true;
        }

        logger.error("Test '{}' failed after {} retry attempts",
                result.getMethod().getMethodName(),
                FrameworkConstants.MAX_RETRY_COUNT);
        return false;
    }
}