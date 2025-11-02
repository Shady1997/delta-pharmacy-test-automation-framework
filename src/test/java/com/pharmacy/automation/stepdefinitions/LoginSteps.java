// 2. LoginSteps.java
package com.pharmacy.automation.stepdefinitions;

import com.pharmacy.automation.config.ConfigReader;
import com.pharmacy.automation.pages.DashboardPage;
import com.pharmacy.automation.pages.LoginPage;
import io.cucumber.java.en.*;
import org.testng.Assert;

public class LoginSteps {
    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        loginPage = new LoginPage();
    }

    @When("I login with username {string} and password {string}")
    public void iLoginWithUsernameAndPassword(String username, String password) {
        dashboardPage = loginPage.login(username, password);
    }

    @When("I login with valid credentials")
    public void iLoginWithValidCredentials() {
        String username = ConfigReader.getProperty("app.username");
        String password = ConfigReader.getProperty("app.password");
        dashboardPage = loginPage.login(username, password);
    }

    @Then("I should see the dashboard")
    public void iShouldSeeTheDashboard() {
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(),
                "Dashboard should be displayed");
    }

    @Then("I should see an error message")
    public void iShouldSeeAnErrorMessage() {
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Error message should be displayed");
    }
}
