FROM        registry.chardma.org.uk:8443/debian:latest
MAINTAINER  machard.1984@gmail.com

RUN         echo 'debconf debconf/frontend select Noninteractive' | debconf-set-selections

RUN         apt-get install -y locales
RUN         locale-gen en_US.UTF-8

ENV         export LC_ALL="C"
ENV         export DEBIAN_FRONTEND=noninteractive
ENV         export PKG_CONFIG_PATH=/opt/gvm/lib/pkgconfig:$PKG_CONFIG_PATH

RUN         apt install -y software-properties-common
RUN         apt install -y cmake pkg-config libglib2.0-dev libgpgme-dev libgnutls28-dev uuid-dev libssh-gcrypt-dev \
                libldap2-dev doxygen graphviz libradcli-dev libhiredis-dev libpcap-dev bison libksba-dev libsnmp-dev \
                gcc-mingw-w64 heimdal-dev libpopt-dev xmltoman redis-server xsltproc libical-dev postgresql \
                postgresql-contrib postgresql-server-dev-all gnutls-bin nmap rpm nsis curl wget fakeroot gnupg \
                sshpass socat snmp smbclient libmicrohttpd-dev libxml2-dev python-polib gettext rsync xml-twig-tools \
                python3-paramiko python3-lxml python3-defusedxml python3-pip python3-psutil virtualenv vim git xsltproc xmltoman
RUN         apt install -y texlive-latex-extra --no-install-recommends
RUN         apt install -y texlive-fonts-recommended

RUN         curl -sS https://dl.yarnpkg.com/debian/pubkey.gpg | apt-key add -
RUN         echo "deb https://dl.yarnpkg.com/debian/ stable main" > /etc/apt/sources.list.d/yarn.list
RUN         apt update
RUN         apt -y install yarn

RUN         curl -sL https://deb.nodesource.com/setup_10.x | bash -
RUN         apt update
RUN         apt install -y nodejs

RUN         echo 'export PATH="$PATH:/opt/gvm/bin:/opt/gvm/sbin:/opt/gvm/.local/bin"' | tee -a /etc/profile.d/gvm.sh
RUN         chmod 0755 /etc/profile.d/gvm.sh
RUN         ./etc/profile.d/gvm.sh
RUN         echo "/opt/gvm/lib" > /etc/ld.so.conf.d/gvm.conf

RUN         mkdir /opt/gvm
RUN         adduser gvm --disabled-password --home /home/gvm
RUN         usermod -aG redis gvm
RUN         chown gvm:gvm /opt/gvm/

USER        gvm
RUN         mkdir /opt/gvm/src
WORKDIR     /opt/gvm/src
RUN         git clone -b gvm-libs-20.08 --single-branch  https://github.com/greenbone/gvm-libs.git
RUN         git clone -b openvas-20.08 --single-branch https://github.com/greenbone/openvas.git
RUN         git clone -b gvmd-20.08 --single-branch https://github.com/greenbone/gvmd.git
RUN         git clone -b master --single-branch https://github.com/greenbone/openvas-smb.git
RUN         git clone -b gsa-20.08 --single-branch https://github.com/greenbone/gsa.git
RUN         git clone -b ospd-openvas-20.08 --single-branch  https://github.com/greenbone/ospd-openvas.git
RUN         git clone -b ospd-20.08 --single-branch https://github.com/greenbone/ospd.git
RUN         git clone -b master --single-branch https://github.com/greenbone/pheme.git
USER        root

WORKDIR     /opt/gvm/src/gvm-libs
RUN         mkdir build
WORKDIR     /opt/gvm/src/gvm-libs/build
RUN         cmake ..
RUN         make
RUN         make install

WORKDIR     /opt/gvm/src/openvas
RUN         mkdir build
WORKDIR     /opt/gvm/src/openvas/build
RUN         cmake ..
RUN         make
RUN         make install

WORKDIR     /opt/gvm/src/gvmd
RUN         mkdir build
WORKDIR     /opt/gvm/src/gvmd/build
RUN         cmake ..
RUN         make
RUN         make install

WORKDIR     /opt/gvm/src/openvas-smb
RUN         mkdir build
WORKDIR     /opt/gvm/src/openvas-smb/build
RUN         cmake ..
RUN         make
RUN         make install

WORKDIR     /opt/gvm/src/gsa
RUN         mkdir build
WORKDIR     /opt/gvm/src/gsa/build
RUN         cmake ..
RUN         make
RUN         make install

WORKDIR     /opt/gvm/src/ospd-openvas
RUN         mkdir build
WORKDIR     /opt/gvm/src/ospd-openvas/build
RUN         cmake ..
RUN         make
RUN         make install

WORKDIR     /opt/gvm/src/ospd
RUN         mkdir build
WORKDIR     /opt/gvm/src/ospd/build
RUN         cmake ..
RUN         make
RUN         make install

WORKDIR     /opt/gvm/src/pheme
RUN         mkdir build
WORKDIR     /opt/gvm/src/pheme/build
RUN         cmake ..
RUN         make
RUN         make install