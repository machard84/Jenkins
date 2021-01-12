FROM            registry.chardma.org.uk:8443/centos:7
MAINTAINER      Mark Chard (machard.1984@gmail.com)
RUN             yum -y -q wget
RUN             wget -P /etc/yum.repos.d/ https://nexus.chardma.org.uk/repository/configs/yum.repos.d/elasticsearch.repo
RUN             yum install -y -q --enablerepo=elasticsearch elasticsearch shadow-utils
ENTRYPOINT      ["/usr/share/elasticsearch/bin/elasticsearch"]
CMD             ["-v"]
