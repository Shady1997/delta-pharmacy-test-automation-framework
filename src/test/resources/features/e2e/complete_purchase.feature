# 9. e2e/complete_purchase.feature
@e2e @critical
Feature: Complete Purchase Flow
  As a customer
  I want to complete full purchase journey
  So that I can successfully order products

  @smoke
  Scenario: Customer completes purchase with cash payment
    Given I am on the login page
    When I login with valid credentials
    And I am on the products page
    And I search for "Paracetamol"
    And I add "Paracetamol 500mg" to cart
    Then my cart should contain 1 items
    When I proceed to checkout
    And I enter shipping address "123 Main St, Cairo, 12345, Egypt"
    And I select payment method "Cash"
    And I confirm the order
    Then I should see order confirmation
    And order status should be "PENDING"

  @regression
  Scenario: Customer completes purchase with card payment
    Given I am on the login page
    When I login with valid credentials
    And I am on the products page
    And I add "Ibuprofen 400mg" to cart
    And I add "Aspirin 100mg" to cart
    Then my cart should contain 2 items
    When I proceed to checkout
    And I enter shipping address "456 Test Ave, Alexandria, Egypt"
    And I select payment method "Card"
    And I enter card number "4111111111111111"
    And I enter card holder "John Doe"
    And I enter expiry date "12/26"
    And I enter CVV "123"
    And I confirm the order
    Then I should see order confirmation
