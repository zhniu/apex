::
:: (C) Copyright LM Ericsson System Expertise AT/LMI, 2017
:: 
:: The copyright to the computer program(s) herein is the property of Ericsson System Expertise EEI, Sweden. The
:: program(s) may be used and/or copied only with the written permission from Ericsson System Expertise AT/LMI or in
:: accordance with the terms and conditions stipulated in the agreement/contract under which the program(s) have been
:: supplied.
::

::
:: Script to run the APEX
::
:: @package    com.ericsson.apex.apex
:: @author     Sven van der Meer <sven.van.der.meer@ericsson.com>
:: @copyright  LM Ericsson
:: @license    proprietary
:: @version    v0.5.6


::
:: DO NOT CHANGE CODE BELOW, unless you know what you are doing
::

@echo off
setlocal enableDelayedExpansion


if defined APEX_HOME (
	if exist "%APEX_HOME%\" (
		set _dummy=dir
	) else (
		echo[
		echo Apex directory 'APEX_HOME' not a directory
		echo Please set environment for 'APEX_HOME'
		echo[
		exit /b
	)
) else (
	echo[
	echo Apex directory 'APEX_HOME' not set
	echo Please set environment for 'APEX_HOME'
	echo[
	exit /b
)

%APEX_HOME%\bin\apexApps.bat engine %*
