pipeline {
  agent any
  stages {
    stage('prepare') {
      steps {
        echo '1.prepare stage'
        checkout scm
        script {
          build_tag = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
          if (env.BRANCH_NAME != 'master') {
            build_tag = "${env.BRANCH_NAME}-${build_tag}"
          }
        }

      }
    }

    stage('test') {
      steps {
        echo '2.test stage'
      }
    }

    stage('build') {
      steps {
        echo '3.build docker image stage'
        sh "docker build -t 10.1.161.30:10014/library/app:${build_tag} ."
      }
    }

    stage('push') {
      steps {
        echo '4.push docker image stage'
        withCredentials(bindings: [usernamePassword(credentialsId: 'harbor', usernameVariable: 'harborUser', passwordVariable: 'harborPassword')]) {
          sh "docker login 10.1.161.30:10014 -u ${harborUser} -p ${harborPassword}"
          sh "docker push 10.1.161.30:10014/library/app:${build_tag}"
        }

      }
    }

    stage('deploy') {
      steps {
        echo '5.deploy stage'
        script {
          if (env.BRANCH_NAME == 'master') {
            input "确认要部署线上环境吗？"
          }
        }

        sh "sed -i 's#<build_tag>#${build_tag}#g' deploy.yaml"
        sh "sed -i 's#<BRANCH_NAME>#${env.BRANCH_NAME}#g' deploy.yaml"
        sh 'kubectl apply -f deploy.yaml --record'
      }
    }

  }
}