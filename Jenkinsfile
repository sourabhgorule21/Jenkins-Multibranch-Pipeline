pipeline {
    agent any

    stages {
        stage('Deploy Stack') {
            steps {
                sh '''
                docker compose down
                docker compose up -d --build
                docker compose ps
                '''
            }
        }

        stage('Logs') {
            steps {
                sh 'docker compose logs --tail=80 app'
            }
        }
    }
}
