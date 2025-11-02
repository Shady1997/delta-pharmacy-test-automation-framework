// 3. ProductsPage.java
package com.pharmacy.automation.pages;

import com.pharmacy.automation.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class ProductsPage extends BasePage {

    private final By productsTitle = By.xpath("//h1[contains(text(),'Products')]");
    private final By searchBox = By.cssSelector("input[placeholder*='Search']");
    private final By addProductButton = By.xpath("//button[contains(text(),'Add Product')]");
    private final By productCards = By.cssSelector(".product-card, [class*='product']");
    private final By addToCartButton = By.xpath("//button[contains(text(),'Add to Cart')]");
    private final By productName = By.cssSelector(".product-name, h3");
    private final By productPrice = By.cssSelector(".product-price, [class*='price']");
    private final By productStock = By.cssSelector(".stock-quantity, [class*='stock']");

    @Step("Search for product: {productName}")
    public void searchProduct(String productName) {
        logger.info("Searching for product: {}", productName);
        elementHelper.sendKeys(searchBox, productName);
        elementHelper.waitForPageLoad();
    }

    @Step("Add product to cart by name: {productName}")
    public void addProductToCart(String productName) {
        By specificProduct = By.xpath("//h3[contains(text(),'" + productName + "')]/ancestor::div[contains(@class,'product')]//button[contains(text(),'Add to Cart')]");
        elementHelper.scrollToElement(specificProduct);
        elementHelper.click(specificProduct);
        logger.info("Added product to cart: {}", productName);
    }

    @Step("Check if products are displayed")
    public boolean areProductsDisplayed() {
        return elementHelper.isElementDisplayed(productCards);
    }

    @Step("Get first product name")
    public String getFirstProductName() {
        return elementHelper.getText(productName);
    }

    @Step("Get first product price")
    public String getFirstProductPrice() {
        return elementHelper.getText(productPrice);
    }

    @Step("Click add product button (Admin)")
    public void clickAddProduct() {
        elementHelper.click(addProductButton);
    }

    @Step("Check if product is in stock: {productName}")
    public boolean isProductInStock(String productName) {
        By stockLocator = By.xpath("//h3[contains(text(),'" + productName + "')]/ancestor::div[contains(@class,'product')]//span[contains(@class,'stock')]");
        String stockText = elementHelper.getText(stockLocator);
        return !stockText.contains("Out of Stock") && !stockText.equals("0");
    }
}
