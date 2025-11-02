# 7. chat.feature
@sanity
Feature: Chat Communication
  As a customer
  I want to chat with pharmacy staff
  So that I can get help

  Background:
    Given I am on the login page
    When I login with valid credentials
    And I am on chat page

  @positive
  Scenario: View chat interface
    Then chat should be displayed

  @positive
  Scenario: Send a message
    When I send message "Hello, I need help"
    Then I should see 1 messages