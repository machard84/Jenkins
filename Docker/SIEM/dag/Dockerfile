FROM        registry.chardma.org.uk:8443/centos:7
MAINTAINER  Mark Chard (machard.1984@gmail.com)
RUN         yum -y -q install wget
RUN         wget -P /etc/yum.repos.d/ https://nexus.chardma.org.uk/repository/configs/yum.repos.d/endace.repo --no-check-certificate
RUN         wget -P /etc/yum.repos.d/ https://nexus.chardma.org.uk/repository/configs/yum.repos.d/epel.repo --no-check-certificate
RUN         yum install -y -q dag-*
RUN         rm -rf /var/cache/yum