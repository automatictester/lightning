#!groovy

def call() {
    sh './mvnw -pl lightning-core clean install -DmockS3'
}
