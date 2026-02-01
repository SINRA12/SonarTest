properties([
    githubProjectProperty(projectUrlStr: 'https://github.com/SINRA12/SonarTest/')
])

pipeline {
    agent any
    tools { maven 'M3' }

    stage('Debug Env') {
    steps {
        sh 'printenv | sort' // For Linux/Docker
        // OR use: echo "CHANGE_ID is: ${env.CHANGE_ID}"
      }
    }
    stages {
        stage('Status Start') {
            steps {
                script {
                    try {
                        // Removed repo and credentialsId to stop the 'Unknown parameter' warning
                        setGitHubPullRequestStatus(context: 'SonarQube Quality Gate', state: 'PENDING', message: 'Building...')
                    } catch (Exception e) {
                        echo "Discovery failed: ${e.message}"
                    }
                }
            }
        }
        stage('Build & Sonar') {
            steps {
                withSonarQubeEnv('MySonarServer') {
                    sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=SonarTest -Dsonar.token=squ_588fe030cc25367ea49bedd8778ef86043a0d98f'
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
