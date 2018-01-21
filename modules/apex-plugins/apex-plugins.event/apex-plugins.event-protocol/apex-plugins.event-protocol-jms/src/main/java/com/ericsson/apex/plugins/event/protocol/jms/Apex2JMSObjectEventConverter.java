/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.event.protocol.jms;

import java.util.ArrayList;
import java.util.List;

import javax.jms.ObjectMessage;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.service.engine.event.ApexEvent;
import com.ericsson.apex.service.engine.event.ApexEventException;
import com.ericsson.apex.service.engine.event.ApexEventProtocolConverter;
import com.ericsson.apex.service.engine.event.ApexEventRuntimeException;
import com.ericsson.apex.service.parameters.eventprotocol.EventProtocolParameters;

/**
 * The Class Apex2JMSObjectEventConverter converts {@link ApexEvent} instances into string instances of {@link javax.jms.ObjectMessage} message events for JMS.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public final class Apex2JMSObjectEventConverter implements ApexEventProtocolConverter {
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(Apex2JMSObjectEventConverter.class);

    // JMS event protocol parameters on the consumer (JMS->Apex) sides
    private JMSObjectEventProtocolParameters eventProtocolParameters = null;

    /**
     * Constructor to create the Apex to JMS Object converter.
     *
     * @throws ApexEventException the apex event exception
     */
    public Apex2JMSObjectEventConverter() throws ApexEventException {
    }

    @Override
    public void init(final EventProtocolParameters parameters) {
        // Check if properties have been set for JMS object event conversion as a consumer. They may not be set because JMS may not be in use
        // on both sides of Apex
        if (!(parameters instanceof JMSObjectEventProtocolParameters)) {
            String errormessage = "specified Event Protocol Parameters properties of type \"" + parameters.getClass().getCanonicalName()
                    + "\" are not applicable to a " + Apex2JMSObjectEventConverter.class.getName() + " converter";
            LOGGER.error(errormessage);
        }
        else {
            this.eventProtocolParameters = (JMSObjectEventProtocolParameters) parameters;
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.service.engine.event.ApexEventConverter#toApexEvent(java.lang.Object)
     */
    @Override
    public List<ApexEvent> toApexEvent(final Object eventObject) throws ApexEventException {
        // Check if this is an ObjectMessage from JMS
        if (!(eventObject instanceof ObjectMessage)) {
            String errorMessage = "message \"" + eventObject + "\" received from JMS is not an instance of \"" + ObjectMessage.class.getCanonicalName() + "\"";
            LOGGER.warn(errorMessage);
            throw new ApexEventRuntimeException(errorMessage);
        }

        // Get the object from the object message
        ObjectMessage objectMessage = (ObjectMessage) eventObject;
        Object jmsIncomingObject;
        try {
            jmsIncomingObject = objectMessage.getObject();
        }
        catch (Exception e) {
            String errorMessage = "object contained in message \"" + eventObject + "\" received from JMS could not be retrieved as a Java object";
            LOGGER.debug(errorMessage, e);
            throw new ApexEventRuntimeException(errorMessage, e);
        }

        // Check that the consumer parameters for JMS->Apex messaging have been set
        if (eventProtocolParameters == null) {
            String errorMessage = "consumer parameters for JMS events consumed by Apex are not set in the Apex configuration for this engine";
            LOGGER.debug(errorMessage);
            throw new ApexEventRuntimeException(errorMessage);
        }

        // Create the Apex event
        // @formatter:off
        final ApexEvent apexEvent = new ApexEvent(
                jmsIncomingObject.getClass().getSimpleName() + eventProtocolParameters.getIncomingEventSuffix(),
                eventProtocolParameters.getIncomingEventVersion(),
                jmsIncomingObject.toString().getClass().getPackage().getName(),
                eventProtocolParameters.getIncomingEventSource(),
                eventProtocolParameters.getIncomingEventTarget());
        // @formattter:on

        // Set the data on the apex event as the incoming object
        apexEvent.put(jmsIncomingObject.getClass().getSimpleName(), jmsIncomingObject);

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
    public Object fromApexEvent(final ApexEvent apexEvent) throws ApexEventException {
        // Check the Apex event
        if (apexEvent == null) {
            LOGGER.warn("event processing failed, Apex event is null");
            throw new ApexEventException("event processing failed, Apex event is null");
        }

        // Check that the Apex event has a single parameter
        if (apexEvent.size() != 1) {
            String errorMessage = "event processing failed, Apex event must have one and only one parameter for JMS Object handling";
            LOGGER.warn(errorMessage);
            throw new ApexEventException(errorMessage);
        }

        // Return the single object from the Apex event message
        return apexEvent.values().iterator().next();
    }
}
