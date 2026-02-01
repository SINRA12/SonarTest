pipeline {
    agent any
    stages {
        stage('Clone') {
            steps { checkout scm }
        }
        stage('Build & Sonar Analysis') {
            steps {
                withSonarQubeEnv('MySonarServer') { 
                    // This runs the maven command we practiced
                    sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=SonarTest'
                }
            }
        }
        stage("Quality Gate") {
            steps {
                // Jenkins waits here for the Webhook from SonarQube
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }
}
