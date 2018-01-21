/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.apps.utilities.docs;

////
//// NOTE: This file contains tags for ASCIIDOC
//// DO NOT REMOVE any of those tag lines, e.g.
//// //tag::**
//// //end::**
////

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.junit.Test;

//tag::import[]
import com.ericsson.apex.apps.utilities.CliOptions;
import com.ericsson.apex.apps.utilities.CliParser;
//end::import[]

/**
 * Examples for documentation using {@link CliParser}.
 *
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
public class ExampleCliParser {

    /**
     * Test example parser.
     */
    @Test
    public void testExampleParser() {
        final String[] args = new String[] {"-h"};

        // tag::setApp[]
        final String appName = "test-app";
        final String appDescription = "a test app for documenting how to use the CLI utilities";
        // end::setApp[]

        // tag::setCli[]
        final CliParser cli = new CliParser();
        cli.addOption(CliOptions.HELP);
        cli.addOption(CliOptions.VERSION);
        cli.addOption(CliOptions.MODELFILE);
        // end::setCli[]

        // tag::parseCli[]
        final CommandLine cmd = cli.parseCli(args);
        // end::parseCli[]

        // tag::processCliHelp[]
        // help is an exit option, print usage and exit
        if (cmd.hasOption('h') || cmd.hasOption("help")) {
            final HelpFormatter formatter = new HelpFormatter();
            System.out.println(appName + " v" + cli.getAppVersion() + " - " + appDescription);
            formatter.printHelp(appName, cli.getOptions());
            System.out.println();
            return;
        }
        // end::processCliHelp[]

        // tag::processCliVersion[]
        // version is an exit option, print version and exit
        if (cmd.hasOption('v') || cmd.hasOption("version")) {
            System.out.println(appName + " " + cli.getAppVersion());
            System.out.println();
            return;
        }
        // end::processCliVersion[]

        // tag::processCliModel[]
        String modelFile = cmd.getOptionValue('m');
        if (modelFile == null) {
            modelFile = cmd.getOptionValue("model");
        }
        if (modelFile == null) {
            System.err.println(appName + ": no model file given, cannot proceed (try -h for help)");
            return;
        }
        // end::processCliModel[]

        // tag::someStartPrint[]
        System.out.println();
        System.out.println(appName + ": starting");
        System.out.println(" --> model file: " + modelFile);
        System.out.println();
        System.out.println();
        // end::someStartPrint[]

        // tag::yourApp[]
        // your code for the application here
        // e.g.
        // try {
        // Model2Cli app = new Model2Cli(modelFile, !cmd.hasOption("sv"), appName);
        // app.runApp();
        // }
        // catch(ApexException aex) {
        // System.err.println(appName + ": caught APEX exception with message: " + aex.getMessage());
        // }
        // end::yourApp[]
    }
}
