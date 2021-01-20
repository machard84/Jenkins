FROM        registry.chardma.org.uk:8443/centos:7
MAINTAINER  machard.1984@gmail.com
RUN         yum -y -q install wget
WORKDIR     /tmp
RUN         wget -q --content-disposition https://www.tenable.com/downloads/api/v1/public/pages/nessus/downloads/9728/download?i_agree_to_tenable_license_agreement=true
RUN         yum localinstall -y -q Nessus*
RUN         rm Nessus*
ENTRYPOINT  ["/opt/nessus/sbin/nessus-service"]