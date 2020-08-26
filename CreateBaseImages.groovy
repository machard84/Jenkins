def jobs = [
        "centos",
        "fedora",
        "debian",
        "ubuntu",
]
def parallelStagesMap = jobs.collectEntries{
    ["${it}" : generateStage(it)]
}
def generateStage(job){
    return{
        stage("remove cache directory from ${job} image") {
            sh "sudo rm -rf ${WORKSPACE}/${job}/var/cache/*"
        }
        stage("import ${job} image") {
            sh "sudo tar -C ${WORKSPACE}/${job} -c . | docker import - 127.0.0.1:5000/${job}:latest"
        }
        stage("push ${job} image to cluster local repository") {
            sh "docker image push 127.0.0.1:5000/${job}:latest"
        }
    }
}
pipeline{
    environment {
        CENTOS_URL = 'http://mirror.centos.org/centos/${CENTOS_VERSION}/os/x86_64/Packages/'
        CENTOS_RELEASE = sh(
                returnStdout: true,
                script: 'curl -s  http://mirror.centos.org/centos/${CENTOS_VERSION}/os/x86_64/Packages/ | grep -oE centos-release-[a-z0-9.-]+.centos.x86_64.rpm | uniq'
        )
        FEDORA_URL = 'http://rpmfind.net/linux/fedora/linux/updates/${FEDORA_VERSION}/Everything/x86_64/Packages/f/'
        FEDORA_RELEASE = sh(
                returnStdout: true,
                script: 'curl -s http://rpmfind.net/linux/fedora/linux/updates/${FEDORA_VERSION}/Everything/x86_64/Packages/f/ | grep -oE fedora-release-[0-9-]+.noarch.rpm'
        )
        FEDORA_REPOS = sh(
                returnStdout: true,
                script: 'curl -s http://rpmfind.net/linux/fedora/linux/updates/${FEDORA_VERSION}/Everything/x86_64/Packages/f/ | grep -oE fedora-repos-${FEDORA_VERSION}-[0-9]+.noarch.rpm'
        )
        FEDORA_GPG = sh(
                returnStdout: true,
                script: 'curl -s http://rpmfind.net/linux/fedora/linux/updates/${FEDORA_VERSION}/Everything/x86_64/Packages/f/ | grep -oE fedora-gpg-keys-${FEDORA_VERSION}-[0-9]+.noarch.rpm'
        )
    }
    agent { label "$HOST" }
    stages{
        stage('Create CentOS image') {
            steps{
                dir('centos'){
                    wget --proxy=${PROXY} ${CENTOS_URL}/${CENTOS_RELEASE}
                    sudo rpm -ivh --force --root=${WORKSPACE}/centos --nodeps ${CENTOS_RELEASE}
                    sudo yum --installroot=${WORKSPACE}/centos --noplugins --nogpgcheck --releasever=7 install -y yum yum-plugin-ovl
                }
            }
        }
        stage('Change the CentOS repository') {
            steps{
                dir('centos/etc/yum.repos.d'){
                    sudo rm -f *.repo
                    sudo wget http://10.0.33.55:8081/repository/configs/yum.repos.d/CentOS.repo
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
                sh 'sudo yum --installroot=${WORKSPACE}/fedora --noplugins --nogpgcheck --releasever=${FEDORA_VERSION} install -y dnf yum-plugin-ovl'
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
    }
    post{
        success{
            script{
                parallel parallelStagesMap
                updateGitlabCommitStatus name: 'build', state: 'success'
                cleanWs()
                deleteDir()
            }
        }
        failure{
            script{
                updateGitlabCommitStatus name: 'build', state: 'failed'
                cleanWs()
                deleteDir()
            }
        }

    }
}