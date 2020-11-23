package patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2018_2.*
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

    requirements {
        add {
            contains("teamcity.agent.jvm.os.name", "Windows")
        }
    }
}
