#!groovy

folder('lightning')

['core', 'standalone', 'maven', 'gradle', 'lambda'].each { module ->

    multibranchPipelineJob("lightning/${module}") {
        branchSources {
            git {
                remote('git@github.com:automatictester/lightning.git')
                credentialsId('github-creds')
            }
        }
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
                                        remote 'git@github.com:automatictester/lightning.git'
                                        credentialsId 'github-creds'
                                    }
                                }
                            }
                        }
                        allowVersionOverride true
                        defaultVersion 'master'
                        implicit false
                    }
                }
            }
        }
        factory {
            workflowBranchProjectFactory {
                scriptPath("jenkins/${module}/Jenkinsfile")
            }
        }
    }
}
