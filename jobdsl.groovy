#!groovy

def gitCreds = '11be7a79-8034-407b-8351-dbd1d3342c24'
def folderName = 'lightning'


// Create folder with Pipeline Plugin Folder-Level Shared Library
folder(folderName) {
    properties {
        folderLibraries {
            libraries {
                libraryConfiguration {
                    name 'SharedLib'
                    retriever {
                        modernSCM {
                            scm {
                                git {
                                    id 'lightning-git-repo'
                                    remote 'git@github.com:deliverymind/lightning.git'
                                    remoteName 'origin'
                                    rawRefSpecs ''
                                    credentialsId gitCreds
                                    ignoreOnPushNotifications false
                                    includes '*'
                                    excludes ''
                                }
                            }
                        }
                    }
                    allowVersionOverride true
                    defaultVersion 'master'
                    implicit true
                }
            }
        }
    }
}


// Create separate jobs for every Lightning module
['core', 'standalone', 'maven', 'gradle'].each { module ->
    pipelineJob("$folderName/${module}") {
        concurrentBuild(false)
        properties {
            buildDiscarder {
                strategy {
                    logRotator {
                        numToKeepStr('10')
                        daysToKeepStr(null)
                        artifactDaysToKeepStr(null)
                        artifactNumToKeepStr(null)
                    }
                }
            }
            githubProjectProperty {
                projectUrlStr('https://github.com/deliverymind/lightning/')
            }
            rebuildSettings {
                autoRebuild(true)
                rebuildDisabled(false)
            }
        }
        parameters {
            stringParam('RELEASE_VERSION', '9.0.0', '9.1.0, 9.2.0 etc')
            stringParam('POST_RELEASE_SNAPSHOT_VERSION', '9.0.1-SNAPSHOT', '9.1.1-SNAPSHOT, 9.2.1-SNAPSHOT etc')
            nonStoredPasswordParam('GPG_PASSPHRASE', 'GPG passphrase for signing artefacts')
            stringParam('REPO_URL', 'git@github.com:deliverymind/lightning.git', 'Or local path, e.g.: "file:///Users/username/git/lightning"')
            stringParam('GIT_CREDENTIALS_ID', gitCreds, '')
            stringParam('GIT_BRANCH', 'master', '')
            stringParam('TEST_ONLY', 'true', '')
            stringParam('DRY_RUN', 'true', '')
        }
        definition {
            cpsScm {
                scm {
                    git {
                        branch('*/master')
                        remote {
                            credentials(gitCreds)
                            url('$REPO_URL')
                        }
                    }
                }
                scriptPath("jenkins/${module}/Jenkinsfile")
            }
        }
    }
}
