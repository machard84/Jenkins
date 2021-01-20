FROM            registry.chardma.org.uk:8443/centos:7
MAINTAINER      Mark Chard (machard.1984@gmail.com)
RUN             yum -y -q install wget
RUN             wget -P /etc/yum.repos.d/ https://nexus.chardma.org.uk/repository/configs/yum.repos.d/mariadb.repo
RUN             yum install -y -q mysql-community-server
COPY            conf.d/config.txt /etc/my.cnf.d/
COPY            conf.d/my.cnf /etc/my.cnf
RUN             chown mysql: /etc/my.cnf /etc/my.cnf.d/config.txt
RUN             mysqld --initialize --user=mysql --console
COPY            conf.d/config.txt /root/
EXPOSE          3306
ENTRYPOINT      ["/usr/sbin/mysqld"]
