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

/**
 * This class is a bean that holds a block of text read from an incoming text file.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TextBlock {
    private boolean endOfText = false;
    private String text;

    /**
     * Constructor to initiate the text block.
     *
     * @param endOfText the end of text
     * @param text the text
     */
    public TextBlock(final boolean endOfText, final String text) {
        this.endOfText = endOfText;
        this.text = text;
    }

    /**
     * Checks if is end of text.
     *
     * @return true, if checks if is end of text
     */
    public boolean isEndOfText() {
        return endOfText;
    }

    /**
     * Sets whether end of text has been reached.
     *
     * @param endOfText the end of text flag value
     */
    public void setEndOfText(final boolean endOfText) {
        this.endOfText = endOfText;
    }

    /**
     * Gets the text of the text block.
     *
     * @return the text of the text block
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text of the text block.
     *
     * @param text the text of the text block
     */
    public void setText(final String text) {
        this.text = text;
    }
}
