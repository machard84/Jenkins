pipeline {
    agent none
    stages {
        stage("Docker Clean-Up Matrix") {
            matrix {
                axes {
                    axis {
                        name 'FUNCTION'
                        values "container", "image", "network", "volume"
                    }
                    axis {
                         name 'NODE'
                         values "rpi2", "tristram", "predator"
                    }
                }
                stages {
                    stage("Clean up") {
                        agent {
                            label "${NODE}"
                        }
                        steps {
                            sh "docker ${FUNCTION} prune -f"
                            sh 'docker ${FUNCTION} ls'
                        }
                    }
                }
            }
        }
    }
}
