
pipeline {
    agent { label 'agent-1' }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build JAR') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                docker build -t poyrazemun/quizzapp:latest .
                '''
            }
        }

        stage('Restart Container') {
            steps {
                sh '''
                docker compose down || true
                docker compose up -d
                '''
            }
        }
    }
}
