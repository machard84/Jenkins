pipeline{
    agent { label "${HOST}"}
    stages{
        stage('Clean docker containers'){
            steps{
                docker container prune -f
            }
        }
        stage('Clean docker containers'){
            steps{
                docker image prune -f
            }
        }
        stage('Clean docker volumes'){
            steps{
                docker volume prune -f
            }
        }
        stage('Clean docker volumes'){
            steps{
                docker network prune -f
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