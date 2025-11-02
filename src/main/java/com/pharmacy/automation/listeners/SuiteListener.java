package com.pharmacy.automation.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;

public class SuiteListener implements ISuiteListener {
    private static final Logger logger = LogManager.getLogger(SuiteListener.class);
    private long startTime;

    @Override
    public void onStart(ISuite suite) {
        startTime = System.currentTimeMillis();
        logger.info("========================================");
        logger.info("Suite '{}' started execution", suite.getName());
        logger.info("========================================");
    }

    @Override
    public void onFinish(ISuite suite) {
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime) / 1000;

        logger.info("========================================");
        logger.info("Suite '{}' finished execution", suite.getName());
        logger.info("Total execution time: {} seconds ({} minutes)", duration, duration / 60);
        logger.info("========================================");
    }
}