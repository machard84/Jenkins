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
                            label "${NODE}"
                        }
                        steps {
                            sh 'docker ${FUNCTION} prune -f'
                        }
                    }
                    stage("Show whats left") {
                        agent {
                            label "${NODE   }"
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
