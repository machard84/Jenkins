FROM            registry.chardma.org.uk:8443/dag:latest
MAINTAINER      Mark Chard (machard.1984@gmail.com)
RUN             yum install -y -q epel-release wget shadow-utils
RUN             wget -P /etc/yum.repos.d/ https://nexus.chardma.org.uk/repository/configs/yum.repos.d/bro.repo
RUN             yum install -y -q bro
ENTRYPOINT      ["/opt/bro/bin/bro"]
CMD             ["-C", "-i", "eth0", "local" ]
