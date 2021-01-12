FROM            127.0.0.1:5000/centos:7
MAINTAINER      Mark Chard (machard.1984@gmail.com)
RUN             yum install -y -q shadow-utils which wget
RUN             wget -P /etc/yum.repos.d/ https://nexus.chardma.org.uk/repository/configs/yum.repos.d/ntop-ng.repo
RUN             wget -P /etc/yum.repos.d/ https://nexus.chardma.org.uk/repository/configs/yum.repos.d/epel.repo
RUN             yum install -y -q nprobe
COPY            conf.d/nprobe-none.conf /etc/nprobe/nprobe-none.conf
EXPOSE          2055
ENTRYPOINT      ["nprobe"]
CMD             ["-i", "none", "-n", "udp://0.0.0.0:2055", "--collector-port", "2055", "--zmq", "tcp://ntopng:5666", "-T", "@NTOPNG@", "--mysql='mysql:ntopng:nflow:ntopng:ntopng'", "--zmq-probe-mode"]
