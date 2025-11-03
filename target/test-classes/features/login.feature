# 1. login.feature
@smoke @regression
Feature: User Login
  As a user
  I want to login to the application
  So that I can access my account

  Background:
    Given I am on the login page

  @positive
  Scenario: Successful login with valid credentials
    When I login with valid credentials
    Then I should see the dashboard

  @negative
  Scenario: Failed login with invalid credentials
    When I login with username "invalid@email.com" and password "wrongpass"
    Then I should see an error message

  @negative
  Scenario Outline: Login with different invalid credentials
    When I login with username "<username>" and password "<password>"
    Then I should see an error message

    Examples:
      | username          | password    |
      | invalid@test.com  | wrong123    |
      | test@test.com     | incorrectPW |
      | admin@wrong.com   | admin       |