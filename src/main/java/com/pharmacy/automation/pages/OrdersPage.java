// 6. OrdersPage.java
package com.pharmacy.automation.pages;

import com.pharmacy.automation.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class OrdersPage extends BasePage {

    private final By ordersTitle = By.xpath("//h1[contains(text(),'Orders') or contains(text(),'My Orders')]");
    private final By orderConfirmation = By.xpath("//*[contains(text(),'Order placed') or contains(text(),'successful')]");
    private final By orderCards = By.cssSelector(".order-card, [class*='order']");
    private final By orderStatus = By.cssSelector(".order-status, [class*='status']");
    private final By cancelOrderButton = By.xpath("//button[contains(text(),'Cancel')]");
    private final By orderDetails = By.cssSelector(".order-details, [class*='details']");
    private final By noOrdersMessage = By.xpath("//*[contains(text(),'No orders')]");

    @Step("Check if order confirmation is displayed")
    public boolean isOrderConfirmationDisplayed() {
        return elementHelper.isElementDisplayed(orderConfirmation);
    }

    @Step("Get order status")
    public String getOrderStatus() {
        return elementHelper.getText(orderStatus);
    }

    @Step("Get order count")
    public int getOrderCount() {
        try {
            return elementHelper.findElements(orderCards).size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Step("Cancel order")
    public void cancelOrder() {
        elementHelper.click(cancelOrderButton);
    }

    @Step("Check if no orders message is displayed")
    public boolean isNoOrdersMessageDisplayed() {
        return elementHelper.isElementDisplayed(noOrdersMessage);
    }
}
