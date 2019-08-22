pipeline {
  agent {
      label 'maven'
  }
  stages {
    stage('Build App') {
      steps {
        sh "mvn install"
      }
    }
    stage('Create Image Builder') {
      when {
        expression {
          openshift.withCluster() {
            return !openshift.selector("bc", "mapit").exists();
          }
        }
      }
      steps {
        script {
          openshift.withCluster() {
            openshift.newBuild("--name=mapit", "--image-stream=redhat-openjdk18-openshift:1.1", "--binary")
          }
        }
      }
    }
    stage('Build Image') {
      steps {
        script {
          openshift.withCluster() {
            openshift.selector("bc", "mapit").startBuild("--from-file=target/mapit-spring.jar", "--wait")
          }
        }
      }
    }
    stage('Promote to DEV') {
      steps {
        script {
          openshift.withCluster() {
            openshift.tag("mapit:latest", "mapit:dev")
          }
        }
      }
    }
    stage('Create DEV') {
      when {
        expression {
          openshift.withCluster() {
            return !openshift.selector('dc', 'mapit-dev').exists()
          }
        }
      }
      steps {
        script {
          openshift.withCluster() {
            openshift.newApp("mapit:latest", "--name=mapit-dev").narrow('svc').expose()
          }
        }
      }
    }
    stage('Promote STAGE') {
      steps {
        script {
          openshift.withCluster() {
            openshift.tag("mapit:dev", "mapit:stage")
          }
        }
      }
    }
    stage('Create STAGE') {
      when {
        expression {
          openshift.withCluster() {
            return !openshift.selector('dc', 'mapit-stage').exists()
          }
        }
      }
      steps {
        script {
          openshift.withCluster() {
            openshift.newApp("mapit:stage", "--name=mapit-stage").narrow('svc').expose()
          }
        }
      }
    }
  }
}


// works
// pipeline {
//     agent any
//     stages {
//         stage('Build') {
//             steps {
//                echo 'This is a minimal pipeline.'
//             }
//         }
//     }
// }

// pipeline {
//   agent any
//   stages {
//     stage('Build') {
//       stage('Checkout') {
//           checkout scm
//         }
//         stage('Build') {
//           sh 'mvn install'
//         }
//         stage('Unit Test') {
//           sh 'mvn test'
//         }
//     }
//   }
// }


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