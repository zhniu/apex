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
////NOTE: This file contains tags for ASCIIDOC
////DO NOT REMOVE any of those tag lines, e.g.
//////tag::**
//////end::**
////

import org.apache.commons.cli.CommandLine;
import org.junit.Test;

import com.ericsson.apex.apps.utilities.CliOptions;
import com.ericsson.apex.apps.utilities.CliParser;

/**
 * Examples for documentation using {@link CliParser#getAppVersion()}.
 *
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
public class ExampleAppVersion {

    /** Test example app version. */
    @Test
    public void testExampleAppVersion() {
        final String[] args = new String[] {"-v"};

        // tag::setupParser[]
        final CliParser cli = new CliParser();
        cli.addOption(CliOptions.VERSION);
        final CommandLine cmd = cli.parseCli(args);
        // end::setupParser[]

        // tag::processCliVersion[]
        // version is an exit option, print version and exit
        if (cmd.hasOption('v') || cmd.hasOption("version")) {
            System.out.println("myApp" + " " + cli.getAppVersion());
            System.out.println();
            return;
        }
        // end::processCliVersion[]
    }
}
