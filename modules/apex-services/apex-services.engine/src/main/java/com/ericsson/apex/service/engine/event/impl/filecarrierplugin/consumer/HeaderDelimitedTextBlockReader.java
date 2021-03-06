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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.core.infrastructure.threading.ApplicationThreadFactory;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;
import com.ericsson.apex.service.parameters.eventprotocol.EventProtocolTextTokenDelimitedParameters;

/**
 * The Class TextBlockReader reads the next block of text from an input stream.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class HeaderDelimitedTextBlockReader implements TextBlockReader, Runnable {
    // The logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(HeaderDelimitedTextBlockReader.class);

    // The amount of time to wait for input on the text block reader
    private static final long TEXT_BLOCK_DELAY = 250;

    // Tag for the start of a text block
    private final String blockStartToken;

    // The input stream for text
    private InputStream inputStream;

    // The lines of input read from the input stream
    private final Queue<String> textLineQueue = new LinkedBlockingQueue<>();

    // The thread used to read text from the input stream
    private Thread textConsumputionThread;

    // True while EOF has not been seen on input
    private boolean eofOnInputStream = false;

    /**
     * Constructor, initialize the text block reader.
     *
     * @param blockStartToken the block start token for the start of a text block
     */
    public HeaderDelimitedTextBlockReader(final String blockStartToken) {
        this.blockStartToken = blockStartToken;
    }

    /**
     * Constructor, initialize the text block reader using token delimited event protocol parameters.
     *
     * @param tokenDelimitedParameters the token delimited event protocol parameters
     */
    public HeaderDelimitedTextBlockReader(final EventProtocolTextTokenDelimitedParameters tokenDelimitedParameters) {
        this.blockStartToken = tokenDelimitedParameters.getDelimiterToken();
    }

    /* (non-Javadoc)
     * @see com.ericsson.apex.service.engine.event.impl.filecarrierplugin.consumer.TextBlockReader#init(java.io.InputStream)
     */
    @Override
    public void init(final InputStream incomingInputStream) {
        this.inputStream = incomingInputStream;

        // Configure and start the text reading thread
        textConsumputionThread = new ApplicationThreadFactory(this.getClass().getName()).newThread(this);
        textConsumputionThread.setDaemon(true);
        textConsumputionThread.start();
    }

    /* (non-Javadoc)
     * @see com.ericsson.apex.service.engine.event.impl.filecarrierplugin.consumer.TextBlockReader#readTextBlock()
     */
    @Override
    public TextBlock readTextBlock() throws IOException {
        // Holder for the current text block
        final StringBuilder textBlockBuilder = new StringBuilder();

        // Wait for the timeout period if there is no input
        if (!eofOnInputStream && textLineQueue.size() == 0) {
            ThreadUtilities.sleep(TEXT_BLOCK_DELAY);
        }

        // Scan the lines in the queue
        while (textLineQueue.size() > 0) {
            // Scroll down in the available lines looking for the start of the text block
            if (textLineQueue.peek().startsWith(blockStartToken)) {
                // Process the input line header
                textBlockBuilder.append(textLineQueue.remove());
                textBlockBuilder.append('\n');
                break;
            }
            else {
                LOGGER.warn("invalid input on consumer: " + textLineQueue.remove());
            }
        }

        // Get the rest of the text document
        while (textLineQueue.size() > 0 && !textLineQueue.peek().startsWith(blockStartToken)) {
            textBlockBuilder.append(textLineQueue.remove());
            textBlockBuilder.append('\n');
        }

        // Condition the text block and return it
        final String textBlock = textBlockBuilder.toString().trim();
        final boolean endOfText = (eofOnInputStream && textLineQueue.size() == 0 ? true : false);

        if (textBlock.length() > 0) {
            return new TextBlock(endOfText, textBlock);
        }
        else {
            return new TextBlock(endOfText, null);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        final BufferedReader textReader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            // Read the input line by line until we see end of file on the stream
            String line;
            while ((line = textReader.readLine()) != null) {
                textLineQueue.add(line);
            }
        }
        catch (final IOException e) {
            LOGGER.warn("I/O exception on text input on consumer: ", e);
        }
        finally {
            eofOnInputStream = true;
        }
    }
}
