#
# Docker file to build an image that runs APEX on Java 8 in Ubuntu
#
# apex/base:0.7.0
FROM ubuntu:16.04
MAINTAINER Liam Fallon liam.fallon@ericsson.com

RUN apt-get update && \
 	apt-get upgrade -y && \
	apt-get install -y software-properties-common && \
	add-apt-repository ppa:webupd8team/java -y && \
	apt-get update && \
	echo oracle-javax8-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections && \
	apt-get install -y oracle-java8-installer && \
	rm -rf /var/cache/oracle-jdk8-installer && \
        apt-get install -y curl && \
	apt-get clean

RUN mkdir /tmp/packages
COPY target/apex-domains-periodic-0.7.0.deb /tmp/packages

RUN dpkg -i /tmp/packages/apex-domains-periodic-0.7.0.deb  && \
	rm /tmp/packages/apex-domains-periodic-0.7.0.deb && \
	rmdir /tmp/packages

ENV PATH /opt/ericsson/apex/apex/bin:$PATH

RUN  apt-get clean

RUN chown -R apexuser:apexuser /home/apexuser
WORKDIR /home/apexuser

EXPOSE 12345
EXPOSE 23324
USER apexuser:apexuser

RUN mkdir /home/apexuser/etc
RUN mkdir /home/apexuser/outputLog

COPY target/classes/config/ApexConfigPeriodic.json /home/apexuser/etc
COPY target/PeriodicPolicyModel.json              /home/apexuser/etc

ENTRYPOINT ["apexApps.sh"]
CMD ["engine", "-c", "/home/apexuser/etc/ApexConfigPeriodic.json", "-m", "/home/apexuser/etc/PeriodicPolicyModel.json"]

