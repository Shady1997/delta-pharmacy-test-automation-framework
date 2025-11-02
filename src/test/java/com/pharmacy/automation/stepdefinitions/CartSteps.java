// 4. CartSteps.java
package com.pharmacy.automation.stepdefinitions;

import com.pharmacy.automation.pages.CartPage;
import com.pharmacy.automation.pages.CheckoutPage;
import io.cucumber.java.en.*;
import org.testng.Assert;

public class CartSteps {
    private CartPage cartPage;
    private CheckoutPage checkoutPage;

    @Given("I have {int} items in cart")
    public void iHaveItemsInCart(int count) {
        cartPage = new CartPage();
        Assert.assertEquals(cartPage.getCartItemCount(), count,
                "Cart should have " + count + " items");
    }

    @When("I proceed to checkout")
    public void iProceedToCheckout() {
        checkoutPage = cartPage.proceedToCheckout();
    }

    @Then("my cart should contain {int} items")
    public void myCartShouldContainItems(int count) {
        Assert.assertEquals(cartPage.getCartItemCount(), count,
                "Cart should contain " + count + " items");
    }

    @Then("cart total should be displayed")
    public void cartTotalShouldBeDisplayed() {
        String total = cartPage.getCartTotal();
        Assert.assertNotNull(total, "Cart total should be displayed");
    }
}
