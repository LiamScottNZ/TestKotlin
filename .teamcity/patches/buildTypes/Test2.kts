package patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2018_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, create a buildType with id = 'Test2'
in the root project, and delete the patch script.
*/
create(DslContext.projectId, BuildType({
    id("Test2")
    name = "Test2"

    dependencies {
        artifacts(RelativeId("Test2")) {
            buildRule = lastSuccessful()
            artifactRules = "package.zip"
        }
    }
}))

