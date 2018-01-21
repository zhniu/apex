#!/usr/bin/env bash

##
## COPYRIGHT (C) Ericsson 2016-2018
## 
## The copyright to the computer program(s) herein is the property of
## Ericsson Inc. The programs may be used and/or copied only with written
## permission from Ericsson Inc. or in accordance with the terms and
## conditions stipulated in the agreement/contract under which the
## program(s) have been supplied.
##

##
## Script to run APEX Applications
## Call -h for help
## - requires BASH with associative arrays, bash of at least version 4
## - for BASH examples with arrays see for instance: http://www.artificialworlds.net/blog/2012/10/17/bash-associative-array-examples/
## - adding a new app means to add a command to APEX_APP_MAP and a description to APEX_APP_DESCR_MAP using same/unique key
##
## @package    com.ericsson.apex.apex
## @author     Sven van der Meer <sven.van.der.meer@ericsson.com>
## @copyright  Ericsson
## @license    proprietary
## @version    v0.7.0


##
## DO NOT CHANGE CODE BELOW, unless you know what you are doing
##


if [ -z $APEX_HOME ]
then
	APEX_HOME="/opt/ericsson/apex/apex"
fi

if [ ! -d $APEX_HOME ]
then
	echo
	echo 'Apex directory "'$APEX_HOME'" not set or not a directory'
	echo "Please set environment for 'APEX_HOME'"
	exit
fi


## script name for output
MOD_SCRIPT_NAME=`basename $0`

## config for CP apps
_config="-Dlogback.configurationFile=$APEX_HOME/etc/logback.xml -Dhazelcast.config=$APEX_HOME/etc/hazelcast.xml -Dhazelcast.mancenter.enabled=false"


## system to get CygWin paths
system=`uname -s | cut -c1-6`
cpsep=":"
if [ "$system" == "CYGWIN" ] ; then
	APEX_HOME=`cygpath -m ${APEX_HOME}`
	cpsep=";"
fi

## CP for CP apps
CLASSPATH="$APEX_HOME/etc${cpsep}$APEX_HOME/etc/hazelcast${cpsep}$APEX_HOME/etc/infinispan${cpsep}$APEX_HOME/lib/*"


## array of applications with name=command
declare -A APEX_APP_MAP
APEX_APP_MAP["engine"]="java -cp ${CLASSPATH} $_config com.ericsson.apex.service.engine.main.ApexMain"

## array of applications with name=description
declare -A APEX_APP_DESCR_MAP
APEX_APP_DESCR_MAP["engine"]="starts the APEX engine"


##
## Help screen and exit condition (i.e. too few arguments)
##
Help()
{
	echo ""
	echo "$MOD_SCRIPT_NAME - runs APEX applications"
	echo ""
	echo "       Usage:  $MOD_SCRIPT_NAME [options] | [<application> [<application options>]]"
	echo ""
	echo "       Options"
	echo "         -d <app>    - describes an application"
	echo "         -l          - lists all applications supported by this script"
	echo "         -h          - this help screen"
	echo ""
	echo ""
	exit 255;
}
if [ $# -eq 0 ]; then
	Help
fi


##
## read command line, cannot do as while here due to 2-view CLI
##
if [ "$1" == "-l" ]; then
	echo "$MOD_SCRIPT_NAME: supported applications:"
	echo " --> ${!APEX_APP_MAP[@]}"
	echo ""
	exit 0
fi
if [ "$1" == "-d" ]; then
	if [ -z "$2" ]; then
		echo "$MOD_SCRIPT_NAME: no application given to describe, supported applications:"
		echo " --> ${!APEX_APP_MAP[@]}"
		echo ""
		exit 0;
	else
		_cmd=${APEX_APP_DESCR_MAP[$2]}
		if [ -z "$_cmd" ]; then
			echo "$MOD_SCRIPT_NAME: unknown application '$2'"
			echo ""
			exit 0;
		fi
		echo "$MOD_SCRIPT_NAME: application '$2'"
		echo " --> $_cmd"
		echo ""
		exit 0;
	fi
fi
if [ "$1" == "-h" ]; then
	Help
	exit 0
fi


_app=$1
shift
_cmd=${APEX_APP_MAP[$_app]}
if [ -z "$_cmd" ]; then
	echo "$MOD_SCRIPT_NAME: application '$_app' not supported"
	exit 1
fi
_cmd="$_cmd $*"
## echo "$MOD_SCRIPT_NAME: running application '$_app' with command '$_cmd'"
exec $_cmd

