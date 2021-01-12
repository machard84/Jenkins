FROM            registry.chardma.org.uk:8443/dag:latest
MAINTAINER      Mark Chard (machard.1984@gmail.com)
RUN             yum -y -q install wget
RUN             wget -P /etc/yum.repos.d/ https://nexus.chardma.org.uk/repository/configs/yum.repos.d/epel.repo --no-check-certificate
RUN             yum install -y -q https://www.snort.org/downloads/snort/snort-2.9.16.1-1.centos8.x86_64.rpm libdnet
RUN             ln -s /usr/lib64/libdnet.so.1.0.1 /usr/lib64/libdnet.1
ENTRYPOINT      ["snort"]
CMD             ["-U", "-b", "-l", "/var/log/snort"]
