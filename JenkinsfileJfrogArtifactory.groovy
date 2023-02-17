node('Agent_Win2k16') {
    // replace with a query for the choicen list (e.g. query against SVN or Git)... node is needed if accessing SVN or Git...
    env.NAME_LIST = "One\nTwo\nThree"
    withCredentials([[$class: 'UsernamePasswordMultiBinding',
                      credentialsId: 'user-credential-in-gitlab',
                      usernameVariable: 'GIT_USERNAME',
                      passwordVariable: 'GITLAB_ACCESS_TOKEN']]) {
        env.BRANCH_NAMES = sh (script: 'git ls-remote -h https://${GIT_USERNAME}:${GITLAB_ACCESS_TOKEN}@github.com/PROJS/PROJ.git | sed \'s/\\(.*\\)\\/\\(.*\\)/\\2/\' ', returnStdout:true).trim()
    }
}
//def MyChoice = "One\nTwo\nThree"
pipeline {
    agent { label 'Agent_Win2k16'}
    triggers {
        pollSCM 'H/5 5-23 * * *'
        //cron '* * * * *'
    }
    environment {
        A_NAME = 'World'

        MAVEN_HOME = tool('Maven_339')
        JAVA_HOME = tool('JDK_Zulu_8.31')
    }
    parameters {
        //choice(name: 'CHOICE', choices: ['One', 'Two', 'Three'], description: 'Pick something')
        //choice(name: 'ChoiceName', choices: "${MyChoice}", description: 'Pick your name')
        choice(name: 'ChoiceName', choices: "${NAME_LIST}", description: 'Pick your name')
        choice(
                name: 'BranchName',
                choices: "${BRANCH_NAMES}",
                description: 'to refresh the list, go to configure, disable "this build has parameters", launch build (without parameters)to reload the list and stop it, then launch it again (with parameters)'
        )
    }

    //tools {
    //maven 'Maven_339'
    //jdk 'JDK_Zulu_8.31'
    //}
    stages {
        stage('Hello') {
            steps {
                echo "Hello ${A_NAME} and ${ChoiceName}"
                sh "echo SUCCESS on ${BranchName}"
            }
        }
        stage('Build') {
            steps {
                // Setting up Maven resolver and deployer - replacement for settings.xml for builds (does not apply to docker images push which still requires settings.xml)
                rtServer(
                        id: "ARTIFACTORY_SERVER",
                        url: 'http://artifactory.XXX.com:8081/artifactory',
                        credentialsId: "1aab92ba-XXXX-XXXX-XXXX-010c26ed662e"
                )
                rtMavenDeployer(
                        id: "MAVEN_DEPLOYER",
                        serverId: "ARTIFACTORY_SERVER",
                        releaseRepo: "libs-releases-testrepo",
                        snapshotRepo: "libs-snapshots-testrepo",
                        deployArtifacts: true
                )
                rtMavenResolver(
                        id: "MAVEN_RESOLVER",
                        serverId: "ARTIFACTORY_SERVER",
                        releaseRepo: "repo",
                        snapshotRepo: "repo"
                )

                rtMavenRun(
                        // Tool name from Jenkins configuration.
                        // tool: 'Maven_3.3.9',
                        pom: 'pom.xml',
                        //goals: 'clean install -DskipTests',
                        goals: 'clean install',
                        // Maven options.
                        // opts: '-Xms1024m -Xmx4096m',
                        resolverId: 'MAVEN_RESOLVER',
                        deployerId: 'MAVEN_DEPLOYER',
                )
                rtPublishBuildInfo( serverId: 'ARTIFACTORY_SERVER' )

                //bat "mvn -version"
                //bat "mvn clean install sonar:sonar"
                //bat "mvn clean install"
            }
        }
        stage('Tests Publish') {
            steps {
                junit '**/target/**/*.xml'
                jacoco(
                        execPattern: '**/**.exec',
                        classPattern: '**/classes',
                        sourcePattern: '**/src/main/java',
                        inclusionPattern: '**/*.java'
                )
            }
        }
    }
    post {
        always {
            echo 'Post always'

            // One or more steps need to be included within each condition's block.
        }
        failure {
            echo 'Post failure'

            //mail bcc: '', body: 'test', cc: '', from: '', replyTo: '', subject: 'test', to: 'SomeName@company.com'
            emailext attachLog: false, body: '''Hello ${BUILD_URL} has failed''', subject: 'Test Job', to: 'SomeName@company.com'
        }
        unstable {
            echo 'Post unstable'
        }
    }
}
