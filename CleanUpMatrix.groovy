pipeline {
    agent none
    stages {
        stage("foo") {
            matrix {
                axes {
                    axis {
                        name 'FUNCTION'
                        values "image", "container", "volume", "network"
                    }
                    axis {
                        name 'NODE'
                        values "rpi2", "adh-030", "tristram"
                    }
                }
                stages {
                    stage("first") {
                        steps {
                            echo "First branch"
                            echo "FUNCTION=${FUNCTION}"
                            echo "HOSTNAME=${NODE}"
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