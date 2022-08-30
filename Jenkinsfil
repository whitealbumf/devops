node('jenkins-slave') {
    stage('prepare') {
        echo "1.prepare stage"
        checkout scm
        script {
            build_tag = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
            if (env.BRANCH_NAME != 'master') {
                build_tag = "${env.BRANCH_NAME}-${build_tag}"
            }
        }
    }


    stage('test') {
      echo "2.test stage"
    }


    stage('build') {
        echo "3.build docker image stage"
        sh "docker build -t 10.1.161.30:10014/library/app:${build_tag} ."
    }


    stage('push') {
        echo "4.push docker image stage"
        withCredentials([usernamePassword(credentialsId: 'harbor', usernameVariable: 'harborUser', passwordVariable: 'harborPassword')]) {
            sh "docker login 10.1.161.30:10014 -u ${harborUser} -p ${harborPassword}"
            sh "docker push 10.1.161.30:10014/library/app:${build_tag}"
        }
    }


    stage('deploy') {
        echo "5.deploy stage"
        if (env.BRANCH_NAME == 'master') {
            input "确认要部署线上环境吗？"
        }
        sh "sed -i 's#<build_tag>#${build_tag}#g' deploy.yaml"
        sh "sed -i 's#<BRANCH_NAME>#${env.BRANCH_NAME}#g' deploy.yaml"
        sh "kubectl apply -f deploy.yaml --record"
    }
}