#!groovy

def call() {
    sshagent(['github-creds']) {
        sh 'git push --set-upstream origin master; git push --tags'
    }
}
