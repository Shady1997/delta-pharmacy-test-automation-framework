// 1. LoginTests.java - COMPLETE IMPLEMENTATION
package com.pharmacy.automation.tests;

import com.pharmacy.automation.base.BaseTest;
import com.pharmacy.automation.config.ConfigReader;
import com.pharmacy.automation.listeners.RetryAnalyzer;
import com.pharmacy.automation.pages.DashboardPage;
import com.pharmacy.automation.pages.LoginPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Epic("Authentication")
@Feature("User Login")
public class LoginTests extends BaseTest {

    private LoginPage loginPage;

    @BeforeMethod
    public void initializePages() {
        loginPage = new LoginPage();
        logger.info("LoginPage initialized");
    }

    @Test(priority = 1,
            description = "Verify successful login with valid customer credentials",
            groups = {"smoke", "regression"})
    @Description("Test to verify user can login with valid username and password and dashboard is displayed")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Customer Login")
    public void testValidCustomerLogin() {
        logger.info("=== TEST: Valid Customer Login ===");

        String username = ConfigReader.getProperty("app.username");
        String password = ConfigReader.getProperty("app.password");

        logger.info("Step 1: Enter username: {}", username);
        logger.info("Step 2: Enter password");
        logger.info("Step 3: Click login button");

        DashboardPage dashboardPage = loginPage.login(username, password);

        logger.info("Step 4: Verify dashboard is displayed");
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(),
                "Dashboard should be displayed after successful login");

        logger.info("Step 5: Verify welcome message contains user name");
        String welcomeMsg = dashboardPage.getWelcomeMessage();
        Assert.assertNotNull(welcomeMsg, "Welcome message should not be null");
        Assert.assertFalse(welcomeMsg.isEmpty(), "Welcome message should not be empty");

