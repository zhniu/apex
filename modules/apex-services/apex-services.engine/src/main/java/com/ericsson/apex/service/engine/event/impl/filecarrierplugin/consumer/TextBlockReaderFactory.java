/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.engine.event.impl.filecarrierplugin.consumer;

import java.io.InputStream;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.service.engine.event.ApexEventException;
import com.ericsson.apex.service.parameters.eventprotocol.EventProtocolParameters;
import com.ericsson.apex.service.parameters.eventprotocol.EventProtocolTextCharDelimitedParameters;
import com.ericsson.apex.service.parameters.eventprotocol.EventProtocolTextTokenDelimitedParameters;

/**
 * This factory creates text block readers for breaking character streams into blocks of text.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TextBlockReaderFactory {
    // The logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(TextBlockReaderFactory.class);

    /**
     * Get a text block reader for the given event protocol.
     *
     * @param inputStream the input stream that will be used for reading
     * @param eventProtocolParameters the parameters that have been specified for event protocols
     * @return the tagged reader
     * @throws ApexEventException On an unsupported event protocol
     */
    public TextBlockReader getTaggedReader(final InputStream inputStream, final EventProtocolParameters eventProtocolParameters) throws ApexEventException {
        // Check the type of event protocol we have
        if (eventProtocolParameters instanceof EventProtocolTextCharDelimitedParameters) {
            // We have character delimited textual input
            EventProtocolTextCharDelimitedParameters charDelimitedParameters = (EventProtocolTextCharDelimitedParameters) eventProtocolParameters;

            // Create the text block reader
            TextBlockReader characterDelimitedTextBlockReader = new CharacterDelimitedTextBlockReader(charDelimitedParameters);
            characterDelimitedTextBlockReader.init(inputStream);
            return characterDelimitedTextBlockReader;
        }
        else if (eventProtocolParameters instanceof EventProtocolTextTokenDelimitedParameters) {
            // We have token delimited textual input
            EventProtocolTextTokenDelimitedParameters tokenDelimitedParameters = (EventProtocolTextTokenDelimitedParameters) eventProtocolParameters;

            // Create the text block reader
            HeaderDelimitedTextBlockReader headerDelimitedTextBlockReader = new HeaderDelimitedTextBlockReader(tokenDelimitedParameters);
            headerDelimitedTextBlockReader.init(inputStream);
            return headerDelimitedTextBlockReader;
        }
        else {
            String errorMessage = "could not create text block reader for a textual event protocol, the required type " + eventProtocolParameters.getLabel()
                    + " is not supported";
            LOGGER.error(errorMessage);
            throw new ApexEventException(errorMessage);
        }
    }
}
