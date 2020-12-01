pipeline {
  environment {
    registry = "gcr.io/concrete-crow-297015/ahmet-app"
    registryCredential = 'gcr-credentials'
    dockerImage = ''
  }
  agent any
  stages {
    stage('Git Checkout & Build & Test') {
      steps {
        git 'https://github.com/ahmetbyzl/spring-hellohepsiburada/'
        
        sh 'mvn -B -DskipTests clean package'
        
        sh 'mvn test'
        
      }
    }
    stage('Building image') {
      steps{
        script {
        
          dockerImage = docker.build registry + ":$BUILD_NUMBER"
          dockerImage = docker.build registry + ":latest"
        }
      }
    }
    stage('Deploy Image') {
      steps{
        script {
          docker.withRegistry("https://gcr.io", "gcr:gcr-credentials") {
            dockerImage.push()
          }
        }
      }
    }
    stage('Remove Unused docker image') {
      steps{
        sh "docker rmi $registry:$BUILD_NUMBER"
      }
    }
    stage('Deploy k8s') {
      steps{
          
          
          // withCredentials([usernameColonPassword(credentialsId: 'gcr:gcr-credentials', variable: 'google-cred')]) {
          
          
       // sh'''
        //    #!/bin/bash
            
          //  a=$(echo $(kubectl get deployments | grep spring-hello | wc -l))
          //  if [ $a -ge 1 ]
         //   then
        //    kubectl patch deployment spring-hello -p "{\"spec\":{\"template\":{\"spec\":{\"containers\":[{\"name\":\"spring-hello\",\"image\":\"\$registry:\$BUILD_NUMBER\"}]}}}}"

            
          //  else
                    sh 'cd /var/lib/jenkins/workspace/kubernetes-deploy '
                    sh 'pwd'
                    sh 'kubectl apply -f template-k8s.yml'
      //      fi
    //    '''
        // }
      }
    } 
  }
}