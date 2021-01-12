FROM            registry.chardma.org.uk:8443/centos:7
MAINTAINER      Mark Chard(machard.1984@gmail.com)
RUN             yum install -y -q community-mysql-devel gcc python3-devel python3 python3-pip
RUN             pip3 install --user Flask Flask-PyMongo
WORKDIR         /usr/local/app/Templates/
COPY            app/templates/ /usr/local/app/templates/
WORKDIR         /usr/local/app/
COPY            ./app/mysql-connector.py /usr/local/app/
ENTRYPOINT      ["/usr/bin/python3"]
CMD             ["mysql-connector.py"]
