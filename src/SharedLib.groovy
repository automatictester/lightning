
def cleanupWorkspaceAndCloneRepo() {
    cleanWs()
    git credentialsId: 'github-creds', url: 'git@github.com:automatictester/lightning.git'
}

def purge() {
    sh 'rm -rf ~/.m2/repository/uk/co/automatictester/*lightning*/'
}

def push() {
    sshagent(['github-creds']) {
        sh 'git push --set-upstream origin master; git push --tags'
    }
}