FROM            127.0.0.1:5000/centos:7
MAINTAINER      Mark Chard (machard.1984@gmail.com)
RUN             yum install -y -q epel-release which
RUN             yum install -y -q redis hiredis-devel
COPY            conf.d/redis.conf /etc/redis.conf
EXPOSE          6379
ENTRYPOINT      ["/usr/bin/redis-server"]
CMD             ["/etc/redis.conf", "--loglevel", "verbose"]