pipeline {
    agent any
    
    tools {
        // This must match the Name you gave in Global Tool Configuration
        maven 'M3' 
        jdk 'jdk17' // If you configured a JDK tool similarly
    }
    
    stages {
        stage('Clone') {
            steps { 
                // This tells GitHub the build has started
                setGitHubPullRequestStatus(context: 'SonarQube Quality Gate', status: 'PENDING')
                checkout scm 
            }
        }
        stage('Build & Sonar Analysis') {
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
        success {
            setGitHubPullRequestStatus(context: 'SonarQube Quality Gate', status: 'SUCCESS', description: 'Passed!')
        }
        failure {
            setGitHubPullRequestStatus(context: 'SonarQube Quality Gate', status: 'FAILURE', description: 'Failed or Quality Gate broke.')
        }
    }
}
