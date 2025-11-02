// 3. SanityTestRunner.java
package com.pharmacy.automation.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.pharmacy.automation.stepdefinitions"},
        tags = "@sanity",
        plugin = {
                "pretty",
                "html:target/cucumber-reports/sanity-report.html",
                "json:target/cucumber-reports/sanity-report.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true
)
public class SanityTestRunner extends AbstractTestNGCucumberTests {
}
