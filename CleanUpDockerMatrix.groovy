def tasks = [
    "container",
    "image",
    "network",
    "volume",
]
def parallelStagesMap = tasks.collectEntries{
    ["${it}" : generateStage(it)]
}
def generateStage(task){
    return{
        stage("Clean up ${task}") {
            sh "docker ${task} prune -f"
        }
    }
}
pipeline {
    agent none
    stages {
        stage("foo") {
            matrix {
                axes {
                    axis {
                        name 'FUNCTION'
                        values "container", "volume", "image", "network"
                    }
                    axis {
                         name 'NODE'
                         values "rpi2", "predator", "tristram"
                    }
                }
                stages {
                    stage("Get Hostname") {
                        agent {
                            label "${NODE}"
                        }
                        steps {
                            sh 'hostname -f'
                        }
                    }
                    stage("Clean up docker") {
                        agent {
                            label "${NODE}"
                        }
                        steps {
                            parallel parallelStagesMap
                        }
                    }
                    stage("Show whats left") {
                        agent {
                            label "${NODE}"
                        }
                        steps {
                            sh 'docker ${FUNCTION} ls'
                        }
                    }
                }
            }
        }
    }
}
