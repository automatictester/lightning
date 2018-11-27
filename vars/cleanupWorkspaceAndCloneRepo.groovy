#!groovy

def call() {
    cleanWs()
    git credentialsId: 'github-creds', url: 'git@github.com:automatictester/lightning.git'
}
