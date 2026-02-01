properties([
    githubProjectProperty(projectUrlStr: 'https://github.com/SINRA12/SonarTest/')
])

pipeline {
    agent any
    
    tools {
        maven 'M3'
    }

    stages {
        stage('Status Start') {
            steps {
                script {
                    try {
                        // Explicitly defining repo and credentialsId to bypass discovery issues
                        setGitHubPullRequestStatus(
                            repo: 'SINRA12/SonarTest',
                            context: 'SonarQube Quality Gate', 
                            state: 'PENDING', 
                            message: 'Building...',
                            credentialsId: 'github-token-f' // Ensure this ID matches your Jenkins credentials
                        )
                    } catch (Exception e) {
                        echo "Could not update GitHub status: ${e.message}"
                    }
                }
            }
        }
        
        stage('Build & Sonar') {
            steps {
                withSonarQubeEnv('MySonarServer') {
                    // Running Maven with your hardcoded token as requested
                    sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=SonarTest -Dsonar.token=squ_588fe030cc25367ea49bedd8778ef86043a0d98f'
                }
            }
        }
        
        stage("Quality Gate") {
            steps {
                // Waits for the SonarQube background task to finish
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
                    // Final update sent to GitHub based on the build outcome
                    setGitHubPullRequestStatus(
                        repo: 'SINRA12/SonarTest',
                        context: 'SonarQube Quality Gate', 
                        state: resultState, 
                        message: "Build ${resultState}",
                        credentialsId: 'github-token-f'
                    )
                } catch (Exception e) {
                    echo "Final status update failed: ${e.message}"
                }
            }
        }
    }
}
