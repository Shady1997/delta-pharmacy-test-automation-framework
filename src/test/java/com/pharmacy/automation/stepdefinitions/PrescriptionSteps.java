// 7. PrescriptionSteps.java
package com.pharmacy.automation.stepdefinitions;

import com.pharmacy.automation.pages.PrescriptionsPage;
import io.cucumber.java.en.*;
import org.testng.Assert;

public class PrescriptionSteps {
    private PrescriptionsPage prescriptionsPage;

    @Given("I am on prescriptions page")
    public void iAmOnPrescriptionsPage() {
        prescriptionsPage = new PrescriptionsPage();
    }

    @When("I upload prescription file {string}")
    public void iUploadPrescriptionFile(String filePath) {
        prescriptionsPage.uploadPrescription(filePath);
    }

    @When("I enter doctor name {string}")
    public void iEnterDoctorName(String doctorName) {
        prescriptionsPage.enterDoctorName(doctorName);
    }

    @When("I submit the prescription")
    public void iSubmitThePrescription() {
        prescriptionsPage.submitPrescription();
    }

    @When("pharmacist approves the prescription")
    public void pharmacistApprovesThePrescription() {
        prescriptionsPage.approvePrescription();
    }

    @Then("prescription status should be {string}")
    public void prescriptionStatusShouldBe(String expectedStatus) {
        String actualStatus = prescriptionsPage.getPrescriptionStatus();
        Assert.assertTrue(actualStatus.contains(expectedStatus),
                "Prescription status should be " + expectedStatus);
    }
}