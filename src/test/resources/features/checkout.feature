# 4. checkout.feature
@regression
Feature: Checkout Process
  As a customer
  I want to complete checkout
  So that I can place my order

  Background:
    Given I am on the login page
    When I login with valid credentials
    And I am on the products page
    And I add "Paracetamol 500mg" to cart
    And I have 1 items in cart

  @smoke @positive
  Scenario: Checkout with cash on delivery
    When I proceed to checkout
    And I enter shipping address "123 Test St, Cairo, Egypt"
    And I select payment method "Cash"
    And I confirm the order
    Then I should see order confirmation

  @positive
  Scenario: Checkout with credit card
    When I proceed to checkout
    And I enter shipping address "456 Test Ave, Alexandria, Egypt"
    And I select payment method "Card"
    And I enter card number "4111111111111111"
    And I enter card holder "Test User"
    And I enter expiry date "12/26"
    And I enter CVV "123"
    And I confirm the order
    Then I should see order confirmation
