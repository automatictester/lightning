#!groovy

folder('lightning')

['core', 'standalone', 'maven'].each { module ->

    multibranchPipelineJob("lightning/${module}") {
        branchSources {
            git {
                id('89D67797-17C2-4935-99C8-9E66AEF453DE_' + ${module})
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
