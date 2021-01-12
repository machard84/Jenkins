FROM            registry.chardma.org.uk:8443/centos:7
MAINTAINER      Mark Chard (machard.1984@gmail.com)
RUN             yum -y -q install wget
RUN             wget -P /etc/yum.repos.d/ https://nexus.chardma.org.uk/repository/configs/yum.repos.d/epel.repo
RUN             yum install -y -q suricata PyYAML
RUN             suricata-update
ENTRYPOINT      ["suricata"]
CMD             ["-c", "/etc/suricata/suricata.yaml", "-i", "eth0", "-l", "/var/log/suricata"]
