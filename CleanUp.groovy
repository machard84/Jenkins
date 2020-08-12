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
                script {
                    parallel parallelStagesMap
                }
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