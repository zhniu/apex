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

/**
 * Implementers of the interface TextBlockReader read the next block of text from an input stream.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public interface TextBlockReader {
    /**
     * Initialize the text block reader reader.
     *
     * @param inputStream The stream to read from
     */
    void init(InputStream inputStream);

    /**
     * Read a block of text between two delimiters.
     *
     * @return The text block
     * @throws IOException On reading errors
     */
    TextBlock readTextBlock() throws IOException;
}
