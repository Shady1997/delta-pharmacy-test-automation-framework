// 7. PrescriptionsPage.java
package com.pharmacy.automation.pages;

import com.pharmacy.automation.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class PrescriptionsPage extends BasePage {

    private final By prescriptionsTitle = By.xpath("//h1[contains(text(),'Prescription')]");
    private final By uploadButton = By.xpath("//button[contains(text(),'Upload')]");
    private final By fileInput = By.cssSelector("input[type='file']");
    private final By doctorNameField = By.cssSelector("input[placeholder*='Doctor']");
    private final By notesField = By.cssSelector("textarea[placeholder*='Notes']");
    private final By submitButton = By.xpath("//button[contains(text(),'Submit')]");
    private final By prescriptionCards = By.cssSelector(".prescription-card, [class*='prescription']");
    private final By prescriptionStatus = By.cssSelector(".prescription-status, [class*='status']");
    private final By approveButton = By.xpath("//button[contains(text(),'Approve')]");
    private final By rejectButton = By.xpath("//button[contains(text(),'Reject')]");

    @Step("Upload prescription file: {filePath}")
    public void uploadPrescription(String filePath) {
        elementHelper.sendKeys(fileInput, filePath);
    }

    @Step("Enter doctor name: {doctorName}")
    public void enterDoctorName(String doctorName) {
        elementHelper.sendKeys(doctorNameField, doctorName);
    }

    @Step("Enter notes: {notes}")
    public void enterNotes(String notes) {
        elementHelper.sendKeys(notesField, notes);
    }

    @Step("Submit prescription")
    public void submitPrescription() {
        elementHelper.click(submitButton);
    }

    @Step("Get prescription status")
    public String getPrescriptionStatus() {
        return elementHelper.getText(prescriptionStatus);
    }

    @Step("Approve prescription")
    public void approvePrescription() {
        elementHelper.click(approveButton);
    }

    @Step("Reject prescription")
    public void rejectPrescription() {
        elementHelper.click(rejectButton);
    }
}
