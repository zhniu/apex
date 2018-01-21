#
# Docker file to build an image the File2StdoutJsonEventJavascript example 
#
# apex/file2stdoutjsoneventjavascript
FROM apex/base
MAINTAINER John Keeney John.Keeney@ericsson.com

EXPOSE 12345
USER apexuser:apexuser
ENTRYPOINT ["apexEngine.sh"]
CMD ["-c", "examples/config/SampleDomain/File2StdoutJsonEventJavascript.json"]
