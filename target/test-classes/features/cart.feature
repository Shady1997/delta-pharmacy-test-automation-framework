# 3. cart.feature
@regression
Feature: Shopping Cart
  As a customer
  I want to manage my shopping cart
  So that I can review before purchase

  Background:
    Given I am on the login page
    When I login with valid credentials
    And I am on the products page
    And I add "Paracetamol 500mg" to cart

  @smoke @positive
  Scenario: View cart with items
    Then my cart should contain 1 items

  @positive
  Scenario: Proceed to checkout from cart
    When I proceed to checkout
    Then I should see checkout page

  @positive
  Scenario: View cart total
    Then cart total should be displayed
