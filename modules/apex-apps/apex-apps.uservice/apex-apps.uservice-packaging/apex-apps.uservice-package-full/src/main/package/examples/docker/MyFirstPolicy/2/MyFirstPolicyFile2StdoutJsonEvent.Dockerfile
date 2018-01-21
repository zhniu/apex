#
# Docker file to build an image the MyFirstPolicyFile2StdoutJsonEvent example 
#
# apex/myfirstpolicyfile2stdoutjsonevent:2

FROM apex/base
MAINTAINER John Keeney John.Keeney@ericsson.com

EXPOSE 12345
USER apexuser:apexuser
ENTRYPOINT ["apexEngine.sh"]
CMD ["-c", "examples/config/MyFirstPolicy/2/MyFirstPolicyConfigFile2StdoutJsonEvent.json"]
