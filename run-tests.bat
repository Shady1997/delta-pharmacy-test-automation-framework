@echo off
REM ============================================
REM Delta Pharmacy Test Automation Runner
REM Windows Batch Script
REM ============================================

echo.
echo ========================================
echo Delta Pharmacy Test Automation
echo ========================================
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven from https://maven.apache.org/download.cgi
    exit /b 1
)

REM Check if Java is installed
where java >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install JDK 17+ from https://www.oracle.com/java/technologies/downloads/
    exit /b 1
)

REM Display Java and Maven versions
echo Maven Version:
call mvn -version | findstr "Apache Maven"
echo.
echo Java Version:
call java -version 2>&1 | findstr "version"
echo.

REM Get test suite parameter
set TEST_SUITE=%1
set BROWSER=%2
set ENVIRONMENT=%3

REM Set defaults if not provided
if "%BROWSER%"=="" set BROWSER=chrome
if "%ENVIRONMENT%"=="" set ENVIRONMENT=testing

echo Test Suite: %TEST_SUITE%
echo Browser: %BROWSER%
echo Environment: %ENVIRONMENT%
echo.

REM Execute tests based on suite parameter
if "%TEST_SUITE%"=="smoke" (
    echo Running Smoke Tests...
    call mvn clean test -DsuiteXmlFile=testng-suites/smoke-tests.xml -Dbrowser=%BROWSER% -Denv=%ENVIRONMENT%
) else if "%TEST_SUITE%"=="regression" (
    echo Running Regression Tests...
    call mvn clean test -DsuiteXmlFile=testng-suites/regression-tests.xml -Dbrowser=%BROWSER% -Denv=%ENVIRONMENT%
) else if "%TEST_SUITE%"=="sanity" (
    echo Running Sanity Tests...
    call mvn clean test -DsuiteXmlFile=testng-suites/sanity-tests.xml -Dbrowser=%BROWSER% -Denv=%ENVIRONMENT%
) else if "%TEST_SUITE%"=="e2e" (
    echo Running End-to-End Tests...
    call mvn clean test -DsuiteXmlFile=testng-suites/e2e-tests.xml -Dbrowser=%BROWSER% -Denv=%ENVIRONMENT%
) else if "%TEST_SUITE%"=="parallel" (
    echo Running Parallel Tests on All Browsers...
    call mvn clean test -DsuiteXmlFile=testng-suites/testng.xml -Denv=%ENVIRONMENT%
) else if "%TEST_SUITE%"=="all" (
    echo Running All Tests...
    call mvn clean test -Dbrowser=%BROWSER% -Denv=%ENVIRONMENT%
) else (
    echo.
    echo ========================================
    echo USAGE:
    echo ========================================
    echo run-tests.bat [suite] [browser] [environment]
    echo.
    echo Suite Options:
    echo   smoke       - Run smoke tests (15 min)
    echo   sanity      - Run sanity tests (30 min)
    echo   regression  - Run regression tests (4-6 hours)
    echo   e2e         - Run end-to-end tests (2-3 hours)
    echo   parallel    - Run parallel tests on all browsers
    echo   all         - Run all tests
    echo.
    echo Browser Options:
    echo   chrome      - Google Chrome (default)
    echo   firefox     - Mozilla Firefox
    echo   edge        - Microsoft Edge
    echo.
    echo Environment Options:
    echo   testing     - Testing environment (default)
    echo   staging     - Staging environment
    echo   production  - Production environment
    echo.
    echo Examples:
    echo   run-tests.bat smoke
    echo   run-tests.bat regression chrome testing
    echo   run-tests.bat e2e firefox staging
    echo   run-tests.bat parallel - testing
    echo.
    exit /b 1
)

REM Check test execution result
if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Tests Completed Successfully!
    echo ========================================
    echo.
    echo Reports Generated:
    echo   - Extent Report: test-output\reports\ExtentReport.html
    echo   - TestNG Report: test-output\index.html
    echo   - Allure Results: target\allure-results
    echo.
    
    REM Prompt to generate Allure report
    set /p GENERATE_ALLURE="Do you want to generate Allure report? (Y/N): "
    if /i "%GENERATE_ALLURE%"=="Y" (
        echo.
        echo Generating Allure Report...
        call mvn allure:serve
    )
) else (
    echo.
    echo ========================================
    echo Tests Failed!
    echo ========================================
    echo.
    echo Check the following for details:
    echo   - Console output above
    echo   - test-output\screenshots for failure screenshots
    echo   - test-output\videos for test recordings
    echo   - logs\automation.log for detailed logs
    echo.
)

echo.
echo Press any key to exit...
pause >nul