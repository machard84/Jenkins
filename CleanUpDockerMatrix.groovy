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
                    agent "${NODE}"
                    stage("Clean up docker") {
                        steps {
                            sh 'docker ${FUNCTION} prune -f'
                        }
                    }
                    stage("Show whats left") {
                        steps {
                            sh 'docker ${FUNCTION} ls'
                        }
                    }
                }
            }
        }
    }
}
