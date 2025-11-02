package com.pharmacy.automation.utils;

import com.pharmacy.automation.constants.FrameworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class LocalizationHelper {
    private static final Logger logger = LogManager.getLogger(LocalizationHelper.class);
    private Properties properties;
    private String locale;

    public LocalizationHelper(String locale) {
        this.locale = locale;
        loadProperties();
    }

    private void loadProperties() {
        String filePath = FrameworkConstants.LOCALIZATION_PATH + "messages_" + locale + ".properties";
        properties = new Properties();

        try (FileInputStream fis = new FileInputStream(filePath)) {
            properties.load(fis);
            logger.info("Loaded localization file for locale: {}", locale);
        } catch (IOException e) {
            logger.error("Failed to load localization file: {}", filePath, e);
        }
    }

    public String getMessage(String key) {
        String message = properties.getProperty(key);
        if (message == null) {
            logger.warn("Message not found for key: {} in locale: {}", key, locale);
            return key;
        }
        return message;
    }

    public String getMessage(String key, Object... args) {
        String message = getMessage(key);
        return String.format(message, args);
    }
}