// 10. NotificationsPage.java
package com.pharmacy.automation.pages;

import com.pharmacy.automation.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class NotificationsPage extends BasePage {

    private final By notificationsTitle = By.xpath("//h1[contains(text(),'Notification')]");
    private final By notificationCards = By.cssSelector(".notification-card, [class*='notification']");
    private final By markAllReadButton = By.xpath("//button[contains(text(),'Mark All')]");
    private final By deleteButton = By.xpath("//button[contains(text(),'Delete')]");
    private final By noNotificationsMessage = By.xpath("//*[contains(text(),'No notifications')]");

    @Step("Get notification count")
    public int getNotificationCount() {
        return elementHelper.findElements(notificationCards).size();
    }

    @Step("Mark all as read")
    public void markAllAsRead() {
        elementHelper.click(markAllReadButton);
    }

    @Step("Check if notifications are displayed")
    public boolean areNotificationsDisplayed() {
        return elementHelper.isElementDisplayed(notificationCards);
    }
}