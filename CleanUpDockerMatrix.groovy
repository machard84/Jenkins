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
                    stage {
                        agent {
                            label "${NODE}"
                        }
                        steps {
                            sh "docker ${FUNCTION} prune -f"
                        }
                    }
                    stage {
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
