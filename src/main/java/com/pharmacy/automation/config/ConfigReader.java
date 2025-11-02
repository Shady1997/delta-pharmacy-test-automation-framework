package com.pharmacy.automation.config;

import com.pharmacy.automation.constants.FrameworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration Reader for loading environment-specific properties
 * Supports testing, staging, and production environments
 *
 * @author Delta Pharmacy Automation Team
 */
public final class ConfigReader {

    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static Properties properties;
    private static final String DEFAULT_ENV = "testing";

    private ConfigReader() {
        // Private constructor to prevent instantiation
    }

    /**
     * Initialize configuration based on environment
     * Environment can be set via system property: -Denv=staging
     */
    public static void initializeConfig() {
        String environment = System.getProperty("env", DEFAULT_ENV);
        loadProperties(environment);
    }

    /**
     * Load properties file based on environment
     * @param environment Environment name (testing/staging/production)
     */
    private static void loadProperties(String environment) {
        String configFile = FrameworkConstants.CONFIG_PATH + environment + ".properties";
        properties = new Properties();

        try (FileInputStream fis = new FileInputStream(configFile)) {
            properties.load(fis);
            logger.info("Successfully loaded configuration for environment: {}", environment);
            logger.info("Application URL: {}", properties.getProperty(FrameworkConstants.APP_URL));
        } catch (IOException e) {
            logger.error("Failed to load configuration file: {}", configFile, e);
            throw new RuntimeException("Configuration file not found: " + configFile, e);
        }
    }

    /**
     * Get property value by key
     * @param key Property key
     * @return Property value
     */
    public static String getProperty(String key) {
        if (properties == null) {
            initializeConfig();
        }

        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Property '{}' not found in configuration", key);
            throw new RuntimeException("Property not found: " + key);
        }

        // Handle environment variable substitution ${ENV_VAR}
        if (value.contains("${") && value.contains("}")) {
            String envVar = value.substring(value.indexOf("${") + 2, value.indexOf("}"));
            String envValue = System.getenv(envVar);
            if (envValue != null) {
                value = value.replace("${" + envVar + "}", envValue);
                logger.debug("Resolved environment variable {} to value", envVar);
            }
        }

        return value;
    }

    /**
     * Get property with default value
     * @param key Property key
     * @param defaultValue Default value if property not found
     * @return Property value or default value
     */
    public static String getProperty(String key, String defaultValue) {
        try {
            return getProperty(key);
        } catch (RuntimeException e) {
            logger.debug("Using default value for property: {}", key);
            return defaultValue;
        }
    }

    /**
     * Get integer property
     * @param key Property key
     * @return Integer value
     */
    public static int getIntProperty(String key) {
        try {
            return Integer.parseInt(getProperty(key));
        } catch (NumberFormatException e) {
            logger.error("Invalid integer value for property: {}", key);
            throw new RuntimeException("Invalid integer property: " + key, e);
        }
    }

    /**
     * Get boolean property
     * @param key Property key
     * @return Boolean value
     */
    public static boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }

    /**
     * Get application URL
     * @return Application URL
     */
    public static String getAppUrl() {
        return getProperty(FrameworkConstants.APP_URL);
    }

    /**
     * Get browser type
     * @return Browser name
     */
    public static String getBrowser() {
        return getProperty("browser", FrameworkConstants.CHROME);
    }

    /**
     * Check if headless mode is enabled
     * @return true if headless mode is enabled
     */
    public static boolean isHeadless() {
        return getBooleanProperty("headless");
    }

    /**
     * Get implicit wait timeout
     * @return Timeout in seconds
     */
    public static int getImplicitWait() {
        return getIntProperty("implicit.wait");
    }

    /**
     * Get explicit wait timeout
     * @return Timeout in seconds
     */
    public static int getExplicitWait() {
        return getIntProperty("explicit.wait");
    }

    /**
     * Get page load timeout
     * @return Timeout in seconds
     */
    public static int getPageLoadTimeout() {
        return getIntProperty("page.load.timeout");
    }

    /**
     * Reload configuration (useful for switching environments dynamically)
     */
    public static void reloadConfig() {
        properties = null;
        initializeConfig();
        logger.info("Configuration reloaded successfully");
    }

    /**
     * Get all properties
     * @return Properties object
     */
    public static Properties getAllProperties() {
        if (properties == null) {
            initializeConfig();
        }
        return properties;
    }
}