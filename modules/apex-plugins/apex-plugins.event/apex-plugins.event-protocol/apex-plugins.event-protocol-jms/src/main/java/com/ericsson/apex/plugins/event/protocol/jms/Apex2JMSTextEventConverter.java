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

import java.util.List;

import javax.jms.TextMessage;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.service.engine.event.ApexEvent;
import com.ericsson.apex.service.engine.event.ApexEventException;
import com.ericsson.apex.service.engine.event.ApexEventRuntimeException;
import com.ericsson.apex.service.engine.event.impl.jsonprotocolplugin.Apex2JSONEventConverter;

/**
 * The Class Apex2JMSTextEventConverter converts {@link ApexEvent} instances into string instances of {@link javax.jms.TextMessage} message events for JMS. It
 * is a proxy for the built in {@link com.ericsson.apex.service.engine.event.impl.jsonprotocolplugin.Apex2JSONEventConverter} plugin.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public final class Apex2JMSTextEventConverter extends Apex2JSONEventConverter {
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(Apex2JMSTextEventConverter.class);

    /**
     * Constructor to create the Apex to JMS Object converter.
     *
     * @throws ApexEventException the apex event exception
     */
    public Apex2JMSTextEventConverter() throws ApexEventException {
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.service.engine.event.ApexEventConverter#toApexEvent(java.lang.Object)
     */
    @Override
    public List<ApexEvent> toApexEvent(final Object eventObject) throws ApexEventException {
        // Check if this is an TextMessage from JMS
        if (!(eventObject instanceof TextMessage)) {
            String errorMessage = "message \"" + eventObject + "\" received from JMS is not an instance of \"" + TextMessage.class.getCanonicalName() + "\"";
            LOGGER.debug(errorMessage);
            throw new ApexEventRuntimeException(errorMessage);
        }

        // Get the string from the object message
        TextMessage textMessage = (TextMessage) eventObject;
        String jmsString;
        try {
            jmsString = textMessage.getText();
        }
        catch (Exception e) {
            String errorMessage = "object contained in message \"" + eventObject + "\" received from JMS could not be retrieved as a Java String";
            LOGGER.debug(errorMessage, e);
            throw new ApexEventRuntimeException(errorMessage, e);
        }

        // Use the generic JSON plugin from here
        return super.toApexEvent(jmsString);
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

        // Return the Apex event as a string object
        return super.fromApexEvent(apexEvent);
    }
}
