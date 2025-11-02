package com.pharmacy.automation.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class JsonReader {
    private static final Logger logger = LogManager.getLogger(JsonReader.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static JsonNode readJson(String filePath) {
        try {
            File file = new File(filePath);
            return objectMapper.readTree(file);
        } catch (IOException e) {
            logger.error("Error reading JSON file: {}", filePath, e);
            return null;
        }
    }

    public static String getTestData(String filePath, String key) {
        try {
            JsonNode root = readJson(filePath);
            return root.get(key).asText();
        } catch (Exception e) {
            logger.error("Error getting test data for key: {}", key, e);
            return null;
        }
    }

    public static <T> T readJsonAs(String filePath, Class<T> clazz) {
        try {
            File file = new File(filePath);
            return objectMapper.readValue(file, clazz);
        } catch (IOException e) {
            logger.error("Error reading JSON as class: {}", clazz.getName(), e);
            return null;
        }
    }
}