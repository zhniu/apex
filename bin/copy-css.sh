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
## Script to copy css artifacts from parent to all child modules, recursively.
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

echo 
echo "$MOD_SCRIPT_NAME: copying standard css and images to modules"
for dir in `find -type d -name "site"|grep "/src"|grep "./modules/"`
do
	echo "--> copying to $dir"
	cp -dfrp src/site/css $dir
	cp -dfrp src/site/images $dir
done

echo "-> done"
echo
