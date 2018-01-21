#
# Docker file to build an image the MyFirstPolicyStdin2StdoutJsonEvent example 
#
# apex/myfirstpolicystdin2stdoutjsonevent:2

FROM apex/base
MAINTAINER John Keeney John.Keeney@ericsson.com

EXPOSE 12345
USER apexuser:apexuser
ENTRYPOINT ["apexEngine.sh"]
CMD ["-c", "examples/config/MyFirstPolicy/2/MyFirstPolicyConfigStdin2StdoutJsonEvent.json"]
