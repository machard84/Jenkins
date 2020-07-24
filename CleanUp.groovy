def jobs = [
        'container',
        'image',
        'network',
        'volume',
]
def parallelStagesMap = jobs.collectEntries{
    ["${it}" : generateStage(it)]
}
def generateStage(job){
    return{
        stage("Debug info for: ${HOST}"){
            sh 'cat /etc/hostname'
            sh 'echo ${PATH}'
            sh 'whoami'
            sh 'pwd'
            sh 'ls /usr/bin -l'
        }
        stage("Clean up docker images on: ${HOST}") {
            withEnv(['PATH+EXTRA=/usr/bin']) {
                sh "/usr/bin/docker ${job} prune -f"
            }
        }
    }
}
pipeline{
    agent { label "${HOST}"}
    stages{
        stage('CleanUp'){
            steps{
                sh 'echo Cleaning up'
                script {
                    parallel parallelStagesMap
                }
            }
        }
    }
    post{
        success {
            echo 'One way or another, I have finished'
            cleanWs()
            deleteDir()
        }
    }
}