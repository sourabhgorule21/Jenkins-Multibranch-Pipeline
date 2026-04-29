pipeline {
    agent any

    environment {
        APP_NAME = 'demo-app'
        APP_IMAGE = 'demo-app:latest'
        APP_PORT = '9090'
        DB_HOST = 'mysql-db'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t ${APP_IMAGE} .'
            }
        }

        stage('Remove Old Container') {
            steps {
                sh 'docker rm -f ${APP_NAME} || true'
            }
        }

        stage('Run New Container') {
            steps {
                sh '''
                docker run -d \
                  --name ${APP_NAME} \
                  --restart unless-stopped \
                  -p ${APP_PORT}:9090 \
                  -e DB_HOST=${DB_HOST} \
                  -e DB_USERNAME=${DB_USERNAME} \
                  -e DB_PASSWORD=${DB_PASSWORD} \
                  ${APP_IMAGE}
                '''
            }
        }

        stage('Verify Deployment') {
            steps {
                sh 'docker ps --filter "name=${APP_NAME}"'
            }
        }
    }
}
