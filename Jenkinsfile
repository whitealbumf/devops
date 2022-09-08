def registry = "10.1.161.30:10014"
def project = "library"
def app_name = "app"
def app_image = "${registry}/${project}/${app_name}:${build_tag}"

pipeline {
    agent {
        kubernetes {
            cloud "kubernetes"
            inheritFrom "jenkins-slave"
            yamlFile "jenkins-slave.yaml"
        }
    }

    stages {
        stage('prepare') {
            steps {
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
                echo "skip test"
            }
        }


        stage('package') {
                    steps {
                        sh "mvn clean package -Dmaven.test.skip=true"
                    }
                }


        stage('build and push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'harbor', usernameVariable: 'harborUser', passwordVariable: 'harborPassword')]) {
                    sh "docker login ${registry} -u ${harborUser} -p ${harborPassword}"
                    sh "docker build -t ${app_image} ."
                    sh "docker push ${app_image}"
                }
            }
        }


        stage('deploy') {
            steps {
                script {
                    if (env.BRANCH_NAME == 'master') {
                        input "确认要部署线上环境吗？"
                    }
                }
                sh "sed -i 's#<app_image>#${app_image}#g' deploy.yaml"
                //sh "sed -i 's#<BRANCH_NAME>#${env.BRANCH_NAME}#g' deploy.yaml"
                sh "echo export KUBECONFIG=/etc/kubernetes/admin.conf >> /etc/profile"
                sh "kubectl apply -f deploy.yaml --record"
                echo "docker system and images clean"
                sh "docker system prune"
            }
        }
    }
}