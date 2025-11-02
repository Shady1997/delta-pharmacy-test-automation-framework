// 5. CheckoutSteps.java
package com.pharmacy.automation.stepdefinitions;

import com.pharmacy.automation.pages.CheckoutPage;
import com.pharmacy.automation.pages.OrdersPage;
import io.cucumber.java.en.*;
import org.testng.Assert;

public class CheckoutSteps {
    private CheckoutPage checkoutPage;
    private OrdersPage ordersPage;

    @When("I enter shipping address {string}")
    public void iEnterShippingAddress(String address) {
        checkoutPage = new CheckoutPage();
        checkoutPage.enterShippingAddress(address);
    }

    @When("I select payment method {string}")
    public void iSelectPaymentMethod(String paymentMethod) {
        checkoutPage.selectPaymentMethod(paymentMethod);
    }

    @When("I enter card number {string}")
    public void iEnterCardNumber(String cardNumber) {
        checkoutPage.enterCardNumber(cardNumber);
    }

    @When("I enter card holder {string}")
    public void iEnterCardHolder(String cardHolder) {
        checkoutPage.enterCardHolder(cardHolder);
    }

    @When("I enter expiry date {string}")
    public void iEnterExpiryDate(String expiryDate) {
        checkoutPage.enterExpiryDate(expiryDate);
    }

    @When("I enter CVV {string}")
    public void iEnterCVV(String cvv) {
        checkoutPage.enterCVV(cvv);
    }

    @When("I confirm the order")
    public void iConfirmTheOrder() {
        ordersPage = checkoutPage.confirmOrder();
    }

    @Then("I should see checkout page")
    public void iShouldSeeCheckoutPage() {
        Assert.assertTrue(checkoutPage.isCheckoutPageDisplayed(),
                "Checkout page should be displayed");
    }
}