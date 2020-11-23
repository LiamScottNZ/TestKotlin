import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.DotnetTestStep
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.dotnetTest
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2018_2.failureConditions.BuildFailureOnMetric
import jetbrains.buildServer.configs.kotlin.v2018_2.failureConditions.failOnMetricChange
import jetbrains.buildServer.configs.kotlin.v2018_2.projectFeatures.buildReportTab
import jetbrains.buildServer.configs.kotlin.v2018_2.triggers.finishBuildTrigger
import jetbrains.buildServer.configs.kotlin.v2018_2.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2020.1"

project {

    vcsRoot(HttpsGithubComLiamScottNZKotlin)

    buildType(TestConfig)
}

object TestConfig : BuildType({
    name = "TestConfig"

    params {
        param("env.DOTNET_ENVIRONMENT", "uat")
    }
    
    var environment = "%env.DOTNET_ENVIRONMENT%"
    
    val rolePrefix = "arn:aws:iam::"
    val roleSuffix = ":role/TEST"
    
    val uatId = 123;
    val prodId = 456;
    
    var role = "";    
    if (environment == "uat") {
        role = rolePrefix + uatId + roleSuffix;
    } else {
        role = rolePrefix + prodId + roleSuffix;
    }

    vcs {
        root(HttpsGithubComLiamScottNZKotlin)

        checkoutMode = CheckoutMode.ON_AGENT
        cleanCheckout = true
    }

    steps {
    }   

    features {
        feature {
            id = "BUILD_EXT_1"
            type = "xml-report-plugin"
            param("xmlReportParsing.reportType", "trx")
            param("xmlReportParsing.reportDirs", role)
        }
    }
})

object HttpsGithubComLiamScottNZKotlin : GitVcsRoot({
    name = "https://github.com/LiamScottNZ/TestKotlin"
    url = "https://github.com/LiamScottNZ/TestKotlin.git"
    authMethod = password {
        userName = "LiamScottNZ"
        password = "credentialsJSON:8c0df767-65b1-4ef0-9fbc-3501b9ac7fc9"
    }
})
