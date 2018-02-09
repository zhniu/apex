/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.apps.utilities;

////
////NOTE: This file contains tags for ASCIIDOC
////DO NOT REMOVE any of those tag lines, e.g.
//////tag::**
//////end::**
////

import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Application CLI parser.
 *
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
public class CliParser {

    /** The local set of CLI options. */
    private final Options options;

    /** The command line, null on start, not null after successful parse. */
    private CommandLine cmd;

    /**
     * Creates a new CLI parser.
     */
    public CliParser() {
        options = new Options();
    }

    /**
     * Adds an option to the parser.
     * 
     * @param option the new option, must not be null
     * @return self to allow chaining
     */
    public CliParser addOption(final Option option) {
        if (option == null) {
            throw new IllegalStateException("CLI parser: given option was null");
        }
        options.addOption(option);
        return this;
    }

    /**
     * Parses the arguments with the set options.
     * 
     * @param args the arguments to parse
     * @return a command line with parsed arguments, null on parse errors.
     */
    public CommandLine parseCli(final String[] args) {
        final CommandLineParser parser = new DefaultParser();
        try {
            cmd = parser.parse(options, args);
        }
        catch (final ParseException ex) {
            System.err.println("Parsing failed.  Reason: " + ex.getMessage());
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
        return cmd;
    }

    /**
     * Returns the parsed command line.
     * 
     * @return the parsed command line, null if nothing parsed
     */
    public CommandLine getCommandLine() {
        return cmd;
    }

    /**
     * Returns the CLI options.
     * 
     * @return CLI options
     */
    public Options getOptions() {
        return options;
    }

    /**
     * Returns the version for an application as set by Maven.
     * 
     * @return version, null if version file <code>/app-version.txt</code> was not found
     */
    @SuppressWarnings("resource")
    // tag::cliParserVersion[]
    public String getAppVersion() {
        return new Scanner(CliParser.class.getResourceAsStream("/app-version.txt"), "UTF-8").useDelimiter("\\A").next();
    }
    // end::cliParserVersion[]
}
