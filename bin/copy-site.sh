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
## Script to copy generated documentation from a master clone to a gh-pages clone using rsync.
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

if [ ! -d ../github-pages ]
then
	echo
	echo 'Copy site only works on double Github directories.'
	echo "One for APEX, another one called 'github-pages' for gh-pages branch."
	exit
fi

if [ ! -d target/gh-pages ]
then
	echo
	echo 'Did not find "target/gh-pages".'
	echo "Please build the site first."
	exit
fi

## script name for output
MOD_SCRIPT_NAME=`basename $0`

##
## Help screen and exit condition (i.e. too few arguments)
##
Help()
{
	echo ""
	echo "$MOD_SCRIPT_NAME - copies generated site to github-pages"
	echo ""
	echo "       Usage:  $MOD_SCRIPT_NAME [options]"
	echo ""
	echo "       Options"
	echo "         -d          - dry run, no copy/delete performed"
	echo "         -x          - execute the copy"
	echo "         -h          - this help screen"
	echo ""
	echo ""
	exit 255;
}
if [ $# -eq 0 ]; then
	Help
fi

##
## check CLI one by one
##

##
## do dry run
##
if [ "$1" == "-d" ]; then
	echo "$MOD_SCRIPT_NAME: dry run"
	rsync -av --delete --dry-run target/gh-pages/site/ ../github-pages/site | grep delet
	exit 0
fi

##
## do copy, execute actions
##
if [ "$1" == "-x" ]; then
	echo "$MOD_SCRIPT_NAME: executing copy/delete"
	rsync -a --delete target/gh-pages/site/ ../github-pages/site
	exit 0
fi
