#
# Docker file to build an image the Stdin2StdoutJsonEventJavascript example 
#
# apex/stdin2stdoutjsoneventjavascript
FROM apex/base
MAINTAINER John Keeney John.Keeney@ericsson.com

EXPOSE 12345
USER apexuser:apexuser
ENTRYPOINT ["apexEngine.sh"]
CMD ["-c", "examples/config/SampleDomain/Stdin2StdoutJsonEventJavascript.json"]
