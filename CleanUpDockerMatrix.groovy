def tasks = [
    "container",
    "image",
    "network",
    "volume",
]
def hosts = [
    "container",
    "volume",
    "image",
    "network"
]
pipeline {
    agent none
    stages {
        stage("foo") {
            matrix {
                axes {
                    axis {
                        name 'FUNCTION'
                        values "${tasks}"
                    }
                    axis {
                         name 'NODE'
                         values "${hosts}"
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
