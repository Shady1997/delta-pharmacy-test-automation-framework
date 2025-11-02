// 4. E2ETestRunner.java
package com.pharmacy.automation.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features/e2e",
        glue = {"com.pharmacy.automation.stepdefinitions"},
        tags = "@e2e",
        plugin = {
                "pretty",
                "html:target/cucumber-reports/e2e-report.html",
                "json:target/cucumber-reports/e2e-report.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true
)
public class E2ETestRunner extends AbstractTestNGCucumberTests {
}
