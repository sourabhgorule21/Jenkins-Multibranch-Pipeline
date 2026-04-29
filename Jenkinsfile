pipeline {
    agent any

    options {
        skipDefaultCheckout(true)
        timestamps()
    }

    environment {
        APP_NAME = 'demo-app'
        APP_IMAGE = 'demo-app:latest'
        MYSQL_IMAGE = 'mysql:8.4'
        MYSQL_CONTAINER = 'mysql-db'
        DOCKER_NETWORK = 'demo-net'
        MYSQL_VOLUME = 'mysql-data'
        APP_PORT = '9090'
        DB_HOST = 'mysql-db'
        DB_PORT = '3306'
        DB_NAME = 'nmmc'
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

        stage('Preflight Checks') {
            steps {
                sh '''
                if [ -z "${DB_USERNAME}" ] || [ -z "${DB_PASSWORD}" ]; then
                  echo "ERROR: DB_USERNAME and DB_PASSWORD are required."
                  echo "Set them in Jenkins credentials/environment before deployment."
                  exit 1
                fi

                PORT_CONFLICTS=$(docker ps --filter "publish=${APP_PORT}" --format '{{.Names}}' | grep -v "^${APP_NAME}$" || true)
                if [ -n "${PORT_CONFLICTS}" ]; then
                  echo "ERROR: Host port ${APP_PORT} is already in use by: ${PORT_CONFLICTS}"
                  echo "Free port ${APP_PORT} (or change Jenkins host mapping if Jenkins itself uses 9090)."
                  exit 1
                fi
                '''
            }
        }

        stage('Ensure Docker Network') {
            steps {
                sh '''
                docker network inspect ${DOCKER_NETWORK} >/dev/null 2>&1 || docker network create ${DOCKER_NETWORK}
                '''
            }
        }

        stage('Ensure MySQL') {
            steps {
                sh '''
                set +x
                if ! docker ps -a --format '{{.Names}}' | grep -q "^${MYSQL_CONTAINER}$"; then
                  docker volume create ${MYSQL_VOLUME} >/dev/null
                  docker run -d \
                    --name ${MYSQL_CONTAINER} \
                    --network ${DOCKER_NETWORK} \
                    --restart unless-stopped \
                    -e MYSQL_ROOT_PASSWORD=${DB_PASSWORD} \
                    -e MYSQL_DATABASE=${DB_NAME} \
                    -v ${MYSQL_VOLUME}:/var/lib/mysql \
                    ${MYSQL_IMAGE}
                else
                  docker start ${MYSQL_CONTAINER} >/dev/null || true
                  docker network connect ${DOCKER_NETWORK} ${MYSQL_CONTAINER} >/dev/null 2>&1 || true
                fi

                for i in $(seq 1 60); do
                  if docker exec ${MYSQL_CONTAINER} mysqladmin ping -h 127.0.0.1 -uroot -p"${DB_PASSWORD}" --silent >/dev/null 2>&1; then
                    echo "MySQL is ready."
                    exit 0
                  fi
                  sleep 2
                done

                echo "ERROR: MySQL did not become ready in time."
                docker logs --tail 50 ${MYSQL_CONTAINER} || true
                exit 1
                '''
            }
        }

        stage('Run New Container') {
            steps {
                sh '''
                set +x
                docker run -d \
                  --name ${APP_NAME} \
                  --network ${DOCKER_NETWORK} \
                  --restart unless-stopped \
                  -p ${APP_PORT}:9090 \
                  -e DB_HOST=${DB_HOST} \
                  -e DB_PORT=${DB_PORT} \
                  -e DB_NAME=${DB_NAME} \
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
