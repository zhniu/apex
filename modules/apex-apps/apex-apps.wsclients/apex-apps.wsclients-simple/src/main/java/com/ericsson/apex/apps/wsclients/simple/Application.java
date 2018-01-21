/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.apps.wsclients.simple;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.channels.NotYetConnectedException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.lang3.Validate;

import com.ericsson.apex.apps.utilities.CliOptions;
import com.ericsson.apex.apps.utilities.CliParser;

/**
 * Simple console application with main method.
 * 
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
public final class Application {

    /**
     * Private constructor prevents subclassing.
     */
    private Application() {
    }

    /**
     * The main method for the WS applications.
     * @param args command line argument s
     */
    public static void main(final String[] args) {
        String appName = "ws-simple-echo";
        String appDescr = "receives events from APEX via WS and prints them to standard out";
        boolean console = false;

        CliParser cli  = new CliParser();
        cli.addOption(CliOptions.HELP);
        cli.addOption(CliOptions.VERSION);
        cli.addOption(CliOptions.CONSOLE);
        cli.addOption(CliOptions.SERVER);
        cli.addOption(CliOptions.PORT);

        CommandLine cmd = cli.parseCli(args);

        if (cmd.hasOption('c') || cmd.hasOption("console")) {
            appName = "ws-simple-console";
            appDescr = "takes events from stdin and sends via WS to APEX";
            console = true;
        }

        // help is an exit option, print usage and exit
        if (cmd.hasOption('h') || cmd.hasOption("help")) {
            HelpFormatter formatter = new HelpFormatter();
            System.out.println(appName + " v" + cli.getAppVersion() + " - " + appDescr);
            formatter.printHelp(appName, cli.getOptions());
            System.out.println();
            return;
        }

        // version is an exit option, print version and exit
        if (cmd.hasOption('v') || cmd.hasOption("version")) {
            System.out.println(appName + " " + cli.getAppVersion());
            System.out.println();
            return;
        }

        String server = cmd.getOptionValue('s');
        if (server == null) {
            server = cmd.getOptionValue("server");
        }
        if (server == null) {
            server = "localhost";
        }

        String port = cmd.getOptionValue('p');
        if (port == null) {
            port = cmd.getOptionValue("port");
        }
        if (port == null) {
            port = "8887";
        }

        if (console) {
            runConsole(server, port, appName);
        }
        else {
            runEcho(server, port, appName);
        }

    }

    /**
     * Runs the simple echo client.
     * @param server the server, must not be blank
     * @param port the port, must not be blank
     * @param appName the application name, must not be blank
     */
    public static void runEcho(final String server, final String port, final String appName) {
        Validate.notBlank(server);
        Validate.notBlank(port);
        Validate.notBlank(appName);

        System.out.println();
        System.out.println(appName + ": starting simple event echo");
        System.out.println(" --> server: " + server);
        System.out.println(" --> port: " + port);
        System.out.println();
        System.out.println("Once started, the application will simply print out all received events to standard out.");
        System.out.println("Each received event will be prefixed by '---' and suffixed by '===='");
        System.out.println();
        System.out.println();

        try {
            SimpleEcho simpleEcho = new SimpleEcho(server, port, appName);
            simpleEcho.connect();
        }
        catch (URISyntaxException uex) {
            System.err.println(appName + ": URI exception, could not create URI from server and port settings");
        }
        catch (NullPointerException nex) {
            System.err.println(appName + ": null pointer, server or port were null");
        }
        catch (IllegalArgumentException iex) {
            System.err.println(appName + ": illegal argument, server or port were blank");
        }
    }

    /**
     * Runs the simple console.
     * @param server the server, must not be blank
     * @param port the port, must not be blank
     * @param appName the application name, must not be blank
     */
    public static void runConsole(final String server, final String port, final String appName) {
        Validate.notBlank(server);
        Validate.notBlank(port);
        Validate.notBlank(appName);

        System.out.println();
        System.out.println(appName + ": starting simple event console");
        System.out.println(" --> server: " + server);
        System.out.println(" --> port: " + port);
        System.out.println();
        System.out.println(" - terminate the application typing 'exit<enter>' or using 'CTRL+C'");
        System.out.println(" - events are created by a non-blank starting line and terminated by a blank line");
        System.out.println();
        System.out.println();

        try {
            SimpleConsole simpleConsole = new SimpleConsole(server, port, appName);
            simpleConsole.runClient();
        }
        catch (URISyntaxException uex) {
            System.err.println(appName + ": URI exception, could not create URI from server and port settings");
        }
        catch (NullPointerException nex) {
            System.err.println(appName + ": null pointer, server or port were null");
        }
        catch (IllegalArgumentException iex) {
            System.err.println(appName + ": illegal argument, server or port were blank");
        }
        catch (NotYetConnectedException nex) {
            System.err.println(appName + ": not yet connected, connection to server took too long");
        }
        catch (IOException ioe) {
            System.err.println(appName + ": IO exception, something went wrong on the standard input");
        }
    }
}
