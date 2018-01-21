/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.apps.generators.model.model2event;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;

import com.ericsson.apex.apps.utilities.CliOptions;
import com.ericsson.apex.apps.utilities.CliParser;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;

/**
 * Model 2 event generator with main method.
 *
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
public final class Application {

    /** The name of the application. */
    public static final String APP_NAME = "gen-model2event";

    /** The description 1-liner of the application. */
    public static final String APP_DESCRIPTION = "generates JSON templates for events generated from a policy model";

    /** Private constructor to prevent instantiation. */
    private Application() { }

    /**
     * Main method to start the application.
     * 
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        final CliParser cli = new CliParser();
        cli.addOption(CliOptions.HELP);
        cli.addOption(CliOptions.VERSION);
        cli.addOption(CliOptions.MODELFILE);
        cli.addOption(CliOptions.TYPE);

        final CommandLine cmd = cli.parseCli(args);

        // help is an exit option, print usage and exit
        if (cmd.hasOption('h') || cmd.hasOption("help")) {
            final HelpFormatter formatter = new HelpFormatter();
            System.out.println(APP_NAME + " v" + cli.getAppVersion() + " - " + APP_DESCRIPTION);
            formatter.printHelp(APP_NAME, cli.getOptions());
            System.out.println();
            return;
        }

        // version is an exit option, print version and exit
        if (cmd.hasOption('v') || cmd.hasOption("version")) {
            System.out.println(APP_NAME + " " + cli.getAppVersion());
            System.out.println();
            return;
        }

        String modelFile = cmd.getOptionValue('m');
        if (modelFile == null) {
            modelFile = cmd.getOptionValue("model");
        }
        if (modelFile == null) {
            System.err.println(APP_NAME + ": no model file given, cannot proceed (try -h for help)");
            return;
        }

        String type = cmd.getOptionValue('t');
        if (type == null) {
            type = cmd.getOptionValue("type");
        }
        if (type == null) {
            System.err.println(APP_NAME + ": no event type given, cannot proceed (try -h for help)");
            return;
        }
        if (!type.equals("stimuli") && !type.equals("response") && !type.equals("internal")) {
            System.err.println(APP_NAME + ": unknown type <" + type + ">, cannot proceed (try -h for help)");
            return;
        }

        System.out.println();
        System.out.println(APP_NAME + ": starting Event generator");
        System.out.println(" --> model file: " + modelFile);
        System.out.println(" --> type: " + type);
        System.out.println();
        System.out.println();

        try {
            final Model2JsonEventSchema app = new Model2JsonEventSchema(modelFile, type, APP_NAME);
            app.runApp();
        }
        catch (final ApexException aex) {
            System.err.println(APP_NAME + ": caught APEX exception with message: " + aex.getMessage());
        }
    }
}
