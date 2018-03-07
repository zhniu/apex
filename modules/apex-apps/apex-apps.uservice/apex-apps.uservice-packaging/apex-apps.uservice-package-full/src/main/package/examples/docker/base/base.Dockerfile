#
# Docker file to build an image that runs APEX on Java 8 in Ubuntu
#
# apex/base:0.6.0
FROM ubuntu:16.04
MAINTAINER John Keeney John.Keeney@ericsson.com

RUN apt-get update && \
	apt-get upgrade -y && \
	apt-get install -y software-properties-common && \
	add-apt-repository ppa:webupd8team/java -y && \
	apt-get update && \
	echo oracle-javax8-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections && \
	apt-get install -y oracle-java8-installer && \
	rm -rf /var/cache/oracle-jdk8-installer && \
	apt-get clean

RUN mkdir /packages
COPY apex-apps.uservice-packages-full-0.7.0.deb /packages
RUN dpkg -i packages/apex-apps.uservice-packages-full-0.7.0.deb  && \
	rm /packages/apex-apps.uservice-packages-full-0.7.0.deb

ENV PATH /opt/ericsson/apex/apex/bin:$PATH

RUN  apt-get clean

RUN chown -R apexuser:apexuser /home/apexuser/*
WORKDIR /home/apexuser
