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

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.service.parameters.eventprotocol.EventProtocolTextCharDelimitedParameters;

/**
 * The class CharacterDelimitedTextBlockReader reads the next block of text between two character tags from an input stream.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class CharacterDelimitedTextBlockReader implements TextBlockReader {
    // The logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(CharacterDelimitedTextBlockReader.class);

    // The character tags
    private final char startTagChar;
    private final char endTagChar;

    // The input stream for text
    private InputStream inputStream;

    // Flag indicating we have seen EOF on the stream
    private boolean eofOnInputStream = false;

    /**
     * Constructor, set the delimiters.
     *
     * @param startTagChar The start tag for text blocks
     * @param endTagChar The end tag for text blocks
     */
    public CharacterDelimitedTextBlockReader(final char startTagChar, final char endTagChar) {
        this.startTagChar = startTagChar;
        this.endTagChar = endTagChar;
    }

    /**
     * Constructor, set the delimiters from a character delimited event protocol parameter class.
     *
     * @param charDelimitedParameters the character delimited event protocol parameter class
     */
    public CharacterDelimitedTextBlockReader(final EventProtocolTextCharDelimitedParameters charDelimitedParameters) {
        this.startTagChar = charDelimitedParameters.getStartChar();
        this.endTagChar = charDelimitedParameters.getEndChar();
    }

    /* (non-Javadoc)
     * @see com.ericsson.apex.service.engine.event.impl.filecarrierplugin.consumer.TextBlockReader#init(java.io.InputStream)
     */
    @Override
    public void init(final InputStream incomingInputStream) {
        this.inputStream = incomingInputStream;
    }

    /* (non-Javadoc)
     * @see com.ericsson.apex.service.engine.event.impl.filecarrierplugin.consumer.TextBlockReader#readTextBlock()
     */
    @Override
    public TextBlock readTextBlock() throws IOException {
        // Check if there was a previous end of a text block with a non-empty text block returned
        if (eofOnInputStream) {
            return new TextBlock(eofOnInputStream, null);
        }

        // The initial nesting level of incoming text blocks is always zero
        int nestingLevel = 0;

        // Holder for the text block
        final StringBuilder textBlockBuilder = new StringBuilder();

        // Read the next text block
        while (true) {
            final char nextChar = (char) inputStream.read();

            // Check for EOF
            if (nextChar == (char) -1) {
                eofOnInputStream = true;
                break;
            }

            if (nextChar == startTagChar) {
                nestingLevel++;
            }
            else if (nestingLevel == 0 && !Character.isWhitespace(nextChar)) {
                LOGGER.warn("invalid input on consumer: " + nextChar);
                continue;
            }

            textBlockBuilder.append(nextChar);

            // Check for end of the text block, we have come back to level 0
            if (nextChar == endTagChar) {
                if (nestingLevel > 0) {
                    nestingLevel--;
                }

                if (nestingLevel == 0) {
                    break;
                }
            }
        }

        // Condition the text block and return it
        final String textBlock = textBlockBuilder.toString().trim();
        if (textBlock.length() > 0) {
            return new TextBlock(eofOnInputStream, textBlock);
        }
        else {
            return new TextBlock(eofOnInputStream, null);
        }
    }
}
