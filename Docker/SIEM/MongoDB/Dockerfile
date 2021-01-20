FROM        registry.chardma.org.uk:8443/centos:7
MAINTAINER  machard.1984@gmail.com
RUN         yum install -y -q shadow-utils systemd wget
RUN         wget -P /etc/yum.repos.d/ http://nexus.chardma.org.uk/repository/configs/yum.repos.d/mongodb.repo
RUN         yum install -y -q mongodb-org
RUN         rpm -e --nodeps systemd
RUN         yum remove -y -q device-mapper dracut elfutils-default-yama-scope dbus
RUN         mkdir /data/db -p
RUN         chown mongod: /data -R
ENTRYPOINT  ["/usr/bin/mongod"]
CMD         ["--nounixsocket", "--cpu", "--master", "-v"]
