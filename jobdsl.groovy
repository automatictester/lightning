#!groovy

folder('lightning')

['core', 'standalone', 'maven', 'gradle'].each { module ->

    multibranchPipelineJob("lightning/${module}") {
        branchSources {
            git {
                remote('git@github.com:deliverymind/lightning.git')
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
                                        remote 'git@github.com:deliverymind/lightning.git'
                                        credentialsId 'github-creds'
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
        configure {
            it / factory(class: 'org.jenkinsci.plugins.workflow.multibranch.WorkflowBranchProjectFactory') {
                owner(class: 'org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject', reference: '../..')
                scriptPath("jenkins/${module}/Jenkinsfile")
            }
        }
    }
}
