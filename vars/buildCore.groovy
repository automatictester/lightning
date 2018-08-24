def call() {
    withEnv(["PATH+MAVEN=${tool 'M3'}/bin"]) {
        sh 'mvn -pl lightning-core clean install -DmockS3'
    }
}