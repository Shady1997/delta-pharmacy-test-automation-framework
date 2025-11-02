// 9. SupportSteps.java
package com.pharmacy.automation.stepdefinitions;

import com.pharmacy.automation.pages.SupportPage;
import io.cucumber.java.en.*;
import org.testng.Assert;

public class SupportSteps {
    private SupportPage supportPage;

    @Given("I am on support page")
    public void iAmOnSupportPage() {
        supportPage = new SupportPage();
    }

    @When("I create ticket with subject {string} and description {string}")
    public void iCreateTicket(String subject, String description) {
        supportPage.createTicket(subject, description);
    }

    @Then("ticket should be created")
    public void ticketShouldBeCreated() {
        Assert.assertTrue(supportPage.getTicketCount() > 0,
                "Ticket should be created");
    }

    @Then("ticket status should be {string}")
    public void ticketStatusShouldBe(String expectedStatus) {
        String actualStatus = supportPage.getTicketStatus();
        Assert.assertTrue(actualStatus.contains(expectedStatus),
                "Ticket status should be " + expectedStatus);
    }
}
