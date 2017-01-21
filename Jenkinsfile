#!groovy

def sharedLib = new SharedLib()

node {
    stage('Prepare') {
        sharedLib.prepare()
        sharedLib.purge()
    }
    stage('Set release version number') {
        sharedLib.commitReleaseVersion()
    }
    stage('CORE - Test and install') {
        sharedLib.testAndInstallCore()
    }
    stage('CORE - Tag release') {
        sharedLib.tagCoreRelease()
    }
    stage('CORE - Release') {
        sharedLib.releaseCore()
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
        sharedLib.tagMavenPluginRelease()
    }
    stage('STANDALONE - Archive JAR') {
        sharedLib.archiveStandaloneJar()
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
        sharedLib.runMavenPluginITs()
    }
    stage('MAVEN-PLUGIN - Release') {
        sharedLib.releaseMavenPlugin()
    }
    stage('Set snapshot version number') {
        sharedLib.commitSnapshotVersion()
    }
    stage('CORE - Test and install - snapshot') {
        sharedLib.testAndInstallCore()
    }
    stage('STANDALONE - Generate JAR file - snapshot') {
        sharedLib.buildStandaloneJar()
    }
    stage('STANDALONE - Help - snapshot') {
        sharedLib.checkStandaloneJarHelpExitCodes()
    }
    stage('STANDALONE - Stubbed ITs - snapshot') {
        sharedLib.runStandaloneJarStubbedITs()
    }
//     Disabled until we find out how to run nohup from Jenkins job on macOS
//    stage('STANDALONE - ITs (CSV)') {
//        sharedLib.runStandaloneCsvITs()
//    }
//    stage('STANDALONE - ITs (SDW)') {
//        sharedLib.runStandaloneSdwITs()
//    }
    stage('MAVEN-PLUGIN - Install - snapshot') {
        sharedLib.installMavenPlugin()
    }
    stage('MAVEN-PLUGIN - Stubbed ITs - snapshot') {
        sharedLib.runMavenPluginStubbedITs()
    }
    stage('MAVEN-PLUGIN - ITs - snapshot') {
        sharedLib.runMavenPluginITs()
    }
    stage('Push release to origin/master') {
        sharedLib.push()
    }
}