/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.event.protocol.xml;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.xml.sax.SAXException;

import com.ericsson.apex.model.utilities.ResourceUtils;
import com.ericsson.apex.plugins.event.protocol.xml.jaxb.ObjectFactory;
import com.ericsson.apex.plugins.event.protocol.xml.jaxb.XMLApexEvent;
import com.ericsson.apex.plugins.event.protocol.xml.jaxb.XMLApexEventData;
import com.ericsson.apex.service.engine.event.ApexEvent;
import com.ericsson.apex.service.engine.event.ApexEventException;
import com.ericsson.apex.service.engine.event.ApexEventProtocolConverter;
import com.ericsson.apex.service.engine.event.ApexEventRuntimeException;
import com.ericsson.apex.service.parameters.eventprotocol.EventProtocolParameters;

/**
 * The Class Apex2XMLEventConverter converts {@link ApexEvent} instances into string instances of {@link XMLApexEvent} that are XML representations of Apex
 * events defined in JAXB.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public final class Apex2XMLEventConverter implements ApexEventProtocolConverter {
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(Apex2XMLEventConverter.class);

    private static final String MODEL_SCHEMA_NAME = "xml/apex-event.xsd";

    // XML Unmarshaller and marshaller and object factory for events
    private Unmarshaller unmarshaller;
    private Marshaller marshaller;
    private ObjectFactory objectFactory = new ObjectFactory();

    /**
     * Constructor to create the Apex to XML converter.
     *
     * @throws ApexEventException the apex event exception
     */
    public Apex2XMLEventConverter() throws ApexEventException {
        try {
            final URL schemaURL = ResourceUtils.getURLResource(MODEL_SCHEMA_NAME);
            final Schema apexEventSchema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(schemaURL);

            final JAXBContext jaxbContext = JAXBContext.newInstance(XMLApexEvent.class);

            // Set up the unmarshaller to carry out validation
            unmarshaller = jaxbContext.createUnmarshaller();
            unmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
            unmarshaller.setSchema(apexEventSchema);

            // Set up the marshaller
            marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setSchema(apexEventSchema);
        }
        catch (JAXBException | SAXException e) {
            LOGGER.error("Unable to set up marshalling and unmarshalling for XML events", e);
            throw new ApexEventException("Unable to set up marshalling and unmarshalling for XML events", e);
        }
    }

    /* (non-Javadoc)
     * @see com.ericsson.apex.service.engine.event.ApexEventProtocolConverter#init(com.ericsson.apex.service.parameters.eventprotocol.EventProtocolParameters)
     */
    @Override
    public void init(final EventProtocolParameters parameters) {
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.service.engine.event.ApexEventConverter#toApexEvent(java.lang.String, java.lang.Object)
     */
    @Override
    public List<ApexEvent> toApexEvent(final String eventName, final Object eventObject) throws ApexEventException {
        // Check the XML event
        if (eventObject == null) {
            LOGGER.warn("event processing failed, XML event is null");
            throw new ApexEventException("event processing failed, XML event is null");
        }

        // Cast the event to a string, if our conversion is correctly configured, this cast should always work
        String xmlEventString = null;
        try {
            xmlEventString = (String) eventObject;
        }
        catch (Exception e) {
            String errorMessage = "error converting event \"" + eventObject + "\" to a string";
            LOGGER.debug(errorMessage, e);
            throw new ApexEventRuntimeException(errorMessage, e);
        }

        // The XML event
        XMLApexEvent xmlApexEvent = null;

        // Use JAXB to read and verify the event from the XML string
        try {
            final StreamSource source = new StreamSource(new ByteArrayInputStream(xmlEventString.getBytes()));
            final JAXBElement<XMLApexEvent> rootElement = unmarshaller.unmarshal(source, XMLApexEvent.class);
            xmlApexEvent = rootElement.getValue();
        }
        catch (final JAXBException e) {
            LOGGER.warn("Unable to unmarshal Apex XML event\n" + xmlEventString, e);
            throw new ApexEventException("Unable to unmarshal Apex XML event\n" + xmlEventString, e);
        }

        // Create the Apex event
        final ApexEvent apexEvent = new ApexEvent(xmlApexEvent.getName(), xmlApexEvent.getVersion(), xmlApexEvent.getNameSpace(), xmlApexEvent.getSource(),
                xmlApexEvent.getTarget());

        // Set the data on the apex event
        for (final XMLApexEventData xmlData : xmlApexEvent.getData()) {
            apexEvent.put(xmlData.getKey(), xmlData.getValue());
        }

        // Return the event in a single element
        ArrayList<ApexEvent> eventList = new ArrayList<ApexEvent>();
        eventList.add(apexEvent);
        return eventList;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.service.engine.event.ApexEventConverter#fromApexEvent(com.ericsson.apex.service.engine.event.ApexEvent)
     */
    @Override
    public String fromApexEvent(final ApexEvent apexEvent) throws ApexEventException {
        // Check the Apex event
        if (apexEvent == null) {
            LOGGER.warn("event processing failed, Apex event is null");
            throw new ApexEventException("event processing failed, Apex event is null");
        }

        // Get the Apex event data
        final List<XMLApexEventData> xmlDataList = new ArrayList<XMLApexEventData>();

        try {
            for (final Entry<String, Object> apexDataEntry : apexEvent.entrySet()) {
                // Add an XML event data item
                if (apexDataEntry.getValue() != null) {
                    xmlDataList.add(new XMLApexEventData(apexDataEntry.getKey(), apexDataEntry.getValue().toString()));
                }
                else {
                    xmlDataList.add(new XMLApexEventData(apexDataEntry.getKey(), ""));
                }
            }
        }
        catch (final Exception e) {
            LOGGER.warn("Unable to transfer Apex event data to XML\n" + apexEvent, e);
            throw new ApexEventException("Unable to transfer Apex event data to XML\n" + apexEvent, e);
        }

        // Create the XML event
        final XMLApexEvent xmlApexEvent = new XMLApexEvent(apexEvent.getName(), apexEvent.getVersion(), apexEvent.getNameSpace(), apexEvent.getSource(),
                apexEvent.getTarget(), xmlDataList);

        // Write the event into a DOM document
        try {
            // Marshal the event into XML
            final StringWriter writer = new StringWriter();
            marshaller.marshal(objectFactory.createXmlApexEvent(xmlApexEvent), writer);

            // Return the event as XML in a string
            return writer.toString();
        }
        catch (final JAXBException e) {
            LOGGER.warn("Unable to unmarshal Apex event to XML\n" + apexEvent, e);
            throw new ApexEventException("Unable to unmarshal Apex event to XML\n" + apexEvent, e);
        }
    }
}
