# 6. prescriptions.feature
@regression
Feature: Prescription Management
  As a customer
  I want to upload and manage prescriptions
  So that I can get prescribed medications

  Background:
    Given I am on the login page
    When I login with valid credentials
    And I am on prescriptions page

  @positive
  Scenario: Upload prescription
    When I upload prescription file "sample-prescription.pdf"
    And I enter doctor name "Dr. Ahmed Hassan"
    And I submit the prescription
    Then prescription status should be "PENDING"

  @positive
  Scenario: Pharmacist approves prescription
    Given I am on the login page
    When I login with username "pharmacist@pharmacy.com" and password "pharma123"
    And I am on prescriptions page
    When pharmacist approves the prescription
    Then prescription status should be "APPROVED"
