// 8. ChatSteps.java
package com.pharmacy.automation.stepdefinitions;

import com.pharmacy.automation.pages.ChatPage;
import io.cucumber.java.en.*;
import org.testng.Assert;

public class ChatSteps {
    private ChatPage chatPage;

    @Given("I am on chat page")
    public void iAmOnChatPage() {
        chatPage = new ChatPage();
    }

    @When("I send message {string}")
    public void iSendMessage(String message) {
        chatPage.sendMessage(message);
    }

    @Then("chat should be displayed")
    public void chatShouldBeDisplayed() {
        Assert.assertTrue(chatPage.isChatDisplayed(),
                "Chat should be displayed");
    }

    @Then("I should see {int} messages")
    public void iShouldSeeMessages(int count) {
        Assert.assertEquals(chatPage.getMessageCount(), count,
                "Should see " + count + " messages");
    }
}