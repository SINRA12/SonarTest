pipeline {
    agent any
    tools {
        maven 'M3'
        jdk 'jdk17'
    }
    stages {
        stage('Status Start') {
            steps {
                script {
                    try {
                        setGitHubPullRequestStatus(context: 'SonarQube Quality Gate', state: 'PENDING', message: 'Building...')
                    } catch (Exception e) {
                        echo "Could not update GitHub status: ${e.message}"
                    }
                }
            }
        }
        stage('Build & Sonar') {
            steps {
                withSonarQubeEnv('MySonarServer') {
                    sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=SonarTest'
                }
            }
        }
        stage("Quality Gate") {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }
    post {
        always {
            script {
                try {
                    def resultState = currentBuild.currentResult == 'SUCCESS' ? 'SUCCESS' : 'FAILURE'
                    setGitHubPullRequestStatus(context: 'SonarQube Quality Gate', state: resultState, message: "Build ${resultState}")
                } catch (Exception e) {
                    echo "Final status update failed: ${e.message}"
                }
            }
        }
    }
}
