# run-tests.sh (Linux/Mac)
#!/bin/bash

echo "========================================"
echo "Delta Pharmacy Test Automation"
echo "========================================"
echo ""

# Check Maven installation
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven is not installed"
    exit 1
fi

# Check Java installation
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed"
    exit 1
fi

# Display versions
echo "Maven Version:"
mvn -version | grep "Apache Maven"
echo ""
echo "Java Version:"
java -version 2>&1 | grep "version"
echo ""

# Get parameters
TEST_SUITE=${1:-"smoke"}
BROWSER=${2:-"chrome"}
ENVIRONMENT=${3:-"testing"}

echo "Test Suite: $TEST_SUITE"
echo "Browser: $BROWSER"
echo "Environment: $ENVIRONMENT"
echo ""

# Execute tests
case $TEST_SUITE in
    smoke)
        echo "Running Smoke Tests..."
        mvn clean test -DsuiteXmlFile=testng-suites/smoke-tests.xml -Dbrowser=$BROWSER -Denv=$ENVIRONMENT
        ;;
    regression)
        echo "Running Regression Tests..."
        mvn clean test -DsuiteXmlFile=testng-suites/regression-tests.xml -Dbrowser=$BROWSER -Denv=$ENVIRONMENT
        ;;
    sanity)
        echo "Running Sanity Tests..."
        mvn clean test -DsuiteXmlFile=testng-suites/sanity-tests.xml -Dbrowser=$BROWSER -Denv=$ENVIRONMENT
        ;;
    e2e)
        echo "Running E2E Tests..."
        mvn clean test -DsuiteXmlFile=testng-suites/e2e-tests.xml -Dbrowser=$BROWSER -Denv=$ENVIRONMENT
        ;;
    all)
        echo "Running All Tests..."
        mvn clean test -Dbrowser=$BROWSER -Denv=$ENVIRONMENT
        ;;
    *)
        echo "Usage: ./run-tests.sh [suite] [browser] [environment]"
        echo ""
        echo "Suite Options: smoke, sanity, regression, e2e, all"
        echo "Browser Options: chrome, firefox, edge"
        echo "Environment Options: testing, staging, production"
        exit 1
        ;;
esac

# Check result
if [ $? -eq 0 ]; then
    echo ""
    echo "========================================"
    echo "Tests Completed Successfully!"
    echo "========================================"
    echo ""
    echo "Reports:"
    echo "  - Extent: test-output/reports/ExtentReport.html"
    echo "  - TestNG: test-output/index.html"
    echo "  - Allure: target/allure-results"
    echo ""
    read -p "Generate Allure report? (y/n): " GENERATE
    if [ "$GENERATE" = "y" ] || [ "$GENERATE" = "Y" ]; then
        mvn allure:serve
    fi
else
    echo ""
    echo "========================================"
    echo "Tests Failed!"
    echo "========================================"
    echo "Check logs for details"
fi