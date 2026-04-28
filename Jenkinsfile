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
                sh "docker build -t demo-app:${env.BRANCH_NAME} ."
            }
        }
    }
}
