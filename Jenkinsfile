pipeline {
    agent any
    
    tools {
        maven 'M3'
        //jdk 'jdk17'
    }

    stages {
        stage('Status Start') {
            steps {
                script {
                    // Manually set GitHub status to PENDING at the start
                    step([
                        $class: "GitHubCommitStatusSetter",
                        reposSource: [$class: "ManuallyEnteredRepositorySource", url: "https://github.com/SINRA12/SonarTest"],
                        contextSource: [$class: "ManuallyEnteredCommitContextSource", context: "SonarQube Quality Gate"],
                        statusResultSource: [
                            $class: "ConditionalStatusResultSource",
                            results: [[$class: "AnyBuildResult", message: "Analysis in progress...", state: "PENDING"]]
                        ]
                    ])
                }
            }
        }

        stage('Build & Sonar') {
            steps {
                withSonarQubeEnv('MySonarServer') {
                    // Use your specific token here
                    sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=SonarTest -Dsonar.token=squ_588fe030cc25367ea49bedd8778ef86043a0d98f'
                }
            }
        }

        stage("Quality Gate") {
            steps {
                // Wait for the webhook from SonarQube
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }

    post {
        always {
            script {
                // Capture current build result for the final status
                def resultState = currentBuild.currentResult == 'SUCCESS' ? 'SUCCESS' : 'FAILURE'
                
                step([
                    $class: "GitHubCommitStatusSetter",
                    reposSource: [$class: "ManuallyEnteredRepositorySource", url: "https://github.com/SINRA12/SonarTest"],
                    contextSource: [$class: "ManuallyEnteredCommitContextSource", context: "SonarQube Quality Gate"],
                    statusResultSource: [
                        $class: "ConditionalStatusResultSource",
                        results: [[$class: "AnyBuildResult", message: "Build ${resultState}", state: resultState]]
                    ]
                ])
            }
        }
    }
}
