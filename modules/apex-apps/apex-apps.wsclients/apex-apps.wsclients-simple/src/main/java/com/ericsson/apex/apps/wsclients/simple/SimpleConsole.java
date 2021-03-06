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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.NotYetConnectedException;

import org.apache.commons.lang3.Validate;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ServerHandshake;

/**
 * Simple WS client with a console for events.
 * 
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
public class SimpleConsole extends WebSocketClient {

    /** Application name, used as prompt. */
    private final String appName;

    /**
     * Creates a new simple echo object.
     * @param server the name of the server as either IP address or fully qualified host name, must not be blank
     * @param port the port to be used, must not be blank
     * @param appName the application name, used as prompt, must not be blank
     * @throws URISyntaxException is URI could not be created from server/port settings
     * @throws RuntimeException if server or port where blank
     */
    public SimpleConsole(final String server, final String port, final String appName) throws URISyntaxException {
        super(new URI("ws://" + server + ":" + port));
        Validate.notBlank(appName, "SimpleConsole: given application name was blank");
        this.appName = appName;
    }

    @Override
    public void onClose(final int code, final String reason, final boolean remote) {
        System.out.println(this.appName + ": Connection closed by " + (remote ? "APEX" : "me"));
        System.out.print(" ==-->> ");
        switch (code) {
        case CloseFrame.NORMAL:
            System.out.println("normal");
            break;
        case CloseFrame.GOING_AWAY:
            System.out.println("APEX going away");
            break;
        case CloseFrame.PROTOCOL_ERROR:
            System.out.println("some protocol error");
            break;
        case CloseFrame.REFUSE:
            System.out.println("received unacceptable type of data");
            break;
        case CloseFrame.NO_UTF8:
            System.out.println("expected UTF-8, found something else");
            break;
        case CloseFrame.TOOBIG:
            System.out.println("message too big");
            break;
        case CloseFrame.UNEXPECTED_CONDITION:
            System.out.println("unexpected server condition");
            break;
        default:
            System.out.println("unkown close frame code");
            break;
        }
        System.out.print(" ==-->> " + reason);
    }

    @Override
    public void onError(final Exception ex) {
        System.err.println(this.appName + ": " + ex.getMessage());
    }

    @Override
    public void onMessage(final String message) {
        //this client does not expect messages
    }

    @Override
    public void onOpen(final ServerHandshake handshakedata) {
        System.out.println(this.appName + ": opened connection to APEX (" + handshakedata.getHttpStatusMessage() + ")");
    }

    /**
     * Runs the console client.
     * In particular, it starts a new thread for the Websocket connection and then reads from standard input.
     * @throws NotYetConnectedException if not connected to server when sending events
     * @throws IOException on an IO problem on standard in
     */
    public void runClient() throws NotYetConnectedException, IOException {
        Thread thread = new Thread() {
            @Override
            public void run() {
                connect();
            }
        };
        thread.start();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String event = "";
        String line;
        while ((line = in.readLine()) != null) {
            if (line.equals("exit")) {
                break;
            }

            String current = line.trim();
            if ("".equals(current)) {
                this.send(event);
                event = "";
            }
            else {
                event += current;
            }
        }

        thread.interrupt();
        this.close(CloseFrame.NORMAL);
    }

}
