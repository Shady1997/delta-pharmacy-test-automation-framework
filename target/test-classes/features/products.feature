# 2. products.feature
@regression
Feature: Product Management
  As a customer
  I want to browse and search products
  So that I can find what I need

  Background:
    Given I am on the login page
    When I login with valid credentials
    And I am on the products page

  @smoke @positive
  Scenario: View all products
    Then products should be displayed

  @positive
  Scenario: Search for a product
    When I search for "Paracetamol"
    Then products should be displayed

  @positive
  Scenario: Add product to cart
    When I add "Paracetamol 500mg" to cart
    Then my cart should contain 1 items

  @positive
  Scenario: Check product stock
    Then "Paracetamol 500mg" should be in stock
