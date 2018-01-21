/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.services.client.monitoring.rest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * This class reads and handles command line parameters to the Apex RESTful services
 * 
 * User: ewatkmi Date: 31 Jul 2017
 */
public class ApexMonitoringRestParameterParser {
	// Apache Commons CLI options
	Options options;

	/**
	 * Construct the options for the CLI RESTful services
	 */
	public ApexMonitoringRestParameterParser() {
		options = new Options();
		options.addOption("h", "help", false, "outputs the usage of this command");
		options.addOption(
				Option.builder("p")
				.longOpt("port")
				.desc("port to use for the Apex Services REST calls")
				.hasArg()
				.argName("PORT")
				.required(false)
				.type(Number.class)
				.build());
		options.addOption(
				Option.builder("t")
				.longOpt("time-to-live")
				.desc("the amount of time in seconds that the server will run for before terminating")
				.hasArg()
				.argName("TIME_TO_LIVE")
				.required(false)
				.type(Number.class)
				.build());
	}

	/**
	 * Parse the command line options.
	 * @param args the arguments
	 * @return parsed parameters
	 */
	public ApexMonitoringRestParameters parse(final String[] args) {
		CommandLine commandLine = null;
		try {
			commandLine = new DefaultParser().parse(options, args);
		}
		catch (ParseException e) {
			throw new ApexMonitoringRestParameterException("invalid command line arguments specified : " + e.getMessage());
		}

		ApexMonitoringRestParameters parameters = new ApexMonitoringRestParameters();
		String[] remainingArgs = commandLine.getArgs();

		if (commandLine.getArgs().length > 0) {
			throw new ApexMonitoringRestParameterException("too many command line arguments specified : " + Arrays.toString(remainingArgs));
		}

		if (commandLine.hasOption('h')) {
			parameters.setHelp(true);
		}
		try {
			if (commandLine.hasOption('p')) {
				parameters.setRESTPort(((Number)commandLine.getParsedOptionValue("port")).intValue());
			}
		}
		catch (ParseException e) {
			throw new ApexMonitoringRestParameterException("error parsing argument \"port\" :" + e.getMessage(), e);
		}
		try {
			if (commandLine.hasOption('t')) {
				parameters.setTimeToLive(((Number)commandLine.getParsedOptionValue("time-to-live")).longValue());
			}
		}
		catch (ParseException e) {
			throw new ApexMonitoringRestParameterException("error parsing argument \"time-to-live\" :" + e.getMessage(), e);
		}

		return parameters;
	}

	/**
	 * Get help information.
	 * 
	 * @param mainClassName the main class name for the help output
	 * @return help string
	 */
	public String getHelp(final String mainClassName) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter stringPrintWriter = new PrintWriter(stringWriter);
		
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp(
				stringPrintWriter,
				120,
				mainClassName + " [options...] ",
				"",
				options,
				0,
				0,
				"");
		
		return stringWriter.toString();
	}
}
