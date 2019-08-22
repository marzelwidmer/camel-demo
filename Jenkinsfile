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


// pipeline {
// //   agent {
// //     docker {
// //       image 'maven:3.3.9-jdk-8'
// //       args '-v /root/.m2:/root/.m2'
// //     }
//     agent any
//   }
//   stages {
//     stage('Initialize ') {
//       steps {
//         sh '''echo PATH = ${PATH}
// echo M2_HOME = {M2_HOME}
// mvn clean'''
//       }
//     }
//     stage('Build') {
//       steps {
//         sh 'mvn -Dmaven.test.failure.ignore=true install'
//       }
//     }
//     stage('Report') {
//       steps {
//         junit 'target/surefire-reports/ **/*.xml'
//       }
//     }
//   }
// }