// 4. CartPage.java
package com.pharmacy.automation.pages;

import com.pharmacy.automation.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class CartPage extends BasePage {

    private final By cartTitle = By.xpath("//h1[contains(text(),'Cart') or contains(text(),'Shopping Cart')]");
    private final By cartItems = By.cssSelector(".cart-item, [class*='cart-item']");
    private final By itemQuantity = By.cssSelector("input[type='number'], .quantity-input");
    private final By removeButton = By.xpath("//button[contains(text(),'Remove')]");
    private final By checkoutButton = By.xpath("//button[contains(text(),'Checkout') or contains(text(),'Proceed')]");
    private final By cartTotal = By.cssSelector(".cart-total, [class*='total']");
    private final By emptyCartMessage = By.xpath("//*[contains(text(),'empty') or contains(text(),'no items')]");
    private final By continueShoppingButton = By.xpath("//button[contains(text(),'Continue Shopping')]");

    @Step("Get cart item count")
    public int getCartItemCount() {
        try {
            return elementHelper.findElements(cartItems).size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Step("Proceed to checkout")
    public CheckoutPage proceedToCheckout() {
        elementHelper.click(checkoutButton);
        return new CheckoutPage();
    }

    @Step("Get cart total")
    public String getCartTotal() {
        return elementHelper.getText(cartTotal);
    }

    @Step("Remove item from cart")
    public void removeItemFromCart() {
        elementHelper.click(removeButton);
    }

    @Step("Check if cart is empty")
    public boolean isCartEmpty() {
        return elementHelper.isElementDisplayed(emptyCartMessage);
    }

    @Step("Update item quantity to: {quantity}")
    public void updateItemQuantity(int quantity) {
        elementHelper.sendKeys(itemQuantity, String.valueOf(quantity));
    }

    @Step("Continue shopping")
    public ProductsPage continueShopping() {
        elementHelper.click(continueShoppingButton);
        return new ProductsPage();
    }
}
