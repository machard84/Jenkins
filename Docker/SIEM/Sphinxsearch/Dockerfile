FROM            registry.chardma.org.uk:8443/centos:7
MAINTAINER      Mark Chard (machard.1984@gmail.com)
RUN             yum -y -q install http://sphinxsearch.com/files/sphinx-2.3.2-1.rhel7.x86_64.rpm shadow-utils
COPY            conf.d/sphinxsearch.conf /etc/sphinx/sphinx.conf
EXPOSE          9306 9312
