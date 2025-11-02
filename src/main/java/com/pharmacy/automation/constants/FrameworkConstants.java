package com.pharmacy.automation.constants;

import java.time.Duration;

/**
 * Framework constants and configuration values
 * @author Delta Pharmacy Automation Team
 */
public final class FrameworkConstants {

    private FrameworkConstants() {
        // Private constructor to prevent instantiation
    }

    // Timeouts
    public static final Duration EXPLICIT_WAIT = Duration.ofSeconds(20);
    public static final Duration IMPLICIT_WAIT = Duration.ofSeconds(10);
    public static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(30);
    public static final Duration SCRIPT_TIMEOUT = Duration.ofSeconds(20);
    public static final Duration POLLING_INTERVAL = Duration.ofMillis(500);

    // Retry Configuration
    public static final int MAX_RETRY_COUNT = 3;
    public static final int RETRY_INTERVAL_MS = 1000;
    public static final int ELEMENT_RETRY_COUNT = 3;

    // Paths
    public static final String PROJECT_PATH = System.getProperty("user.dir");
    public static final String CONFIG_PATH = PROJECT_PATH + "/src/main/resources/config/";
    public static final String TESTDATA_PATH = PROJECT_PATH + "/src/main/resources/testdata/";
    public static final String SCREENSHOTS_PATH = PROJECT_PATH + "/test-output/screenshots/";
    public static final String VIDEOS_PATH = PROJECT_PATH + "/test-output/videos/";
    public static final String REPORTS_PATH = PROJECT_PATH + "/test-output/reports/";
    public static final String EXTENT_REPORT_PATH = REPORTS_PATH + "ExtentReport.html";
    public static final String ALLURE_RESULTS_PATH = PROJECT_PATH + "/target/allure-results/";

    // Browser Configuration
    public static final String CHROME = "chrome";
    public static final String FIREFOX = "firefox";
    public static final String EDGE = "edge";
    public static final String SAFARI = "safari";

    // Mobile View Sizes
    public static final String IPHONE_14_PRO = "393x852";
    public static final String IPHONE_13 = "390x844";
    public static final String IPHONE_SE = "375x667";
    public static final String SAMSUNG_GALAXY_S21 = "360x800";
    public static final String GOOGLE_PIXEL_6 = "412x915";
    public static final String IPAD_PRO = "1024x1366";
    public static final String IPAD_AIR = "820x1180";

    // Network Conditions (Selenium 4)
    public static final String NETWORK_OFFLINE = "offline";
    public static final String NETWORK_SLOW_2G = "slow2g";
    public static final String NETWORK_2G = "2g";
    public static final String NETWORK_3G = "3g";
    public static final String NETWORK_FAST_3G = "fast3g";
    public static final String NETWORK_4G = "4g";
    public static final String NETWORK_ONLINE = "online";

    // Test Data
    public static final String USERS_JSON = TESTDATA_PATH + "users.json";
    public static final String PRODUCTS_JSON = TESTDATA_PATH + "products.json";
    public static final String TESTDATA_EXCEL = TESTDATA_PATH + "testdata.xlsx";

    // Screenshot Configuration
    public static final boolean TAKE_SCREENSHOT_ON_PASS = false;
    public static final boolean TAKE_SCREENSHOT_ON_FAIL = true;
    public static final boolean TAKE_SCREENSHOT_ON_SKIP = false;

    // Video Recording Configuration
    public static final boolean ENABLE_VIDEO_RECORDING = true;
    public static final boolean RECORD_FAILED_TESTS_ONLY = true;

    // Logging
    public static final boolean ENABLE_LOGGING = true;
    public static final String LOG_LEVEL = "INFO"; // DEBUG, INFO, WARN, ERROR

    // Parallel Execution
    public static final int THREAD_COUNT = 3;
    public static final String DATA_PROVIDER_THREAD_COUNT = "2";

    // Localization
    public static final String DEFAULT_LOCALE = "en";
    public static final String LOCALIZATION_PATH = PROJECT_PATH + "/src/main/resources/localization/";

    // Report Configuration
    public static final String REPORT_TITLE = "Delta Pharmacy Automation Test Results";
    public static final String REPORT_NAME = "Test Execution Report";
    public static final String EXTENT_REPORT_THEME = "dark"; // dark or standard

    // Application Configuration Keys
    public static final String APP_URL = "app.url";
    public static final String APP_USERNAME = "app.username";
    public static final String APP_PASSWORD = "app.password";
    public static final String ADMIN_USERNAME = "admin.username";
    public static final String ADMIN_PASSWORD = "admin.password";
    public static final String PHARMACIST_USERNAME = "pharmacist.username";
    public static final String PHARMACIST_PASSWORD = "pharmacist.password";

    // Browser Options
    public static final boolean HEADLESS_MODE = false;
    public static final boolean MAXIMIZE_WINDOW = true;
    public static final boolean DELETE_COOKIES = true;
    public static final boolean CLEAR_CACHE = true;

    // Selenium Grid
    public static final boolean USE_SELENIUM_GRID = false;
    public static final String SELENIUM_GRID_URL = "http://localhost:4444/wd/hub";

    // API Configuration (for hybrid testing if needed)
    public static final String API_BASE_URL = "http://localhost:8545/pharmacy-api/api";
}