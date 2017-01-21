// Jenkins Pipeline Plugin Folder-Level Shared Library

def prepare() {
    step([$class: 'WsCleanup'])
    git credentialsId: "${GIT_CREDENTIALS_ID}", url: "${REPO_URL}", branch: "${GIT_BRANCH}"
}

def purge() {
    sh 'rm -rf ~/.m2/repository/uk/co/deliverymind/'

//    does not work correctly in all cases
//    withEnv(["PATH+MAVEN=${tool 'M3'}/bin"]) {
//        sh 'mvn -pl plugin dependency:purge-local-repository -DresolutionFuzziness=groupId -Dinclude=uk.co.deliverymind -DactTransitively=false -DreResolve=false -Dverbose=true'
//    }
}

def runStandaloneJarStubbedITs() {
    sh "(cd lightning-standalone; ./src/test/scripts/console/output/report_10_0.sh)"
    sh "(cd lightning-standalone; ./src/test/scripts/console/output/verify_1_1_1.sh)"
    sh "(cd lightning-standalone; ./src/test/scripts/console/output/verify_3_0_0.sh)"
    sh "(cd lightning-standalone; ./src/test/scripts/console/output/verify_3_0_0_2s.sh)"
    sh "(cd lightning-standalone; ./src/test/scripts/exit/code/verify_1_1_1.sh)"
    sh "(cd lightning-standalone; ./src/test/scripts/exit/code/verify_3_0_0.sh)"
    sh "(cd lightning-standalone; ./src/test/scripts/exit/code/verify_3_0_0_2s.sh)"
    sh "(cd lightning-standalone; ./src/test/scripts/exit/code/report_2_0.sh)"
    sh "(cd lightning-standalone; ./src/test/scripts/exit/code/report_2_1.sh)"
    sh "(cd lightning-standalone; ./src/test/scripts/file/junit.sh)"
}

def runStandaloneCsvITs() {
    sh "mvn -pl lightning-standalone-it -P jetty jetty:run &"
    sleep 5
    sh "mvn -pl lightning-standalone-it clean verify -Djmx.file=csv-test.jmx -Dresults.file.format=csv"
    sh "mvn -pl lightning-standalone-it -P jetty jetty:stop"
    sh "java -jar lightning-standalone/target/lightning-standalone-*.jar verify -xml lightning-standalone-it/src/test/resources/lightning.xml --jmeter-csv lightning-standalone-it/target/jmeter/results/csv-test.csv"
}

def runStandaloneSdwITs() {
    sh "mvn -pl lightning-standalone-it -P jetty jetty:run &"
    sleep 5
    sh "mvn -pl lightning-standalone-it clean verify -Djmx.file=sdw-test.jmx -Dresults.file.format=xml"
    sh "mvn -pl lightning-standalone-it -P jetty jetty:stop"
    sh "java -jar lightning-standalone/target/lightning-standalone-*.jar verify -xml lightning-standalone-it/src/test/resources/lightning.xml --jmeter-csv lightning-standalone-it/target/jmeter/bin/sdw-results.csv"
}

def checkStandaloneJarHelpExitCodes() {
    // Check no exception is thrown (regression issue). Output is verified elsewhere.
    sh "java -jar lightning-standalone/target/lightning-standalone-*.jar"
    sh "java -jar lightning-standalone/target/lightning-standalone-*.jar -h"
    sh "java -jar lightning-standalone/target/lightning-standalone-*.jar --help"
}

def runMavenPluginStubbedITs() {
    sh "(cd jmeter-lightning-maven-plugin-it; ./src/test/scripts/console/output/report_10_0.sh)"
    sh "(cd jmeter-lightning-maven-plugin-it; ./src/test/scripts/console/output/verify_1_1_1.sh)"
    sh "(cd jmeter-lightning-maven-plugin-it; ./src/test/scripts/console/output/verify_3_0_0.sh)"
    sh "(cd jmeter-lightning-maven-plugin-it; ./src/test/scripts/console/output/verify_3_0_0_2s.sh)"
    sh "(cd jmeter-lightning-maven-plugin-it; ./src/test/scripts/console/output/verify_regexp.sh)"
    sh "(cd jmeter-lightning-maven-plugin-it; ./src/test/scripts/exit/code/verify_1_1_1.sh)"
    sh "(cd jmeter-lightning-maven-plugin-it; ./src/test/scripts/exit/code/verify_3_0_0.sh)"
    sh "(cd jmeter-lightning-maven-plugin-it; ./src/test/scripts/exit/code/verify_3_0_0_2s.sh)"
    sh "(cd jmeter-lightning-maven-plugin-it; ./src/test/scripts/exit/code/verify_regexp.sh)"
    sh "(cd jmeter-lightning-maven-plugin-it; ./src/test/scripts/exit/code/report_2_0.sh)"
    sh "(cd jmeter-lightning-maven-plugin-it; ./src/test/scripts/exit/code/report_2_1.sh)"
    sh "(cd jmeter-lightning-maven-plugin-it; ./src/test/scripts/file/junit.sh)"
}

def runMavenPluginITs() {
    withEnv(["PATH+MAVEN=${tool 'M3'}/bin"]) {
        sh "mvn -pl jmeter-lightning-maven-plugin-it clean verify -P jmeter"
        // Check the following files were generated. Content is verified elsewhere.
        sh "cat jmeter-lightning-maven-plugin-it/target/jmeter/bin/junit.xml"
        sh "cat lightning-jenkins.properties"
    }
}

def installMavenPlugin() {
    withEnv(["PATH+MAVEN=${tool 'M3'}/bin"]) {
        sh 'mvn -pl jmeter-lightning-maven-plugin clean install'
    }
}

def testAndInstallCore() {
    withEnv(["PATH+MAVEN=${tool 'M3'}/bin"]) {
        sh 'mvn -pl lightning-core clean test install'
    }
}

def buildStandaloneJar() {
    withEnv(["PATH+MAVEN=${tool 'M3'}/bin"]) {
        sh 'mvn -pl lightning-standalone clean compile test assembly:single'
    }
}