/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.apps.uservice.test.engine.runtime;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.service.ParameterService;
import com.ericsson.apex.model.utilities.TextFileUtils;
import com.ericsson.apex.service.engine.engdep.EngDepMessagingService;
import com.ericsson.apex.service.engine.event.ApexEvent;
import com.ericsson.apex.service.engine.event.impl.jsonprotocolplugin.Apex2JSONEventConverter;
import com.ericsson.apex.service.engine.main.ApexCommandLineArguments;
import com.ericsson.apex.service.engine.runtime.ApexEventListener;
import com.ericsson.apex.service.engine.runtime.EngineService;
import com.ericsson.apex.service.engine.runtime.impl.EngineServiceImpl;
import com.ericsson.apex.service.parameters.ApexParameterHandler;
import com.ericsson.apex.service.parameters.engineservice.EngineServiceParameters;

/**
 * The Class Engine is a feature test class that instantiates an Apex Engine service on a server with a specified Apex engine instance count. The Apex Engine
 * service is started and then the events in the event directory are listed. The user selects the event to send to the engine, the engine processes the event
 * and prompts the user for a further event. The user may then terminate the engine or send a further event.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public final class Engine {
    // The engine service that is started
    private EngineService engineService = null;

    // A directory containing events to be sent to the engine, the events should be in the appropriate format for the engine (XML)
    private File eventDir = null;

    // An event handler for reading and writing Apex events to and from JSON documents
    private Apex2JSONEventConverter xmlEventHandler = new Apex2JSONEventConverter();

    // The EngDep messaging service
    private EngDepMessagingService engDepMessagingService = null;

    // State flag for the Engine thread
    private boolean interrupted = false;

    /**
     * The main method kicks off an engine. It takes a server name, thread count, EngDep port and event directory as parameters
     *
     * @param args the arguments {@code server-name} {@code thread-count} {@code engdep-port} {@code event-dir}
     * @throws Exception on engine instantiation and running errors
     */
    public static void main(final String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("usage: Engine <engine-parameter-file> <event-dir>");
            return;
        }

        ApexCommandLineArguments arguments = new ApexCommandLineArguments();
        arguments.parse(args);
        arguments.validate();

        // Get the engine parameters
        new ApexParameterHandler().getParameters(arguments);

        // Kick off an engine
        new Engine(args[1]);
    }

    /**
     * Instantiates a new engine configured using the incoming parameters.
     *
     * @param eventDirName the directory in which XML files for events are stored
     * @throws Exception on engine instantiation and running errors
     */
    private Engine(final String eventDirName) throws Exception {
        System.out.println("engine starting . . .");

        eventDir = new File(eventDirName);
        if (eventDir == null || !eventDir.isDirectory() || !eventDir.canRead()) {
            System.err.println("specified event directory does not exist, is not a directory, or is not readable: " + eventDirName);
            return;
        }

        EngineServiceParameters engineServiceParameters = ParameterService.getParameters(EngineServiceParameters.class);

        try {
            // Create the engine service with the specified parameters
            engineService = EngineServiceImpl.create(engineServiceParameters);
            // Register a new instance of the inner class EngineApexLisener to process result events and start the engine
            engineService.registerActionListener(engineServiceParameters.getEngineKey().getID(), new EngineApexListener());

            // Start the EngDep administration messaging service
            engDepMessagingService = new EngDepMessagingService(engineService, engineServiceParameters.getDeploymentPort());
            engDepMessagingService.start();
        }
        catch (final Exception e) {
            System.err.println("engine start failed");
            e.printStackTrace();
            return;
        }

        System.out.println("engine server running (remember to update model before sending events)");

        // Loop until the user exits
        while (!interrupted) {
            try {
                // Ask the user to select an event
                final String eventXMLString = getEventXMLFromFile();
                System.out.println(eventXMLString);
                if (eventXMLString != null) {
                    // Send the event to the engine service
                    for (ApexEvent apexEvent: xmlEventHandler.toApexEvent(null, eventXMLString)) {
                        engineService.getEngineServiceEventInterface().sendEvent(apexEvent);
                    }
                }
            }
            catch (final Exception e) {
                System.err.println("engine stats server, exception on event sending");
                e.printStackTrace();
                return;
            }
        }

        // Stop EngDep messaging
        engDepMessagingService.stop();

        // The user has exited or the engine has been interrupted, stop it
        System.out.println("engine server shutting down");
    }

    /**
     * Instantiate an Apex event from an XML file. This method presents the user with a list of available events, the user selects one or selects exit. If the
     * user selects an event, that event XML file, the event is instantiated and returned, otherwise the method returns {@code null} causing the program to
     * exit.
     *
     * @return the event instantiated using the definition in the XML document
     */
    private String getEventXMLFromFile() {
        // Put all the XML files in the event directory in a hash map indexed by an integer
        final Map<Integer, File> eventXMLFileMap = new HashMap<Integer, File>();
        int eventNo = 0;
        for (final File xmlFile : eventDir.listFiles()) {
            if (!xmlFile.isFile() || !xmlFile.getName().endsWith(".xml")) {
                System.err.println("directory entry is not an xml file: " + eventDir.getAbsolutePath() + '/' + xmlFile.getName());
                continue;
            }

            eventXMLFileMap.put(eventNo++, xmlFile);
        }

        // Print the event list
        System.out.println("***Event List***");

        for (int e = 0; e < eventXMLFileMap.size(); e++) {
            final String eventLine = String.format("%3d: %s", e, eventXMLFileMap.get(e).getName());
            System.out.println(eventLine);
        }

        // Read the user's input
        System.out.print("select event to send ('x' to exit): ");
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            final String selectedEventString = reader.readLine();
            int selectedEvent = -1;

            // Check if we should exit
            if (selectedEventString.equalsIgnoreCase("x")) {
                interrupted = true;
                return null;
            }

            // Check that the input is a number
            selectedEvent = Integer.valueOf(selectedEventString);
            if (selectedEvent < 0 || selectedEvent >= eventXMLFileMap.size()) {
                throw new NumberFormatException("selected event out of range");
            }

            // Read the contents of the XML file and return the contents as a string
            final File selectedEventFile = eventXMLFileMap.get(selectedEvent);
            System.out.println("selected file: " + selectedEventFile.getAbsolutePath());
            return TextFileUtils.getTextFileAsString(selectedEventFile.getCanonicalPath());
        }
        catch (final NumberFormatException e) {
            System.out.println("invalid event entered: " + e.getMessage());
            return null;
        }
        catch (final Exception e) {
            System.out.println("error reading event file: " + e.getMessage());
            interrupted = true;
            return null;
        }
    }

    /**
     * The listener interface for receiving engineApex events. The class that is interested in processing a engineApex event implements this interface, and the
     * object created with that class is registered with a component using the component's {@code addEngineApexListener} method. When the engineApex event
     * occurs, that object's appropriate method is invoked.
     *
     * This class handles events returned by the Apex engine, simply printing the resulting event.
     *
     * @see EngineApexEvent
     */
    private final class EngineApexListener implements ApexEventListener {
        /*
         * (non-Javadoc)
         *
         * @see com.ericsson.apex.service.engine.runtime.ApexEventListener#onApexEvent(com.ericsson.apex.service.engine.event.ApexEvent)
         */
        @Override
        public synchronized void onApexEvent(final ApexEvent apexEvent) {
            System.out.println("result is:" + apexEvent);
            try {
                System.out.println(xmlEventHandler.fromApexEvent(apexEvent));
            }
            catch (final ApexException e) {
                System.err.println("could not marshal event : " + apexEvent);
                e.printStackTrace();
            }
        }
    }
}
