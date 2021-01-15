FROM            registry.chardma.org.uk:8443/centos:7
MAINTAINER      Mark Chard (machard.1984@gmail.com)
RUN             yum install -y -q shadow-utils wget
RUN             wget -P /etc/yum.repos.d/ https://nexus.chardma.org.uk/repository/configs/yum.repos.d/ntop-ng.repo --no-check-certificate
RUN             wget -P /etc/yum.repos.d/ https://nexus.chardma.org.uk/repository/configs/yum.repos.d/epel.repo --no-check-certificate
RUN             yum install -y -q ntopng mysql-devel
RUN             touch /etc/ntopng/ntopng.start
COPY            conf.d/ntopng.conf /etc/ntopng/ntopng.conf
EXPOSE          3443 5666
ENTRYPOINT      ["/usr/bin/ntopng"]
CMD             ["-W", "3443", "-G", "/var/tmp/ntopng.pid", "-i", "tcp://0.0.0.0:5666c", "-r", "redis:6379:syslog", "--community", "-F", "mysql;mysql;ntopng;flows;ntopng;ntopng"]