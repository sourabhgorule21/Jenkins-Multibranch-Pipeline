pipeline {
    agent any

    stages {
        stage('Build Maven') {
            agent {
                docker {
                    image 'maven:3.9.9-eclipse-temurin-21'
                    args '-v maven-repo:/root/.m2'
                }
            }
            steps {
                sh 'mvn -B clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t demo-app:latest .'
            }
        }

        stage('Deploy Docker Container') {
            steps {
                sh '''
                docker stop demo-app || true
                docker rm demo-app || true

                docker run -d \
                  --name demo-app \
                  -p 9090:9090 \
                  demo-app:latest
                '''
            }
        }
    }

    post {
        success {
            echo 'JAR deployed on Docker successfully ✅'
        }
        failure {
            echo 'Pipeline failed ❌'
        }
    }
}
