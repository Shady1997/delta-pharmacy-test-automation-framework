// 3. ProductSteps.java
package com.pharmacy.automation.stepdefinitions;

import com.pharmacy.automation.pages.ProductsPage;
import io.cucumber.java.en.*;
import org.testng.Assert;

public class ProductSteps {
    private ProductsPage productsPage;

    @Given("I am on the products page")
    public void iAmOnTheProductsPage() {
        productsPage = new ProductsPage();
    }

    @When("I search for {string}")
    public void iSearchFor(String productName) {
        productsPage.searchProduct(productName);
    }

    @When("I add {string} to cart")
    public void iAddToCart(String productName) {
        productsPage.addProductToCart(productName);
    }

    @Then("products should be displayed")
    public void productsShouldBeDisplayed() {
        Assert.assertTrue(productsPage.areProductsDisplayed(),
                "Products should be displayed");
    }

    @Then("{string} should be in stock")
    public void productShouldBeInStock(String productName) {
        Assert.assertTrue(productsPage.isProductInStock(productName),
                "Product should be in stock");
    }
}
