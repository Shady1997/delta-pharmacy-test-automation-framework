# 10. e2e/prescription_flow.feature
@e2e
Feature: Prescription Upload to Order
  As a customer
  I want to upload prescription and order prescribed items
  So that I can get my medications

  Scenario: Complete prescription flow
    Given I am on the login page
    When I login with valid credentials
    And I am on prescriptions page
    And I upload prescription file "sample-prescription.pdf"
    And I enter doctor name "Dr. Mohamed Ali"
    And I submit the prescription
    Then prescription status should be "PENDING"
