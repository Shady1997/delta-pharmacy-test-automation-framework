# 8. support.feature
@sanity
Feature: Support Tickets
  As a customer
  I want to create support tickets
  So that I can report issues

  Background:
    Given I am on the login page
    When I login with valid credentials
    And I am on support page

  @positive
  Scenario: Create support ticket
    When I create ticket with subject "Product Issue" and description "Product not delivered"
    Then ticket should be created

  @positive
  Scenario: Check ticket status
    Then ticket status should be "OPEN"
