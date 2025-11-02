// 5. CheckoutPage.java
package com.pharmacy.automation.pages;

import com.pharmacy.automation.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class CheckoutPage extends BasePage {

    private final By checkoutTitle = By.xpath("//h1[contains(text(),'Checkout')]");
    private final By shippingAddressField = By.cssSelector("textarea[name*='address'], textarea[placeholder*='address']");
    private final By paymentMethodCash = By.xpath("//button[contains(text(),'Cash') or contains(text(),'COD')]");
    private final By paymentMethodCard = By.xpath("//button[contains(text(),'Card') or contains(text(),'Credit')]");
    private final By cardNumberField = By.cssSelector("input[placeholder*='Card Number']");
    private final By cardHolderField = By.cssSelector("input[placeholder*='Card Holder']");
    private final By expiryDateField = By.cssSelector("input[placeholder*='Expiry']");
    private final By cvvField = By.cssSelector("input[placeholder*='CVV']");
    private final By placeOrderButton = By.xpath("//button[contains(text(),'Place Order') or contains(text(),'Confirm')]");
    private final By orderSummary = By.cssSelector(".order-summary, [class*='summary']");

    @Step("Enter shipping address: {address}")
    public void enterShippingAddress(String address) {
        elementHelper.sendKeys(shippingAddressField, address);
    }

    @Step("Select payment method: {method}")
    public void selectPaymentMethod(String method) {
        if (method.equalsIgnoreCase("Cash") || method.equalsIgnoreCase("COD")) {
            elementHelper.click(paymentMethodCash);
        } else {
            elementHelper.click(paymentMethodCard);
        }
    }

    @Step("Enter card number: {cardNumber}")
    public void enterCardNumber(String cardNumber) {
        elementHelper.sendKeys(cardNumberField, cardNumber);
    }

    @Step("Enter card holder name: {cardHolder}")
    public void enterCardHolder(String cardHolder) {
        elementHelper.sendKeys(cardHolderField, cardHolder);
    }

    @Step("Enter expiry date: {expiryDate}")
    public void enterExpiryDate(String expiryDate) {
        elementHelper.sendKeys(expiryDateField, expiryDate);
    }

    @Step("Enter CVV: {cvv}")
    public void enterCVV(String cvv) {
        elementHelper.sendKeys(cvvField, cvv);
    }

    @Step("Confirm order")
    public OrdersPage confirmOrder() {
        elementHelper.click(placeOrderButton);
        elementHelper.waitForPageLoad();
        return new OrdersPage();
    }

    @Step("Check if checkout page is displayed")
    public boolean isCheckoutPageDisplayed() {
        return elementHelper.isElementDisplayed(checkoutTitle);
    }
}