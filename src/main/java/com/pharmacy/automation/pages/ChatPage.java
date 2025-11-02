// 8. ChatPage.java
package com.pharmacy.automation.pages;

import com.pharmacy.automation.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class ChatPage extends BasePage {

    private final By chatTitle = By.xpath("//h1[contains(text(),'Chat')]");
    private final By messageInput = By.cssSelector("input[placeholder*='message'], textarea[placeholder*='message']");
    private final By sendButton = By.xpath("//button[contains(text(),'Send')]");
    private final By chatMessages = By.cssSelector(".chat-message, [class*='message']");
    private final By userList = By.cssSelector(".user-list, [class*='conversation']");

    @Step("Send chat message: {message}")
    public void sendMessage(String message) {
        elementHelper.sendKeys(messageInput, message);
        elementHelper.click(sendButton);
    }

    @Step("Get message count")
    public int getMessageCount() {
        return elementHelper.findElements(chatMessages).size();
    }

    @Step("Check if chat is displayed")
    public boolean isChatDisplayed() {
        return elementHelper.isElementDisplayed(chatTitle);
    }
}
