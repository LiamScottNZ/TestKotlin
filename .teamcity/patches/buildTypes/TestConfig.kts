package patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.BuildFeature
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.ScriptBuildStep
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2018_2.failureConditions.BuildFailureOnText
import jetbrains.buildServer.configs.kotlin.v2018_2.failureConditions.failOnText
import jetbrains.buildServer.configs.kotlin.v2018_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with id = 'TestConfig'
accordingly, and delete the patch script.
*/
changeBuildType(RelativeId("TestConfig")) {
    check(maxRunningBuilds == 0) {
        "Unexpected option value: maxRunningBuilds = $maxRunningBuilds"
    }
    maxRunningBuilds = 1

    expectSteps {
        script {
            name = "Setup"
            executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
            scriptContent = """
                set -eu
                
                echo "%env.ROLE%"
            """.trimIndent()
        }
    }
    steps {
        update<ScriptBuildStep>(0) {
            clearConditions()
            scriptContent = """
                set -eu
                
                set -eu
                
                testResult=`cat %teamcity.build.checkoutDir%/TestOutput/testResult.txt`
                if [ ${'$'}testResult == "FAILED" ]
                then
                 echo "One or more tests failed";
                 exit 1;
                fi
                
                echo ##teamcity[importData type='typeID' path='<path to the xml file>']
            """.trimIndent()
            formatStderrAsError = true
        }
        insert(1) {
            script {
                executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
                scriptContent = """echo "HIE""""
            }
        }
        insert(2) {
            script {
                scriptContent = """
                    containerId=${'$'}(docker create xero-esr-docker-common.artifactory.xero-support.com/esr_api:%build.vcs.number%)
                    docker cp "${'$'}containerId":/app/TestResults/ ./
                    docker rm "${'$'}containerId"
                """.trimIndent()
            }
        }
    }

    failureConditions {

        check(errorMessage == false) {
            "Unexpected option value: errorMessage = $errorMessage"
        }
        errorMessage = true
        add {
            failOnText {
                conditionType = BuildFailureOnText.ConditionType.CONTAINS
                pattern = "SMOKE TESTS FAILED"
                failureMessage = "One or more smoke tests failed"
                reverse = false
                stopBuildOnFailure = true
            }
        }
    }

    features {
        val feature1 = find<BuildFeature> {
            feature {
                type = "xml-report-plugin"
                param("xmlReportParsing.reportDirs", "%env.DOTNET_ENVIRONMENT%")
                param("xmlReportParsing.reportType", "trx")
            }
        }
        feature1.apply {
            param("xmlReportParsing.reportType", "vstest")
            param("xmlReportParsing.reportDirs", "./TestOutput/*.xml")
        }
    }
}
