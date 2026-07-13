pipeline {
    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven3'
    }

    environment {
        IMAGE_NAME = "shek07/ott-platform"
        IMAGE_TAG = "${BUILD_NUMBER}"
        CONTAINER_NAME = "ott-platform-app"

        DOCKER_USER = credentials('DOCKERHUB_CREDENTIALS')
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Building application...'
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                echo 'Running tests...'
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                echo 'Packaging application...'
                sh 'mvn clean package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Building Docker image...'

                sh """
                docker build \
                -t ${IMAGE_NAME}:${IMAGE_TAG} \
                -t ${IMAGE_NAME}:latest .
                """
            }
        }

        stage('Docker Login') {
            steps {
                sh '''
                echo "$DOCKER_USER_PSW" | docker login -u "$DOCKER_USER_USR" --password-stdin
                '''
            }
        }

        stage('Docker Push') {
            steps {
                sh """
                docker push ${IMAGE_NAME}:${IMAGE_TAG}
                docker push ${IMAGE_NAME}:latest
                """
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying container...'

                sh '''
                docker rm -f ott-platform-app || true

                docker run -d \
                  --name ott-platform-app \
                  --network bridge \
                  -p 8081:8080 \
                  -e SPRING_DATASOURCE_URL=jdbc:mysql://172.17.0.1:3306/employee_db \
                  -e SPRING_DATASOURCE_USERNAME=admin \
                  -e SPRING_DATASOURCE_PASSWORD=admin123 \
                  shek07/ott-platform:latest
                '''
            }
        }
    }

    post {

        success {
            echo 'Pipeline completed successfully.'
        }

        failure {
            echo 'Pipeline failed.'
        }

        always {
            sh 'docker logout || true'
        }
    }
}
