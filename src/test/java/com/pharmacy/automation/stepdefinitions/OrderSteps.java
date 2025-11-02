// 6. OrderSteps.java
package com.pharmacy.automation.stepdefinitions;

import com.pharmacy.automation.pages.OrdersPage;
import io.cucumber.java.en.*;
import org.testng.Assert;

public class OrderSteps {
    private OrdersPage ordersPage;

    @Given("I am on orders page")
    public void iAmOnOrdersPage() {
        ordersPage = new OrdersPage();
    }

    @When("I cancel the order")
    public void iCancelTheOrder() {
        ordersPage.cancelOrder();
    }

    @Then("I should see order confirmation")
    public void iShouldSeeOrderConfirmation() {
        Assert.assertTrue(ordersPage.isOrderConfirmationDisplayed(),
                "Order confirmation should be displayed");
    }

    @Then("order status should be {string}")
    public void orderStatusShouldBe(String expectedStatus) {
        String actualStatus = ordersPage.getOrderStatus();
        Assert.assertTrue(actualStatus.contains(expectedStatus),
                "Order status should be " + expectedStatus);
    }

    @Then("I should have {int} orders")
    public void iShouldHaveOrders(int count) {
        Assert.assertEquals(ordersPage.getOrderCount(), count,
                "Should have " + count + " orders");
    }
}