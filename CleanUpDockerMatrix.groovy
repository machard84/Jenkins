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
                    stage("Clean up docker") {
                        agent {
                            "${NODE}"
                        }
                        steps {
                            sh 'docker ${FUNCTION} prune -f'
                        }
                    }
                    stage("Show whats left") {
                        agent "${NODE}"
                        steps {
                            sh 'docker ${FUNCTION} ls'
                        }
                    }
                }
            }
        }
    }
}
