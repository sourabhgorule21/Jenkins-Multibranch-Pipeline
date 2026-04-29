pipeline {
    agent any

    stages {

        stage('Build Maven') {
            steps {
                sh 'mvn -B clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'ls -la target'   // debug (optional)
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
}
