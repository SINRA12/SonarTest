pipeline {
    agent any
    
    tools {
        maven 'M3'     // Must match your Global Tool Configuration name
        jdk 'jdk17'    // Must match your Global Tool Configuration name
    }

    stages {
        stage('Clone') {
            steps { 
                // Fix 1: status -> state
                setGitHubPullRequestStatus(context: 'SonarQube Quality Gate', state: 'PENDING', message: 'Analysis in progress...')
                checkout scm 
            }
        }
        stage('Build & Sonar Analysis') {
            steps {
                withSonarQubeEnv('MySonarServer') { 
                    sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=SonarTest -Dsonar.token=$SONAR_AUTH_TOKEN'
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
        success {
            // Fix 2: status -> state, description -> message
            setGitHubPullRequestStatus(context: 'SonarQube Quality Gate', state: 'SUCCESS', message: 'Quality Gate Passed!')
        }
        failure {
            // Fix 3: status -> state, description -> message
            setGitHubPullRequestStatus(context: 'SonarQube Quality Gate', state: 'FAILURE', message: 'Quality Gate Failed or Build Error')
        }
    }
}
