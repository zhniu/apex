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
## Script to clear all created site artifacts, so all target/site for all modules plus target/gh-pages on parrent.
## Call -h for help
##
## @package    com.ericsson.apex.apex
## @author     Sven van der Meer <sven.van.der.meer@ericsson.com>
## @copyright  Ericsson
## @license    proprietary
## @version    v0.7.0


##
## DO NOT CHANGE CODE BELOW, unless you know what you are doing
##

## script name for output
MOD_SCRIPT_NAME=`basename $0`


##
## Help screen and exit condition (i.e. too few arguments)
##
Help()
{
	echo ""
	echo "$MOD_SCRIPT_NAME - remove all generated site artifacts."
	echo ""
	echo "       Usage:  $MOD_SCRIPT_NAME [options]"
	echo ""
	echo "       Options"
	echo "         -x          - execute the delete actions"
	echo "         -h          - this help screen"
	echo ""
	echo ""
	exit 255;
}
if [ $# -eq 0 ]; then
	Help
fi

while [ $# -gt 0 ]
do
	case $1 in
		# -x do clear
		-x)
			echo 
			echo "$MOD_SCRIPT_NAME: removing generated sites in all modules"
			for dir in `find -type d -name "site"|grep "/target/"`
			do
				echo "--> removing $dir"
				rm -fr $dir
			done
			echo "--> removing target/gh-pages"
			rm -fr target/gh-pages
			exit
		;;

		#-h prints help and exists
		-h)	Help;exit 0;;

		*)	echo "$MOD_SCRIPT_NAME: undefined CLI option - $1"; exit 255;;
	esac
done

