pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'develop', url: 'https://github.com/Les-chomeurs/PayeTonKawa.git'
            }
        }
        stage('Clean') {
            steps {
                bat './mvnw clean'
            }
        }
        stage('Compile'){
            steps{
                bat './mvnw compile'
            }
        }
        stage('Test'){
            steps{
                bat './mvnw test'
            }
        }
        stage('Sonar Validation') {
            steps {
                bat './mvnw verify sonar:sonar -Dsonar.login=634b3180737f5b08ef57317c4263be4fa13370cf -Dsonar.host.url=https://sonarcloud.io -Dsonar.organization=les-chomeurs -Dsonar.projectKey=Les-chomeurs_PayeTonKawa'
            }
        }
        stage('Package'){
            steps{
                bat './mvnw package'
            }
        }
       stage('Archive') {
            steps {
                bat 'rename target\\PayeTonKawa-0.0.1-SNAPSHOT.jar PayeTonKawa-%BUILD_NUMBER%.jar'
                archiveArtifacts artifacts: 'target\\PayeTonKawa-*.jar', followSymlinks: false
            }
        }
    }
}
