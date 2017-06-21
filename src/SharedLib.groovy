// Jenkins Pipeline Plugin Folder-Level Shared Library

def cleanupWorkspaceAndCloneRepo() {
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

def push() {
    sshagent(["${GIT_CREDENTIALS_ID}"]) {
        sh "git push --set-upstream origin master; git push --tags"
    }
}