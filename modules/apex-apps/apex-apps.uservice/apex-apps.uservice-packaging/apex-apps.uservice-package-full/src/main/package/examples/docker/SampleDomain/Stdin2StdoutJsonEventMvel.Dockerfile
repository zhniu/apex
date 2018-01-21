#
# Docker file to build an image the Stdin2StdoutJsonEventMVEL example 
#
# apex/stdin2stdoutjsoneventmvel
FROM apex/base
MAINTAINER John Keeney John.Keeney@ericsson.com

EXPOSE 12345
USER apexuser:apexuser
ENTRYPOINT ["apexEngine.sh"]
CMD ["-c", "examples/config/SampleDomain/Stdin2StdoutJsonEventMVEL.json"]
