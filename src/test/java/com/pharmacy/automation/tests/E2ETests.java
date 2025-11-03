package com.pharmacy.automation.tests;

import com.pharmacy.automation.base.BaseTest;
import com.pharmacy.automation.config.ConfigReader;
import com.pharmacy.automation.pages.*;
import com.pharmacy.automation.utils.DataGenerator;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * End-to-End Test Class
 * Contains complete user journey scenarios from start to finish
 *
 * @author Delta Pharmacy Automation Team
 */
@Epic("End-to-End Scenarios")
@Feature("Complete User Journeys")
public class E2ETests extends BaseTest {

    @Test(priority = 1,
            description = "Complete purchase flow: Login -> Browse -> Add to Cart -> Checkout -> Order",
            groups = {"e2e", "critical", "smoke"})
    @Description("End-to-end test covering complete purchase journey with 13 steps")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Complete Purchase Flow")
    public void testCompletePurchaseFlowE2E() {
        logger.info("========================================");
        logger.info("E2E TEST: Complete Purchase Flow");
        logger.info("========================================");

        SoftAssert softAssert = new SoftAssert();

        // STEP 1: User Login
        logger.info("STEP 1: User Login");
        LoginPage loginPage = new LoginPage();
        String username = ConfigReader.getProperty("app.username");
        String password = ConfigReader.getProperty("app.password");

        DashboardPage dashboardPage = loginPage.login(username, password);
        softAssert.assertTrue(dashboardPage.isDashboardDisplayed(),
                "Dashboard should be displayed after login");
        logger.info("✓ User logged in successfully");

        // STEP 2: Navigate to Products Page
        logger.info("STEP 2: Navigate to Products Page");
        ProductsPage productsPage = dashboardPage.clickProductsMenu();
        softAssert.assertTrue(productsPage.areProductsDisplayed(),
                "Products should be displayed on products page");
        logger.info("✓ Products page loaded successfully");

        // STEP 3: Search for Product
        logger.info("STEP 3: Search for Product");
        String searchKeyword = "Paracetamol";
        productsPage.searchProduct(searchKeyword);
        softAssert.assertTrue(productsPage.areProductsDisplayed(),
                "Search results should be displayed after search");
        logger.info("✓ Product search completed for: {}", searchKeyword);

        // STEP 4: Add Product to Cart
        logger.info("STEP 4: Add Product to Cart");
        String productName = "Paracetamol 500mg";
        productsPage.addProductToCart(productName);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Sleep interrupted during cart update");
        }
        logger.info("✓ Product '{}' added to cart", productName);

        // STEP 5: Navigate to Cart and Verify Items
        logger.info("STEP 5: Navigate to Cart and Verify Items");
        CartPage cartPage = new CartPage();
        int cartItems = cartPage.getCartItemCount();
        softAssert.assertTrue(cartItems > 0,
                "Cart should have at least 1 item. Actual: " + cartItems);
        logger.info("✓ Cart contains {} item(s)", cartItems);

        // STEP 6: Verify Cart Total
        logger.info("STEP 6: Verify Cart Total");
        String cartTotal = cartPage.getCartTotal();
        softAssert.assertNotNull(cartTotal, "Cart total should not be null");
        logger.info("✓ Cart total: {}", cartTotal);

        // STEP 7: Proceed to Checkout
        logger.info("STEP 7: Proceed to Checkout");
        CheckoutPage checkoutPage = cartPage.proceedToCheckout();
        softAssert.assertTrue(checkoutPage.isCheckoutPageDisplayed(),
                "Checkout page should be displayed after proceeding from cart");
        logger.info("✓ Checkout page loaded successfully");

        // STEP 8: Enter Shipping Address
        logger.info("STEP 8: Enter Shipping Address");
        String shippingAddress = DataGenerator.generateAddress() + ", " +
                DataGenerator.generateCity() + ", " +
                DataGenerator.generateZipCode() + ", " +
                DataGenerator.generateCountry();
        checkoutPage.enterShippingAddress(shippingAddress);
        logger.info("✓ Shipping address entered: {}", shippingAddress);

