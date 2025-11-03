# 11. e2e/support_ticket.feature
@e2e
Feature: Support Ticket Resolution
  As a customer
  I want to create and track support ticket
  So that my issues are resolved

  Scenario: Create and track support ticket
    Given I am on the login page
    When I login with valid credentials
    And I am on support page
    And I create ticket with subject "Delivery Delay" and description "My order is delayed"
    Then ticket should be created
    And ticket status should be "OPEN"