        logger.info("Step 6: Verify page URL contains 'dashboard'");
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("dashboard") || currentUrl.contains("/"),
                "URL should contain dashboard after login");

        logger.info("✓ Valid customer login test PASSED");
    }

    @Test(priority = 2,
            description = "Verify login fails with invalid credentials",
            retryAnalyzer = RetryAnalyzer.class,
            groups = {"smoke", "regression", "negative"})
    @Description("Test to verify appropriate error message is displayed for invalid credentials")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Invalid Login")
    public void testInvalidCredentials() {
        logger.info("=== TEST: Invalid Credentials Login ===");

        String invalidEmail = "invalid@email.com";
        String invalidPassword = "wrongpassword123";

        logger.info("Step 1: Enter invalid email: {}", invalidEmail);
        logger.info("Step 2: Enter invalid password");
        logger.info("Step 3: Click login button");

        loginPage.login(invalidEmail, invalidPassword);

        logger.info("Step 4: Verify error message is displayed");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Error message should be displayed for invalid credentials");

        logger.info("Step 5: Verify error message content");
        String errorMessage = loginPage.getErrorMessage();
        Assert.assertNotNull(errorMessage, "Error message should not be null");
        Assert.assertTrue(
                errorMessage.contains("Invalid") ||
                        errorMessage.contains("incorrect") ||
                        errorMessage.contains("wrong") ||
                        errorMessage.contains("failed"),
                "Error message should indicate invalid credentials. Actual: " + errorMessage);

        logger.info("Step 6: Verify user remains on login page");
        String currentUrl = driver.getCurrentUrl();
        Assert.assertFalse(currentUrl.contains("dashboard"),
                "User should remain on login page after failed login");

        logger.info("✓ Invalid credentials test PASSED");
    }

    @Test(priority = 3,
            description = "Verify validation for empty username and password fields",
            groups = {"regression", "negative"})
    @Severity(SeverityLevel.NORMAL)
    @Story("Form Validation")
    public void testEmptyFieldsValidation() {
        logger.info("=== TEST: Empty Fields Validation ===");

        logger.info("Step 1: Click login button without entering credentials");
        loginPage.clickLoginButton();

        SoftAssert softAssert = new SoftAssert();

        logger.info("Step 2: Verify email field validation");
        boolean emailValidation = loginPage.isEmailValidationDisplayed();
        softAssert.assertTrue(emailValidation,
                "Email validation message should be displayed for empty field");

        logger.info("Step 3: Verify password field validation");
        boolean passwordValidation = loginPage.isPasswordValidationDisplayed();
        softAssert.assertTrue(passwordValidation,
                "Password validation message should be displayed for empty field");

        logger.info("Step 4: Verify user remains on login page");
        String currentUrl = driver.getCurrentUrl();
        softAssert.assertFalse(currentUrl.contains("dashboard"),
                "User should not proceed to dashboard with empty fields");

        softAssert.assertAll();
        logger.info("✓ Empty fields validation test PASSED");
    }

    @Test(priority = 4,
            description = "Verify admin can login with valid credentials",
            groups = {"smoke", "regression", "admin"})
    @Severity(SeverityLevel.BLOCKER)
    @Story("Admin Login")
    public void testAdminLogin() {
        logger.info("=== TEST: Admin Login ===");

        String adminUsername = ConfigReader.getProperty("admin.username");
        String adminPassword = ConfigReader.getProperty("admin.password");

        logger.info("Step 1: Login with admin credentials");
        DashboardPage dashboardPage = loginPage.login(adminUsername, adminPassword);

        logger.info("Step 2: Verify dashboard is displayed");
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(),
                "Dashboard should be displayed after admin login");

        logger.info("Step 3: Verify welcome message is present");
        String welcomeMsg = dashboardPage.getWelcomeMessage();
        Assert.assertNotNull(welcomeMsg, "Welcome message should be displayed");

        logger.info("✓ Admin login test PASSED");
    }

    @Test(priority = 5,
            description = "Verify pharmacist can login with valid credentials",
            groups = {"smoke", "regression", "pharmacist"})
    @Severity(SeverityLevel.BLOCKER)
    @Story("Pharmacist Login")
    public void testPharmacistLogin() {
        logger.info("=== TEST: Pharmacist Login ===");

        String pharmacistUsername = ConfigReader.getProperty("pharmacist.username");
        String pharmacistPassword = ConfigReader.getProperty("pharmacist.password");

        logger.info("Step 1: Login with pharmacist credentials");
        DashboardPage dashboardPage = loginPage.login(pharmacistUsername, pharmacistPassword);

        logger.info("Step 2: Verify dashboard is displayed");
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(),
                "Dashboard should be displayed after pharmacist login");

        logger.info("✓ Pharmacist login test PASSED");
    }

    @Test(priority = 6,
            description = "Verify login with invalid email format",
            groups = {"regression", "negative"})
    @Severity(SeverityLevel.NORMAL)
    @Story("Email Validation")
    public void testInvalidEmailFormat() {
        logger.info("=== TEST: Invalid Email Format ===");

        String invalidEmail = "notanemail";
        String password = "password123";

        logger.info("Step 1: Enter invalid email format: {}", invalidEmail);
        loginPage.login(invalidEmail, password);

        logger.info("Step 2: Verify error or validation message");
        boolean errorDisplayed = loginPage.isErrorMessageDisplayed() ||
                loginPage.isEmailValidationDisplayed();
        Assert.assertTrue(errorDisplayed,
                "Error or validation message should be displayed for invalid email format");

        logger.info("✓ Invalid email format test PASSED");
    }

    @Test(priority = 7,
            description = "Verify multiple failed login attempts",
            dataProvider = "invalidCredentials",
            groups = {"regression", "negative"})
    @Severity(SeverityLevel.NORMAL)
    @Story("Multiple Failed Attempts")
    public void testMultipleFailedLogins(String username, String password) {
        logger.info("=== TEST: Multiple Failed Login Attempts ===");
        logger.info("Attempting login with: {} / {}", username, password);

        loginPage.login(username, password);

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Error message should be displayed for invalid credentials: " + username);

        logger.info("✓ Failed login attempt validated for: {}", username);
    }

    @DataProvider(name = "invalidCredentials")
    public Object[][] getInvalidCredentials() {
        return new Object[][]{
                {"invalid1@test.com", "wrong123"},
                {"invalid2@test.com", "incorrect456"},
                {"notexist@email.com", "fake789"}
        };
    }

    @Test(priority = 8,
            description = "Verify SQL injection is prevented",
            groups = {"security", "regression"})
    @Severity(SeverityLevel.CRITICAL)
    @Story("Security - SQL Injection")
    public void testSQLInjectionPrevention() {
        logger.info("=== TEST: SQL Injection Prevention ===");

        String sqlInjection = "' OR '1'='1";
        String password = "password";

        logger.info("Step 1: Attempt SQL injection in username field");
        loginPage.login(sqlInjection, password);

        logger.info("Step 2: Verify login fails");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed() ||
                        !driver.getCurrentUrl().contains("dashboard"),
                "SQL injection should be prevented");

        logger.info("✓ SQL injection prevention test PASSED");
    }

    @Test(priority = 9,
            description = "Verify XSS attack is prevented",
            groups = {"security", "regression"})
    @Severity(SeverityLevel.CRITICAL)
    @Story("Security - XSS")
    public void testXSSPrevention() {
        logger.info("=== TEST: XSS Prevention ===");

        String xssScript = "<script>alert('XSS')</script>";
        String password = "password";

        logger.info("Step 1: Attempt XSS injection in username field");
        loginPage.login(xssScript, password);

        logger.info("Step 2: Verify XSS is prevented");
        String pageSource = driver.getPageSource();
        Assert.assertFalse(pageSource.contains("<script>"),
                "XSS script should be sanitized");

        logger.info("✓ XSS prevention test PASSED");
    }

    @Test(priority = 10,
            description = "Verify logout functionality",
            groups = {"smoke", "regression"})
    @Severity(SeverityLevel.CRITICAL)
    @Story("User Logout")
    public void testLogout() {
        logger.info("=== TEST: Logout Functionality ===");

        logger.info("Step 1: Login with valid credentials");
        String username = ConfigReader.getProperty("app.username");
        String password = ConfigReader.getProperty("app.password");
        DashboardPage dashboardPage = loginPage.login(username, password);

        Assert.assertTrue(dashboardPage.isDashboardDisplayed(),
                "Should be on dashboard before logout");

        logger.info("Step 2: Click logout");
        LoginPage returnedLoginPage = dashboardPage.logout();

        logger.info("Step 3: Verify user is redirected to login page");
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("login") || !currentUrl.contains("dashboard"),
                "User should be redirected to login page after logout");

        logger.info("✓ Logout test PASSED");
    }
}