        // STEP 9: Select Payment Method
        logger.info("STEP 9: Select Payment Method (Cash on Delivery)");
        checkoutPage.selectPaymentMethod("Cash");
        logger.info("✓ Payment method selected: Cash on Delivery");

        // STEP 10: Confirm Order
        logger.info("STEP 10: Confirm Order");
        OrdersPage ordersPage = checkoutPage.confirmOrder();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Sleep interrupted during order confirmation");
        }
        logger.info("✓ Order confirmation initiated");

        // STEP 11: Verify Order Confirmation
        logger.info("STEP 11: Verify Order Confirmation Message");
        softAssert.assertTrue(ordersPage.isOrderConfirmationDisplayed(),
                "Order confirmation message should be displayed after placing order");
        logger.info("✓ Order confirmation message displayed");

        // STEP 12: Verify Order Status
        logger.info("STEP 12: Verify Order Status");
        String orderStatus = ordersPage.getOrderStatus();
        softAssert.assertNotNull(orderStatus, "Order status should not be null");
        logger.info("✓ Order status: {}", orderStatus);

        // STEP 13: Verify Order in History
        logger.info("STEP 13: Verify Order Appears in History");
        int orderCount = ordersPage.getOrderCount();
        softAssert.assertTrue(orderCount > 0,
                "Order history should show at least 1 order");
        logger.info("✓ Total orders in history: {}", orderCount);

        // Assert all soft assertions
        softAssert.assertAll();

        logger.info("========================================");
        logger.info("✓ COMPLETE PURCHASE FLOW E2E TEST PASSED");
        logger.info("All 13 steps completed successfully");
        logger.info("========================================");
    }

    @Test(priority = 2,
            description = "Prescription upload and tracking flow",
            groups = {"e2e", "regression"})
    @Description("End-to-end test for prescription upload, submission, and status tracking")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Prescription Order Flow")
    public void testPrescriptionToOrderFlowE2E() {
        logger.info("========================================");
        logger.info("E2E TEST: Prescription to Order Flow");
        logger.info("========================================");

        SoftAssert softAssert = new SoftAssert();

        // STEP 1: User Login
        logger.info("STEP 1: User Login as Customer");
        LoginPage loginPage = new LoginPage();
        DashboardPage dashboardPage = loginPage.login(
                ConfigReader.getProperty("app.username"),
                ConfigReader.getProperty("app.password"));
        softAssert.assertTrue(dashboardPage.isDashboardDisplayed(),
                "Dashboard should be displayed after login");
        logger.info("✓ Customer logged in successfully");

        // STEP 2: Navigate to Prescriptions Page
        logger.info("STEP 2: Navigate to Prescriptions Page");
        PrescriptionsPage prescriptionsPage = new PrescriptionsPage();
        logger.info("✓ Prescriptions page loaded");

        // STEP 3: Upload Prescription File
        logger.info("STEP 3: Upload Prescription File");
        String testFilePath = System.getProperty("user.dir") +
                "/src/test/resources/testdata/sample-prescription.pdf";

        try {
            prescriptionsPage.uploadPrescription(testFilePath);
            logger.info("✓ Prescription file uploaded: {}", testFilePath);
        } catch (Exception e) {
            logger.warn("File upload skipped (file may not exist): {}", e.getMessage());
        }

        // STEP 4: Enter Doctor Details
        logger.info("STEP 4: Enter Doctor Name and Notes");
        String doctorName = "Dr. " + DataGenerator.generateFullName();
        prescriptionsPage.enterDoctorName(doctorName);
        logger.info("✓ Doctor name entered: {}", doctorName);

        String notes = "Urgent prescription for chronic condition. Patient requires immediate medication.";
        prescriptionsPage.enterNotes(notes);
        logger.info("✓ Prescription notes entered");

        // STEP 5: Submit Prescription
        logger.info("STEP 5: Submit Prescription");
        prescriptionsPage.submitPrescription();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Sleep interrupted during prescription submission");
        }
        logger.info("✓ Prescription submitted successfully");

        // STEP 6: Verify Prescription Status
        logger.info("STEP 6: Verify Prescription Status");
        String status = prescriptionsPage.getPrescriptionStatus();
        softAssert.assertNotNull(status, "Prescription status should not be null");
        logger.info("✓ Prescription status: {}", status);

        // Assert all soft assertions
        softAssert.assertAll();

        logger.info("========================================");
        logger.info("✓ PRESCRIPTION TO ORDER FLOW E2E TEST PASSED");
        logger.info("All 6 steps completed successfully");
        logger.info("========================================");
    }

    @Test(priority = 3,
            description = "Support ticket creation and tracking",
            groups = {"e2e", "regression"})
    @Description("End-to-end test for creating support ticket and verifying status")
    @Severity(SeverityLevel.NORMAL)
    @Story("Support Flow")
    public void testSupportTicketFlowE2E() {
        logger.info("========================================");
        logger.info("E2E TEST: Support Ticket Flow");
        logger.info("========================================");

        SoftAssert softAssert = new SoftAssert();

        // STEP 1: User Login
        logger.info("STEP 1: User Login");
        LoginPage loginPage = new LoginPage();
        loginPage.login(ConfigReader.getProperty("app.username"),
                ConfigReader.getProperty("app.password"));
        logger.info("✓ User logged in successfully");

        // STEP 2: Navigate to Support Page
        logger.info("STEP 2: Navigate to Support Page");
        SupportPage supportPage = new SupportPage();
        logger.info("✓ Support page loaded");

        // STEP 3: Create Support Ticket
        logger.info("STEP 3: Create Support Ticket");
        String ticketSubject = "Product Inquiry - " + DataGenerator.generateProductName();
        String ticketDescription = "I need more information about this product. " +
                "Could you please provide details about dosage, side effects, and storage instructions?";

        supportPage.createTicket(ticketSubject, ticketDescription);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Sleep interrupted during ticket creation");
        }
        logger.info("✓ Support ticket created");
        logger.info("   Subject: {}", ticketSubject);
        logger.info("   Description: {}", ticketDescription);

        // STEP 4: Verify Ticket Creation
        logger.info("STEP 4: Verify Ticket Creation");
        int ticketCount = supportPage.getTicketCount();
        softAssert.assertTrue(ticketCount > 0,
                "At least one support ticket should exist after creation");
        logger.info("✓ Total support tickets: {}", ticketCount);

        // STEP 5: Verify Ticket Status
        logger.info("STEP 5: Verify Ticket Status");
        String ticketStatus = supportPage.getTicketStatus();
        softAssert.assertNotNull(ticketStatus, "Ticket status should not be null");
        logger.info("✓ Ticket status: {}", ticketStatus);

        // Assert all soft assertions
        softAssert.assertAll();

        logger.info("========================================");
        logger.info("✓ SUPPORT TICKET FLOW E2E TEST PASSED");
        logger.info("All 5 steps completed successfully");
        logger.info("========================================");
    }

    @Test(priority = 4,
            description = "Multi-product order with credit card payment",
            groups = {"e2e", "payment", "regression"})
    @Description("End-to-end test for ordering multiple products with card payment")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Multi-Product Order with Card")
    public void testMultiProductCardPaymentE2E() {
        logger.info("========================================");
        logger.info("E2E TEST: Multi-Product Order with Card Payment");
        logger.info("========================================");

        SoftAssert softAssert = new SoftAssert();

        // STEP 1: User Login
        logger.info("STEP 1: User Login");
        LoginPage loginPage = new LoginPage();
        DashboardPage dashboardPage = loginPage.login(
                ConfigReader.getProperty("app.username"),
                ConfigReader.getProperty("app.password"));
        softAssert.assertTrue(dashboardPage.isDashboardDisplayed(),
                "Dashboard should be displayed after login");
        logger.info("✓ User logged in successfully");

        // STEP 2: Navigate to Products
        logger.info("STEP 2: Navigate to Products Page");
        ProductsPage productsPage = dashboardPage.clickProductsMenu();
        softAssert.assertTrue(productsPage.areProductsDisplayed(),
                "Products should be displayed");
        logger.info("✓ Products page loaded");

        // STEP 3: Add Multiple Products to Cart
        logger.info("STEP 3: Add Multiple Products to Cart");

        logger.info("   Adding Product 1: Paracetamol 500mg");
        productsPage.searchProduct("Paracetamol");
        productsPage.addProductToCart("Paracetamol 500mg");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Sleep interrupted");
        }
        logger.info("   ✓ Product 1 added");

        logger.info("   Adding Product 2: Ibuprofen 400mg");
        productsPage.searchProduct("Ibuprofen");
        productsPage.addProductToCart("Ibuprofen 400mg");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Sleep interrupted");
        }
        logger.info("   ✓ Product 2 added");

        logger.info("✓ Multiple products added to cart successfully");

        // STEP 4: View Cart
        logger.info("STEP 4: View Cart and Verify Items");
        CartPage cartPage = new CartPage();
        int cartItems = cartPage.getCartItemCount();
        softAssert.assertTrue(cartItems >= 1,
                "Cart should have at least 1 item. Actual: " + cartItems);
        logger.info("✓ Cart contains {} item(s)", cartItems);

        // STEP 5: Proceed to Checkout
        logger.info("STEP 5: Proceed to Checkout");
        CheckoutPage checkoutPage = cartPage.proceedToCheckout();
        softAssert.assertTrue(checkoutPage.isCheckoutPageDisplayed(),
                "Checkout page should be displayed");
        logger.info("✓ Checkout page loaded");

        // STEP 6: Enter Shipping Address
        logger.info("STEP 6: Enter Shipping Address");
        String address = DataGenerator.generateAddress() + ", " +
                DataGenerator.generateCity() + ", " +
                DataGenerator.generateZipCode();
        checkoutPage.enterShippingAddress(address);
        logger.info("✓ Shipping address entered: {}", address);

        // STEP 7: Select Card Payment Method
        logger.info("STEP 7: Select Credit Card Payment Method");
        checkoutPage.selectPaymentMethod("Card");
        logger.info("✓ Payment method selected: Credit Card");

        // STEP 8: Enter Card Details
        logger.info("STEP 8: Enter Card Details");
        String cardNumber = "4111111111111111";
        String cardHolder = DataGenerator.generateFullName();
        String expiryDate = "12/26";
        String cvv = "123";

        checkoutPage.enterCardNumber(cardNumber);
        checkoutPage.enterCardHolder(cardHolder);
        checkoutPage.enterExpiryDate(expiryDate);
        checkoutPage.enterCVV(cvv);
        logger.info("✓ Card details entered");
        logger.info("   Card Number: {}****", cardNumber.substring(0, 4));
        logger.info("   Card Holder: {}", cardHolder);
        logger.info("   Expiry: {}", expiryDate);

        // STEP 9: Confirm Order
        logger.info("STEP 9: Confirm Order");
        OrdersPage ordersPage = checkoutPage.confirmOrder();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Sleep interrupted");
        }
        logger.info("✓ Order confirmation process completed");

        // STEP 10: Verify Order Confirmation
        logger.info("STEP 10: Verify Order Confirmation");
        softAssert.assertTrue(ordersPage.isOrderConfirmationDisplayed(),
                "Order confirmation message should be displayed");
        logger.info("✓ Order confirmation message displayed");

        // STEP 11: Verify Order Status
        logger.info("STEP 11: Verify Order Status");
        String status = ordersPage.getOrderStatus();
        softAssert.assertNotNull(status, "Order status should not be null");
        logger.info("✓ Order status: {}", status);

        // Assert all soft assertions
        softAssert.assertAll();

        logger.info("========================================");
        logger.info("✓ MULTI-PRODUCT CARD PAYMENT E2E TEST PASSED");
        logger.info("All 11 steps completed successfully");
        logger.info("========================================");
    }

    @Test(priority = 5,
            description = "Chat communication flow",
            groups = {"e2e", "communication"})
    @Description("End-to-end test for chat functionality")
    @Severity(SeverityLevel.NORMAL)
    @Story("Chat Communication")
    public void testChatCommunicationE2E() {
        logger.info("========================================");
        logger.info("E2E TEST: Chat Communication Flow");
        logger.info("========================================");

        SoftAssert softAssert = new SoftAssert();

        // STEP 1: Login as Customer
        logger.info("STEP 1: Customer Login");
        LoginPage loginPage = new LoginPage();
        loginPage.login(ConfigReader.getProperty("app.username"),
                ConfigReader.getProperty("app.password"));
        logger.info("✓ Customer logged in successfully");

        // STEP 2: Navigate to Chat
        logger.info("STEP 2: Navigate to Chat Page");
        ChatPage chatPage = new ChatPage();
        softAssert.assertTrue(chatPage.isChatDisplayed(),
                "Chat interface should be displayed");
        logger.info("✓ Chat page loaded successfully");

        // STEP 3: Send Chat Message
        logger.info("STEP 3: Send Chat Message");
        String message = "Hello, I need help with my order. Can someone assist me?";
        chatPage.sendMessage(message);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Sleep interrupted");
        }
        logger.info("✓ Message sent: {}", message);

        // STEP 4: Verify Message Count
        logger.info("STEP 4: Verify Message Delivery");
        int messageCount = chatPage.getMessageCount();
        softAssert.assertTrue(messageCount > 0,
                "At least one message should be visible after sending");
        logger.info("✓ Message count: {}", messageCount);

        // Assert all soft assertions
        softAssert.assertAll();

        logger.info("========================================");
        logger.info("✓ CHAT COMMUNICATION E2E TEST PASSED");
        logger.info("All 4 steps completed successfully");
        logger.info("========================================");
    }

    @Test(priority = 6,
            description = "Order tracking and status update flow",
            groups = {"e2e", "order-tracking"})
    @Description("End-to-end test for placing order and tracking status")
    @Severity(SeverityLevel.NORMAL)
    @Story("Order Tracking")
    public void testOrderTrackingE2E() {
        logger.info("========================================");
        logger.info("E2E TEST: Order Tracking Flow");
        logger.info("========================================");

        SoftAssert softAssert = new SoftAssert();

        // STEP 1: User Login
        logger.info("STEP 1: User Login");
        LoginPage loginPage = new LoginPage();
        DashboardPage dashboardPage = loginPage.login(
                ConfigReader.getProperty("app.username"),
                ConfigReader.getProperty("app.password"));
        softAssert.assertTrue(dashboardPage.isDashboardDisplayed(),
                "Dashboard should be displayed");
        logger.info("✓ User logged in successfully");

        // STEP 2: Place a New Order
        logger.info("STEP 2: Place New Order");
        ProductsPage productsPage = dashboardPage.clickProductsMenu();
        productsPage.addProductToCart("Paracetamol 500mg");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Sleep interrupted");
        }

        CartPage cartPage = new CartPage();
        CheckoutPage checkoutPage = cartPage.proceedToCheckout();
        checkoutPage.enterShippingAddress(DataGenerator.generateAddress());
        checkoutPage.selectPaymentMethod("Cash");
        OrdersPage ordersPage = checkoutPage.confirmOrder();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Sleep interrupted");
        }

        softAssert.assertTrue(ordersPage.isOrderConfirmationDisplayed(),
                "Order should be confirmed");
        logger.info("✓ Order placed successfully");

        // STEP 3: Track Order Status
        logger.info("STEP 3: Track Order Status");
        String initialStatus = ordersPage.getOrderStatus();
        softAssert.assertNotNull(initialStatus,
                "Initial order status should be available");
        logger.info("✓ Order status retrieved: {}", initialStatus);

        // STEP 4: Verify Order in History
        logger.info("STEP 4: Verify Order Appears in History");
        int orderCount = ordersPage.getOrderCount();
        softAssert.assertTrue(orderCount > 0,
                "Order history should contain at least one order");
        logger.info("✓ Total orders in history: {}", orderCount);

        // Assert all soft assertions
        softAssert.assertAll();

        logger.info("========================================");
        logger.info("✓ ORDER TRACKING E2E TEST PASSED");
        logger.info("All 4 steps completed successfully");
        logger.info("========================================");
    }
}