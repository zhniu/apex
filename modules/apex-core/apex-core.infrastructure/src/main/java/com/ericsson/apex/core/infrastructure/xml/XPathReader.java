/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.infrastructure.xml;

import java.io.InputStream;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.w3c.dom.Document;

/**
 * A generic class for applying the XPATH queries on XML files.
 *
 * @author Sajeevan Achuthan (sajeevan.achuthan@ericsson.com)
 */
public class XPathReader {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(XPathReader.class);

    private String xmlFileName = null;
    private InputStream xmlStream = null;
    private Document xmlDocument;
    private XPath xPath;

    /**
     * Construct Reader for the file passed in.
     *
     * @param xmlFileName the xml file name
     */
    public XPathReader(final String xmlFileName) {
        this.xmlFileName = xmlFileName;
        init();
    }

    /**
     * Construct Reader for the stream passed in.
     *
     * @param xmlStream a stream of XML
     */
    public XPathReader(final InputStream xmlStream) {
        this.xmlStream = xmlStream;
        init();
    }

    /**
     * Initialise the x-path reader.
     */
    private void init() {
        try {
            LOGGER.info("Initializing XPath reader");

            // Check if this is operating on a file
            if (xmlFileName != null) {
                xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFileName);
            }
            // Check if this is operating on a stream
            else if (xmlStream != null) {
                xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlStream);

            }
            // We have an error
            else {
                LOGGER.error("XPath reader not initialized with either a file or a stream");
                return;
            }

            xPath = XPathFactory.newInstance().newXPath();
            LOGGER.info("Initialized XPath reader");
        }
        catch (final Exception ex) {
            LOGGER.error("Error parsing XML file/stream from XPath reading, reason :\n" + ex.getMessage());
        }
    }

    /**
     * Read items from the file using xpath.
     *
     * @param expression x-path expression
     * @param returnType XML node Set
     * @return last node collected
     */
    public Object read(final String expression, final QName returnType) {
        try {
            final XPathExpression xPathExpression = xPath.compile(expression);
            return xPathExpression.evaluate(xmlDocument, returnType);
        }
        catch (final XPathExpressionException ex) {
            LOGGER.error("Failed to read XML file for XPath processing, reason:\n" + ex.getMessage());
            return null;
        }
    }
}
