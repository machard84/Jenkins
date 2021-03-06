pipeline{
    environment {
        REGISTRY = 'registry.chardma.org.uk:8443'
        CENTOS_URL = 'http://mirror.centos.org/centos/${CENTOS_VERSION}/os/x86_64/Packages/'
        CENTOS_RELEASE = sh(
            returnStdout: true,
            script: 'curl -s  http://mirror.centos.org/centos/${CENTOS_VERSION}/os/x86_64/Packages/ | grep -oE centos-release-[a-z0-9.-]+.centos.x86_64.rpm | uniq'
        )
        FEDORA_URL = 'https://rpmfind.net/linux/fedora/linux/releases/${FEDORA_VERSION}/Everything/x86_64/os/Packages/f/'
        FEDORA_RELEASE = sh(
            returnStdout: true,
            script: 'curl -s https://rpmfind.net/linux/fedora/linux/releases/${FEDORA_VERSION}/Everything/x86_64/os/Packages/f/ | grep -oE fedora-release-[0-9-]+.noarch.rpm'
        )
        FEDORA_REPOS = sh(
            returnStdout: true,
            script: 'curl -s https://rpmfind.net/linux/fedora/linux/releases/${FEDORA_VERSION}/Everything/x86_64/os/Packages/f/ | grep -oE fedora-repos-${FEDORA_VERSION}-[0-9]+.noarch.rpm'
        )
        FEDORA_GPG = sh(
            returnStdout: true,
            script: 'curl -s https://rpmfind.net/linux/fedora/linux/releases/${FEDORA_VERSION}/Everything/x86_64/os/Packages/f/ | grep -oE fedora-gpg-keys-${FEDORA_VERSION}-[0-9]+.noarch.rpm'
        )
    }
    agent { label "$HOST" }
    stages{
        stage('Create CentOS image') {
            steps{
                dir('centos'){
                    sh "wget --proxy=${PROXY} ${CENTOS_URL}/${CENTOS_RELEASE}"
                    sh "sudo rpm -ivh --force --root=${WORKSPACE}/centos --nodeps ${CENTOS_RELEASE}"
                    sh "sudo yum --installroot=${WORKSPACE}/centos --noplugins --nogpgcheck --releasever=7 install -y yum yum-plugin-ovl wget"
                    sh 'sudo rm -f etc/yum.repos.d/*.repo'
                    sh 'sudo wget -P etc/yum.repos.d/ http://nexus.chardma.org.uk/repository/configs/yum.repos.d/CentOS.repo'
                }
            }
        }
        stage('Create Fedora image') {
            steps{
                dir('fedora') {
                    sh "wget --proxy=${PROXY} ${FEDORA_URL}/${FEDORA_RELEASE}"
                    sh "wget --proxy=${PROXY} ${FEDORA_URL}/${FEDORA_REPOS}"
                    sh "wget --proxy=${PROXY} ${FEDORA_URL}/${FEDORA_GPG}"
                    sh "sudo rpm -ivh --force --root=${WORKSPACE}/fedora/ --nodeps ${FEDORA_RELEASE}"
                    sh "sudo rpm -ivh --force --root=${WORKSPACE}/fedora/ --nodeps ${FEDORA_GPG}"
                    sh "sudo rpm -ivh --force --root=${WORKSPACE}/fedora/ --nodeps ${FEDORA_REPOS}"
                }
                sh 'sudo yum --installroot=${WORKSPACE}/fedora --noplugins --nogpgcheck --releasever=${FEDORA_VERSION} install -y dnf dnf-plugin-ovl wget'
            }
        }
        stage('Create Debian image'){
            steps{
                dir('debian'){
                    sh 'sudo debootstrap --arch amd64 ${DEBIAN_VERSION} ./ http://ftp.debian.org/debian/'
                }
            }
        }
        stage('Create Ubuntu image'){
            steps{
                dir('ubuntu'){
                    sh 'sudo debootstrap --arch amd64 ${UBUNTU_VERSION} ./ http://ftp.ubuntu.com/ubuntu/'
                }
            }
        }
        stage('Matrix push docker images'){
            matrix{
                axes{
                    axis{
                        name    OS
                        values  "centos",
                                "fedora",
                                "debian",
                                "ubuntu"
                    }
                }
                stages{
                    stage("remove cache directory from image") {
                        steps {
                            sh "sudo rm -rf ${WORKSPACE}/${OS}/var/cache/*"
                        }
                    }
                    stage("Convert chroot to container") {
                        steps {
                            sh "sudo tar -C ${WORKSPACE}/${OS} -c . | docker import - ${REGISTRY}/${OS}:latest"
                        }
                    }
                    stage("Push images to registry") {
                        steps {
                            sh "docker image push ${REGISTRY}/${OS}:latest"
                        }
                    }
                }
            }
        }
        stage("Chown the workdirs so we can clean up"){
            steps {
                sh 'sudo chown jenkins: ${WORKSPACE} -R'
            }
        }
    }
    post {
        always {
            echo 'Clean up'
            cleanWs()
            deleteDir()
        }
        success {
            echo 'Success'
        }
        failure {
            echo 'Fail'
        }
    }
}