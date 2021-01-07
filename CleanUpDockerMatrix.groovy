pipeline {
    agent none
    stages {
        stage("foo") {
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
                            sh "docker ${FUNCTION} prune -f"
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
