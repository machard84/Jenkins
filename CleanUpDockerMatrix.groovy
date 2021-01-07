pipeline {
    agent none
    stages {
        stage("foo") {
            matrix {
                node {
                    label "${NODE}"
                }
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
