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
                            docker ${FUNCTION} prune -f
                        }
                    }
                }
                stage("Show whats left") {
                    steps {
        				docker ${FUNCTION} ls
                    }
                }
            }
        }
    }
}
