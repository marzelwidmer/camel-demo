// see : https://blog.openshift.com/building-declarative-pipelines-openshift-dsl-plugin/
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
            return !openshift.selector("bc", "camel").exists();
          }
        }
      }
      steps {
        script {
          openshift.withCluster() {
            openshift.newBuild("--name=camel", "--image-stream=redhat-openjdk18-openshift:1.1", "--binary")
          }
        }
      }
    }
    stage('Build Image') {
      steps {
        script {
          openshift.withCluster() {
            openshift.selector("bc", "camel").startBuild("--from-file=target/camel-0.0.1-SNAPSHOT.jar", "--wait")
          }
        }
      }
    }
    stage('Promote to DEV') {
      steps {
        script {
          openshift.withCluster() {
            openshift.tag("camel:latest", "camel:dev")
          }
        }
      }
    }
    stage('Create DEV') {
      when {
        expression {
          openshift.withCluster() {
            return !openshift.selector('dc', 'camel-dev').exists()
          }
        }
      }
      steps {
        script {
          openshift.withCluster() {
            openshift.newApp("camel:latest", "--name=camel-dev").narrow('svc').expose()
          }
        }
      }
    }
    stage('Promote STAGE') {
      steps {
        script {
          openshift.withCluster() {
            openshift.tag("camel:dev", "camel:stage")
          }
        }
      }
    }
    stage('Create STAGE') {
      when {
        expression {
          openshift.withCluster() {
            return !openshift.selector('dc', 'camel-stage').exists()
          }
        }
      }
      steps {
        script {
          openshift.withCluster() {
            openshift.newApp("camel:stage", "--name=camel-stage").narrow('svc').expose()
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