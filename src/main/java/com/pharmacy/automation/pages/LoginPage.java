// 1. LoginPage.java
package com.pharmacy.automation.pages;

import com.pharmacy.automation.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {

    private final By emailField = By.id("email");
    private final By passwordField = By.id("password");
    private final By loginButton = By.xpath("//button[contains(text(),'Login') or contains(text(),'Sign In')]");
    private final By errorMessage = By.cssSelector(".error-message, .alert-danger");
    private final By emailValidation = By.xpath("//input[@id='email']/following-sibling::span");
    private final By passwordValidation = By.xpath("//input[@id='password']/following-sibling::span");
    private final By registerLink = By.linkText("Register");
    private final By forgotPasswordLink = By.linkText("Forgot Password");

    @Step("Login with username: {username}")
    public DashboardPage login(String username, String password) {
        logger.info("Attempting to login with username: {}", username);
        elementHelper.sendKeys(emailField, username);
        elementHelper.sendKeys(passwordField, password);
        elementHelper.click(loginButton);
        elementHelper.waitForPageLoad();
        return new DashboardPage();
    }

    @Step("Click login button")
    public void clickLoginButton() {
        elementHelper.click(loginButton);
    }

    @Step("Check if error message is displayed")
    public boolean isErrorMessageDisplayed() {
        return elementHelper.isElementDisplayed(errorMessage);
    }

    @Step("Get error message text")
    public String getErrorMessage() {
        return elementHelper.getText(errorMessage);
    }

    @Step("Check if email validation is displayed")
    public boolean isEmailValidationDisplayed() {
        return elementHelper.isElementDisplayed(emailValidation);
    }

    @Step("Check if password validation is displayed")
    public boolean isPasswordValidationDisplayed() {
        return elementHelper.isElementDisplayed(passwordValidation);
    }

    @Step("Click register link")
    public void clickRegisterLink() {
        elementHelper.click(registerLink);
    }

    @Step("Click forgot password link")
    public void clickForgotPasswordLink() {
        elementHelper.click(forgotPasswordLink);
    }
}
