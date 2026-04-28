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
                sh 'docker build -t demo-app .'
            }
        }

        stage('Run Container') {
            steps {
                sh '''
                docker stop demo-container || true
                docker rm demo-container || true
                docker run -d -p 8081:8080 --name demo-container demo-app
                '''
            }
        }
    }

    post {
        always {
            echo 'Pipeline completed 🚀'
        }
    }
}