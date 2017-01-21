#!groovy

def sharedLib = new SharedLib()

node {
    stage('Prepare') {
        sharedLib.prepare()
        sharedLib.purge()
    }
    stage('Set release version number') {
        if ("${TEST_ONLY}" == "false") {
            sharedLib.commitReleaseVersion()
        }
    }
    stage('CORE - Test and install') {
        sharedLib.testAndInstallCore()
    }
    stage('CORE - Tag release') {
        if ("${TEST_ONLY}" == "false") {
            sharedLib.tagCoreRelease()
        }
    }
    stage('CORE - Release') {
        if ("${TEST_ONLY}" == "false" && "${DRY_RUN}" == "false") {
            sharedLib.releaseCore()
        }
    }
    stage('STANDALONE - Generate JAR file') {
        sharedLib.buildStandaloneJar()
    }
    stage('STANDALONE - Help') {
        sharedLib.checkStandaloneJarHelpExitCodes()
    }
    stage('STANDALONE - Stubbed ITs') {
        sharedLib.runStandaloneJarStubbedITs()
    }
//     Disabled until we find out how to run nohup from Jenkins job on macOS
//    stage('STANDALONE - ITs (CSV)') {
//        sharedLib.runStandaloneCsvITs()
//    }
//    stage('STANDALONE - ITs (SDW)') {
//        sharedLib.runStandaloneSdwITs()
//    }
    stage('STANDALONE - Tag release') {
        if ("${TEST_ONLY}" == "false") {
            sharedLib.tagMavenPluginRelease()
        }
    }
    stage('STANDALONE - Archive JAR') {
        if ("${TEST_ONLY}" == "false") {
            sharedLib.archiveStandaloneJar()
        }
    }
    stage('MAVEN-PLUGIN - Install') {
        sharedLib.installMavenPlugin()
    }
    stage('MAVEN-PLUGIN - Stubbed ITs') {
        sharedLib.runMavenPluginStubbedITs()
    }
    stage('MAVEN-PLUGIN - ITs') {
        sharedLib.runMavenPluginITs()
    }
    stage('MAVEN-PLUGIN - Tag release') {
        if ("${TEST_ONLY}" == "false") {
            sharedLib.runMavenPluginITs()
        }
    }
    stage('MAVEN-PLUGIN - Release') {
        if ("${TEST_ONLY}" == "false" && "${DRY_RUN}" == "false") {
            sharedLib.releaseMavenPlugin()
        }
    }
    stage('Set snapshot version number') {
        if ("${TEST_ONLY}" == "false") {
            sharedLib.commitSnapshotVersion()
        }
    }
    stage('CORE - Test and install - snapshot') {
        if ("${TEST_ONLY}" == "false") {
            sharedLib.testAndInstallCore()
        }
    }
    stage('STANDALONE - Generate JAR file - snapshot') {
        if ("${TEST_ONLY}" == "false") {
            sharedLib.buildStandaloneJar()
        }
    }
    stage('STANDALONE - Help - snapshot') {
        if ("${TEST_ONLY}" == "false") {
            sharedLib.checkStandaloneJarHelpExitCodes()
        }
    }
    stage('STANDALONE - Stubbed ITs - snapshot') {
        if ("${TEST_ONLY}" == "false") {
            sharedLib.runStandaloneJarStubbedITs()
        }
    }
//     Disabled until we find out how to run nohup from Jenkins job on macOS
//    stage('STANDALONE - ITs (CSV)') {
//        if ("${TEST_ONLY}" == "false") {
//            sharedLib.runStandaloneCsvITs()
//        }
//    }
//    stage('STANDALONE - ITs (SDW)') {
//        if ("${TEST_ONLY}" == "false") {
//            sharedLib.runStandaloneSdwITs()
//        }
//    }
    stage('MAVEN-PLUGIN - Install - snapshot') {
        if ("${TEST_ONLY}" == "false") {
            sharedLib.installMavenPlugin()
        }
    }
    stage('MAVEN-PLUGIN - Stubbed ITs - snapshot') {
        if ("${TEST_ONLY}" == "false") {
            sharedLib.runMavenPluginStubbedITs()
        }
    }
    stage('MAVEN-PLUGIN - ITs - snapshot') {
        if ("${TEST_ONLY}" == "false") {
            sharedLib.runMavenPluginITs()
        }
    }
    stage('Push release to origin/master') {
        if ("${TEST_ONLY}" == "false" && "${DRY_RUN}" == "false") {
            sharedLib.push()
        }
    }
}