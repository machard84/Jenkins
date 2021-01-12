FROM            registry.chardma.org.uk:8443/centos:7
MAINTAINER      Mark Chard (machard.1984@gmail.com)
RUN             yum -y -q install wget
RUN             wget -P /etc/yum.repos.d/ https://nexus.chardma.org.uk/repository/configs/yum.repos.d/syslog-ng.repo --no-check-certificate
RUN             yum install -y -q pki-base
RUN             yum install -y -q syslog-ng syslog-ng-sql syslog-ng-http libdbi-dbd-mysql libdbi-drivers libdbi
COPY            conf.d/syslog-ng.conf /etc/syslog-ng/syslog-ng.conf
COPY            conf.d/bro.conf /etc/syslog-ng/conf.d/
COPY            conf.d/snort.conf /etc/syslog-ng/conf.d/
COPY            conf.d/suricata.conf /etc/syslog-ng/conf.d/
COPY            conf.d/s_network.conf /etc/syslog-ng/conf.d/
COPY            conf.d/s_sys.conf /etc/syslog-ng/conf.d/
COPY            conf.d/d_mongodb.conf /etc/syslog-ng/conf.d/
COPY            conf.d/log.conf /etc/syslog-ng/conf.d/
COPY            conf.d/syslog-ng /etc/sysconfig/syslog-ng
COPY            conf.d/patterndb.xml        /etc/syslog-ng/patterndb.xml
EXPOSE          514 514/udp 6514
ENTRYPOINT      ["/sbin/syslog-ng"]
CMD             ["--pidfile=/tmp/syslog-ng.pid", "--debug", "--persist-file=/tmp/syslog-ng.persist-", "--control=/tmp/syslog-ng.ctl", "--foreground", "--stderr", "--no-caps"]
