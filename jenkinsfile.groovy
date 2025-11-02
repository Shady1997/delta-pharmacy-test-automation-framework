#!/usr/bin/env groovy

/**
 * Delta Pharmacy Test Automation - Jenkins Pipeline
 * Groovy Pipeline for running Selenium tests
 *
 * @author Delta Pharmacy Automation Team
 * @version 1.0.0
 */

pipeline {
    agent any

    // Build parameters
    parameters {
        choice(
                name: 'TEST_SUITE',
                choices: ['smoke', 'sanity', 'regression', 'e2e', 'all'],
                description: 'Select test suite to execute'
        )
        choice(
                name: 'BROWSER',
                choices: ['chrome', 'firefox', 'edge', 'all'],
                description: 'Select browser for test execution'
        )
        choice(
                name: 'ENVIRONMENT',
                choices: ['testing', 'staging', 'production'],
                description: 'Select target environment'
        )
        booleanParam(
                name: 'HEADLESS_MODE',
                defaultValue: true,
                description: 'Run tests in headless mode'
        )
        booleanParam(
                name: 'PARALLEL_EXECUTION',
                defaultValue: true,
                description: 'Enable parallel test execution'
        )
        booleanParam(
                name: 'VIDEO_RECORDING',
                defaultValue: true,
                description: 'Enable video recording for tests'
        )
        string(
                name: 'THREAD_COUNT',
                defaultValue: '3',
                description: 'Number of parallel threads'
        )
    }

    // Tools configuration
    tools {
        maven 'Maven-3.9'
        jdk 'JDK-17'
    }

    // Environment variables
    environment {
        MAVEN_OPTS = '-Xmx2048m -Xms1024m'
        JAVA_HOME = "${tool 'JDK-17'}"
        PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
        PROJECT_NAME = 'Delta Pharmacy Automation'
        REPORT_DIR = 'test-output/reports'
        ALLURE_RESULTS = 'target/allure-results'
    }

    // Trigger options
    triggers {
        // Poll SCM every 15 minutes
        pollSCM('H/15 * * * *')

        // Build daily at 2 AM
        cron('0 2 * * *')
    }

    options {
        // Keep last 30 builds
        buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '10'))

        // Timeout after 4 hours
        timeout(time: 4, unit: 'HOURS')

        // Timestamps in console output
        timestamps()

        // Disable concurrent builds
        disableConcurrentBuilds()

        // Skip default checkout
        skipDefaultCheckout()
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    echo "========================================="
                    echo "Stage: Checkout Source Code"
                    echo "========================================="
                    echo "Branch: ${env.BRANCH_NAME}"
                    echo "Build Number: ${env.BUILD_NUMBER}"
                    echo "Job Name: ${env.JOB_NAME}"
                }

                checkout scm

                script {
                    // Get Git commit information
                    env.GIT_COMMIT_MSG = sh(
                            script: 'git log -1 --pretty=%B',
                            returnStdout: true
                    ).trim()
                    env.GIT_AUTHOR = sh(
                            script: 'git log -1 --pretty=%an',
                            returnStdout: true
                    ).trim()

                    echo "Commit: ${env.GIT_COMMIT_MSG}"
                    echo "Author: ${env.GIT_AUTHOR}"
                }
            }
        }

        stage('Environment Setup') {
            steps {
                script {
                    echo "========================================="
                    echo "Stage: Environment Setup"
                    echo "========================================="
                    echo "Test Suite: ${params.TEST_SUITE}"
                    echo "Browser: ${params.BROWSER}"
                    echo "Environment: ${params.ENVIRONMENT}"
                    echo "Headless Mode: ${params.HEADLESS_MODE}"
                    echo "Parallel Execution: ${params.PARALLEL_EXECUTION}"
                }

                // Display Java and Maven versions
                sh 'java -version'
                sh 'mvn -version'

                // Create necessary directories
                sh '''
                    mkdir -p test-output/screenshots
                    mkdir -p test-output/videos
                    mkdir -p test-output/reports
                    mkdir -p logs
                '''
            }
        }

        stage('Clean & Install Dependencies') {
            steps {
                script {
                    echo "========================================="
                    echo "Stage: Clean & Install Dependencies"
                    echo "========================================="
                }

                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    echo "========================================="
                    echo "Stage: Execute Tests"
                    echo "========================================="

                    def suiteFile = params.TEST_SUITE == 'all' ?
                            'testng.xml' : "${params.TEST_SUITE}-tests.xml"

                    def mavenCommand = """
                        mvn test \
                        -DsuiteXmlFile=testng-suites/${suiteFile} \
                        -Dbrowser=${params.BROWSER} \
                        -Denv=${params.ENVIRONMENT} \
                        -Dheadless=${params.HEADLESS_MODE} \
                        -Dthread.count=${params.THREAD_COUNT} \
                        -Denable.video.recording=${params.VIDEO_RECORDING}
                    """

                    // Run tests and capture result
                    def testResult = sh(
                            script: mavenCommand,
                            returnStatus: true
                    )

                    // Store test result for later use
                    env.TEST_RESULT = testResult

                    if (testResult != 0) {
                        echo "⚠️ Tests failed or encountered errors"
                        currentBuild.result = 'UNSTABLE'
                    } else {
                        echo "✅ All tests passed successfully"
                    }
                }
            }
            post {
                always {
                    // Publish TestNG results
                    junit allowEmptyResults: true,
                            testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Generate Allure Report') {
            steps {
                script {
                    echo "========================================="
                    echo "Stage: Generate Allure Report"
                    echo "========================================="
                }

                sh 'mvn allure:report'

                // Publish Allure report
                allure includeProperties: false,
                        jdk: '',
                        results: [[path: env.ALLURE_RESULTS]]
            }
        }

        stage('Publish Reports') {
            steps {
                script {
                    echo "========================================="
                    echo "Stage: Publish Reports"
                    echo "========================================="
                }

                // Publish HTML reports
                publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: env.REPORT_DIR,
                        reportFiles: 'ExtentReport.html',
                        reportName: 'Extent Report',
                        reportTitles: 'Test Execution Report'
                ])

                publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'test-output',
                        reportFiles: 'index.html',
                        reportName: 'TestNG Report',
                        reportTitles: 'TestNG Results'
                ])
            }
        }

        stage('Archive Artifacts') {
            steps {
                script {
                    echo "========================================="
                    echo "Stage: Archive Artifacts"
                    echo "========================================="
                }

                // Archive test artifacts
                archiveArtifacts artifacts: '''
                    test-output/**/*,
                    target/surefire-reports/**/*,
                    target/allure-results/**/*,
                    logs/**/*
                ''', allowEmptyArchive: true, fingerprint: true
            }
        }

        stage('Test Analysis') {
            steps {
                script {
                    echo "========================================="
                    echo "Stage: Test Analysis"
                    echo "========================================="

                    // Count test results
                    def testResults = sh(
                            script: '''
                            find target/surefire-reports -name "TEST-*.xml" | \
                            xargs grep -h "<testsuite" | \
                            awk '{
                                tests += match($0, /tests="([0-9]+)"/, a) ? a[1] : 0
                                failures += match($0, /failures="([0-9]+)"/, b) ? b[1] : 0
                                errors += match($0, /errors="([0-9]+)"/, c) ? c[1] : 0
                                skipped += match($0, /skipped="([0-9]+)"/, d) ? d[1] : 0
                            }
                            END {
                                print "Total:" tests " Passed:" (tests-failures-errors-skipped) " Failed:" failures " Errors:" errors " Skipped:" skipped
                            }'
                        ''',
                            returnStdout: true
                    ).trim()

                    echo "Test Results: ${testResults}"

                    // Calculate pass percentage
                    def passPercentage = sh(
                            script: '''
                            find target/surefire-reports -name "TEST-*.xml" | \
                            xargs grep -h "<testsuite" | \
                            awk '{
                                tests += match($0, /tests="([0-9]+)"/, a) ? a[1] : 0
                                failures += match($0, /failures="([0-9]+)"/, b) ? b[1] : 0
                                errors += match($0, /errors="([0-9]+)"/, c) ? c[1] : 0
                            }
                            END {
                                if (tests > 0) {
                                    printf "%.2f", ((tests-failures-errors)/tests)*100
                                } else {
                                    print "0"
                                }
                            }'
                        ''',
                            returnStdout: true
                    ).trim()

                    echo "Pass Percentage: ${passPercentage}%"

                    // Set build description
                    currentBuild.description = """
                        ${params.TEST_SUITE} on ${params.BROWSER} (${params.ENVIRONMENT})
                        Results: ${testResults}
                        Pass Rate: ${passPercentage}%
                    """
                }
            }
        }
    }

    post {
        always {
            script {
                echo "========================================="
                echo "Pipeline Completed"
                echo "========================================="
                echo "Status: ${currentBuild.result ?: 'SUCCESS'}"
                echo "Duration: ${currentBuild.durationString}"
            }

            // Clean workspace
            cleanWs deleteDirs: true,
                    notFailBuild: true,
                    patterns: [
                            [pattern: 'target/**', type: 'INCLUDE'],
                            [pattern: '.m2/**', type: 'INCLUDE']
                    ]
        }

        success {
            script {
                echo "✅ Build completed successfully!"

                // Send success notification
                emailext(
                        subject: "✅ SUCCESS: ${env.PROJECT_NAME} - Build #${env.BUILD_NUMBER}",
                        body: """
                        <html>
                        <body>
                            <h2 style="color: green;">Build Successful ✅</h2>
                            <p><strong>Project:</strong> ${env.PROJECT_NAME}</p>
                            <p><strong>Build Number:</strong> ${env.BUILD_NUMBER}</p>
                            <p><strong>Test Suite:</strong> ${params.TEST_SUITE}</p>
                            <p><strong>Browser:</strong> ${params.BROWSER}</p>
                            <p><strong>Environment:</strong> ${params.ENVIRONMENT}</p>
                            <p><strong>Duration:</strong> ${currentBuild.durationString}</p>
                            <p><strong>Commit:</strong> ${env.GIT_COMMIT_MSG}</p>
                            <p><strong>Author:</strong> ${env.GIT_AUTHOR}</p>
                            <br>
                            <p><a href="${env.BUILD_URL}">View Build</a></p>
                            <p><a href="${env.BUILD_URL}allure">View Allure Report</a></p>
                            <p><a href="${env.BUILD_URL}Extent_20Report">View Extent Report</a></p>
                        </body>
                        </html>
                    """,
                        to: '${DEFAULT_RECIPIENTS}',
                        mimeType: 'text/html'
                )
            }
        }

        failure {
            script {
                echo "❌ Build failed!"

                // Send failure notification
                emailext(
                        subject: "❌ FAILURE: ${env.PROJECT_NAME} - Build #${env.BUILD_NUMBER}",
                        body: """
                        <html>
                        <body>
                            <h2 style="color: red;">Build Failed ❌</h2>
                            <p><strong>Project:</strong> ${env.PROJECT_NAME}</p>
                            <p><strong>Build Number:</strong> ${env.BUILD_NUMBER}</p>
                            <p><strong>Test Suite:</strong> ${params.TEST_SUITE}</p>
                            <p><strong>Browser:</strong> ${params.BROWSER}</p>
                            <p><strong>Environment:</strong> ${params.ENVIRONMENT}</p>
                            <p><strong>Duration:</strong> ${currentBuild.durationString}</p>
                            <p><strong>Commit:</strong> ${env.GIT_COMMIT_MSG}</p>
                            <p><strong>Author:</strong> ${env.GIT_AUTHOR}</p>
                            <br>
                            <p style="color: red;">Please check the console output and test reports for details.</p>
                            <br>
                            <p><a href="${env.BUILD_URL}console">View Console Output</a></p>
                            <p><a href="${env.BUILD_URL}artifact/test-output/screenshots">View Screenshots</a></p>
                            <p><a href="${env.BUILD_URL}artifact/test-output/videos">View Videos</a></p>
                        </body>
                        </html>
                    """,
                        to: '${DEFAULT_RECIPIENTS}',
                        mimeType: 'text/html'
                )
            }
        }

        unstable {
            script {
                echo "⚠️ Build is unstable (some tests failed)"

                emailext(
                        subject: "⚠️ UNSTABLE: ${env.PROJECT_NAME} - Build #${env.BUILD_NUMBER}",
                        body: """
                        <html>
                        <body>
                            <h2 style="color: orange;">Build Unstable ⚠️</h2>
                            <p>Some tests failed, but the build completed.</p>
                            <p><strong>Project:</strong> ${env.PROJECT_NAME}</p>
                            <p><strong>Build Number:</strong> ${env.BUILD_NUMBER}</p>
                            <p><strong>Test Suite:</strong> ${params.TEST_SUITE}</p>
                            <p><strong>Browser:</strong> ${params.BROWSER}</p>
                            <p><strong>Environment:</strong> ${params.ENVIRONMENT}</p>
                            <br>
                            <p><a href="${env.BUILD_URL}allure">View Allure Report</a></p>
                            <p><a href="${env.BUILD_URL}artifact/test-output/screenshots">View Screenshots</a></p>
                        </body>
                        </html>
                    """,
                        to: '${DEFAULT_RECIPIENTS}',
                        mimeType: 'text/html'
                )
            }
        }
    }
}