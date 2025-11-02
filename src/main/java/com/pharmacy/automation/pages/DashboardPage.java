// 2. DashboardPage.java
package com.pharmacy.automation.pages;

import com.pharmacy.automation.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class DashboardPage extends BasePage {

    private final By dashboardTitle = By.xpath("//h1[contains(text(),'Dashboard')]");
    private final By welcomeMessage = By.cssSelector(".welcome-message, p");
    private final By productsCard = By.xpath("//div[contains(text(),'Total Products')]");
    private final By ordersCard = By.xpath("//div[contains(text(),'Orders')]");
    private final By prescriptionsCard = By.xpath("//div[contains(text(),'Prescriptions')]");
    private final By usersCard = By.xpath("//div[contains(text(),'Users')]");
    private final By logoutButton = By.xpath("//button[contains(text(),'Logout')]");
    private final By productsMenu = By.linkText("Products");
    private final By ordersMenu = By.linkText("Orders");
    private final By cartIcon = By.cssSelector("[data-testid='cart-icon'], .cart-icon");

    @Step("Check if dashboard is displayed")
    public boolean isDashboardDisplayed() {
        return elementHelper.isElementDisplayed(dashboardTitle);
    }

    @Step("Get welcome message")
    public String getWelcomeMessage() {
        return elementHelper.getText(welcomeMessage);
    }

    @Step("Click products menu")
    public ProductsPage clickProductsMenu() {
        elementHelper.click(productsMenu);
        return new ProductsPage();
    }

    @Step("Click orders menu")
    public OrdersPage clickOrdersMenu() {
        elementHelper.click(ordersMenu);
        return new OrdersPage();
    }

    @Step("Click logout")
    public LoginPage logout() {
        elementHelper.click(logoutButton);
        return new LoginPage();
    }

    @Step("Navigate to cart")
    public CartPage navigateToCart() {
        elementHelper.click(cartIcon);
        return new CartPage();
    }

    @Step("Get products count from card")
    public String getProductsCount() {
        return elementHelper.getText(productsCard);
    }
}