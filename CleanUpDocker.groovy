pipeline{
    agent { label "${HOST}"}
    stages{
        stage('Clean docker containers'){
            steps{
                sh 'docker container prune -f'
            }
        }
        stage('Clean docker images'){
            steps{
                sh 'docker image prune -f'
            }
        }
        stage('Clean docker volumes'){
            steps{
                sh 'docker volume prune -f'
            }
        }
        stage('Clean docker networks'){
            steps{
                sh 'docker network prune -f'
            }
        }
    }
    post{
        success {
            cleanWs()
            deleteDir()
        }
    }
}