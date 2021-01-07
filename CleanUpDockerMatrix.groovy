pipeline {
    agent none
    stages {
        stage("Docker Clean-Up Matrix") {
            matrix {
                axes {
                    axis {
                         name 'NODE'
                         values "rpi2",
                                "tristram",
                                "predator"
                    }
                }
                stages {
                    stage("Get docker system info pre cleanup"){
                        agent {
                            label "${NODE}"
                        }
                        steps{
                            sh "docker system df"
                            sh "docker system info"
                        }
                    }
                    stage("Clean up") {
                        agent {
                            label "${NODE}"
                        }
                        steps {
                            sh "docker system prune -f"
                        }
                    }
                    stage("Get docker system info post cleanup") {
                        agent {
                            label "${NODE}"
                        }
                        steps {
                            sh "docker system df"
                        }
                    }
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