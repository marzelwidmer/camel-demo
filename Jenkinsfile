pipeline {
  agent any
  stages {
    stage('Build') {
      stage('Checkout') {
          checkout scm
        }
        stage('Build') {
          sh 'mvn install'
        }
        stage('Unit Test') {
          sh 'mvn test'
        }
    }
  }
}
