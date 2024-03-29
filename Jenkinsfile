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

//     oc new-app fabric8/s2i-java:latest-java11~https://github.com/marzelwidmer/camel-demo; oc expose svc/camel-demo; oc get route camel-demo

    stage('Create Image Builder') {
      when {
        expression {
          openshift.withCluster() {
            return !openshift.selector("bc", "camel-demo").exists();
          }
        }
      }
      steps {
        script {
          openshift.withCluster() {
            openshift.newBuild("--name=camel-demo", "--image-stream=redhat-openjdk18-openshift:1.1", "--binary")
          }
        }
      }
    }
    stage('Build Image') {
      steps {
        script {
          openshift.withCluster() {
            openshift.selector("bc", "camel-demo").startBuild("--from-file=target/camel-demo-0.0.1-SNAPSHOT.jar", "--wait")
          }
        }
      }
    }
    stage('Promote to DEV') {
      steps {
        script {
          openshift.withCluster() {
            openshift.tag("camel-demo:latest", "camel-demo:dev")
          }
        }
      }
    }
    stage('Create DEV') {
      when {
        expression {
          openshift.withCluster() {
            return !openshift.selector('dc', 'camel-demo-dev').exists()
          }
        }
      }
      steps {
        script {
          openshift.withCluster() {
            openshift.newApp("camel-demo:latest", "--name=camel-demo-dev").narrow('svc').expose()
          }
        }
      }
    }
    stage('Promote STAGE') {
      steps {
        script {
          openshift.withCluster() {
            openshift.tag("camel-demo:dev", "camel-demo:stage")
          }
        }
      }
    }
    stage('Create STAGE') {
      when {
        expression {
          openshift.withCluster() {
            return !openshift.selector('dc', 'camel-demo-stage').exists()
          }
        }
      }
      steps {
        script {
          openshift.withCluster() {
            openshift.newApp("camel-demo:stage", "--name=camel-demo-stage").narrow('svc').expose()
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