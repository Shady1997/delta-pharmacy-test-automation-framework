# 5. orders.feature
@regression
Feature: Order Management
  As a customer
  I want to view and manage my orders
  So that I can track my purchases

  Background:
    Given I am on the login page
    When I login with valid credentials

  @smoke @positive
  Scenario: View order history
    Given I am on orders page
    Then I should have 1 orders

  @positive
  Scenario: Check order status
    Given I am on orders page
    Then order status should be "PENDING"

  @positive
  Scenario: Cancel an order
    Given I am on orders page
    When I cancel the order
    Then order status should be "CANCELLED"
