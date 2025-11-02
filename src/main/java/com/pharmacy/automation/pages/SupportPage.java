// 9. SupportPage.java
package com.pharmacy.automation.pages;

import com.pharmacy.automation.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class SupportPage extends BasePage {

    private final By supportTitle = By.xpath("//h1[contains(text(),'Support')]");
    private final By createTicketButton = By.xpath("//button[contains(text(),'Create Ticket')]");
    private final By subjectField = By.cssSelector("input[placeholder*='Subject']");
    private final By descriptionField = By.cssSelector("textarea[placeholder*='Description']");
    private final By priorityDropdown = By.cssSelector("select[name*='priority']");
    private final By submitTicketButton = By.xpath("//button[contains(text(),'Submit')]");
    private final By ticketCards = By.cssSelector(".ticket-card, [class*='ticket']");
    private final By ticketStatus = By.cssSelector(".ticket-status, [class*='status']");

    @Step("Create support ticket with subject: {subject}")
    public void createTicket(String subject, String description) {
        elementHelper.click(createTicketButton);
        elementHelper.sendKeys(subjectField, subject);
        elementHelper.sendKeys(descriptionField, description);
        elementHelper.click(submitTicketButton);
    }

    @Step("Get ticket count")
    public int getTicketCount() {
        return elementHelper.findElements(ticketCards).size();
    }

    @Step("Get ticket status")
    public String getTicketStatus() {
        return elementHelper.getText(ticketStatus);
    }
}