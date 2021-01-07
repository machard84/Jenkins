pipeline {
    agent none
    stages {
        stage("foo") {
            matrix {
                agent {
                    label "${NODE}"
                }
                axes {
                    axis {
                        name 'FUNCTION'
                        values "image", "container", "volume", "network"
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
                }
                stage("second") {
                   steps {
        				echo "Second branch"
                    }
                }
            }
        }
    }
}