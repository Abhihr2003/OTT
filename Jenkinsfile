pipeline {
    agent any

    tools {
        maven 'Maven3'   // Name must match a Maven installation configured in Jenkins > Global Tool Configuration
        jdk 'JDK17'      // Name must match a JDK installation configured in Jenkins > Global Tool Configuration
    }

    environment {
        // Change these to match your own Docker Hub / registry account
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials') // Jenkins credential ID (username/password)
        DOCKER_IMAGE           = "shek07/ott-platform"
        IMAGE_TAG              = "${env.BUILD_NUMBER}"
    }

    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '10'))
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
                echo 'Building with Maven...'
                sh 'mvn -B clean compile'
            }
        }

        stage('Test') {
            steps {
                echo 'Running unit tests...'
                sh 'mvn -B test'
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports/*.xml',
			allowEmptyResults: true
                }
            }
        }

        stage('Package') {
            steps {
                echo 'Packaging application as JAR...'
                sh 'mvn -B package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Building Docker image...'
                sh "docker build -t ${DOCKER_IMAGE}:${IMAGE_TAG} -t ${DOCKER_IMAGE}:latest ."
            }
        }

        stage('Docker Push') {
            steps {
                echo 'Pushing Docker image to registry...'
                sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
                sh "docker push ${DOCKER_IMAGE}:${IMAGE_TAG}"
                sh "docker push ${DOCKER_IMAGE}:latest"
            }
        }
	
	stage('Deploy') {
    steps {
        sh '''
        docker rm -f ott-platform-app || true

        docker run -d \
          --name ott-platform-app \
          --network ott_default \
          -p 8081:8080 \
          -e SPRING_DATASOURCE_URL=jdbc:mysql://ott-mysql:3306/ott_db \
          -e SPRING_DATASOURCE_USERNAME=admin \
          -e SPRING_DATASOURCE_PASSWORD=admin123 \
          shek07/ott-platform:latest
        '''
    }
}


    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Check the logs above.'
        }
        always {
            sh 'docker logout || true'
        }
    }
}
