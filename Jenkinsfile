pipeline {
    agent any

    options {
        timestamps()
    }

    environment {
        APP_NAME = 'demo-app'
        APP_IMAGE = "${APP_NAME}:latest"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build And Test') {
            agent {
                docker {
                    image 'maven:3.9.9-eclipse-temurin-21'
                    args '-v maven-repo:/root/.m2'
                }
            }
            steps {
                sh '''
                chmod +x mvnw
                ./mvnw -B clean verify
                '''
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t ${APP_IMAGE} .'
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                export APP_IMAGE=${APP_IMAGE}
                docker compose down --remove-orphans || true
                docker compose up -d
                '''
            }
        }
    }

    post {
        success {
            echo 'CI/CD pipeline completed successfully.'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
