/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.engine.monitoring;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxConcept;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;
import com.ericsson.apex.model.eventmodel.concepts.AxField;

/**
 * This class is used to monitor event parameter gets and sets.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class EventMonitor {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(EventMonitor.class);

    /**
     * Monitor get on an event parameter.
     *
     * @param eventParameter The event parameter to monitor
     * @param value the value of the event parameter
     * @param userArtifactStack the keys of the artifacts using the event at the moment
     */
    public void monitorGet(final AxField eventParameter, final Object value, final AxConcept[] userArtifactStack) {
        LOGGER.trace(monitor("GET", userArtifactStack, eventParameter, value));
    }

    /**
     * Monitor set on an event parameter.
     *
     * @param eventParameter The event parameter to monitor
     * @param value the value of the event parameter
     * @param userArtifactStack the keys of the artifacts using the event at the moment
     */
    public void monitorSet(final AxField eventParameter, final Object value, final AxConcept[] userArtifactStack) {
        LOGGER.trace(monitor("SET", userArtifactStack, eventParameter, value));
    }

    /**
     * Monitor remove on an event parameter.
     *
     * @param eventParameter The event parameter to monitor
     * @param removedValue the value of the event parameter
     * @param userArtifactStack the keys of the artifacts using the event at the moment
     */
    public void monitorRemove(final AxField eventParameter, final Object removedValue, final AxConcept[] userArtifactStack) {
        LOGGER.trace(monitor("REMOVE", userArtifactStack, eventParameter, removedValue));
    }

    /**
     * Monitor the user artifact stack.
     *
     * @param preamble the preamble
     * @param userArtifactStack The user stack to print
     * @param eventParameter The event parameter that we are monitoring
     * @param value The value of the target object
     * @return the string
     */
    private String monitor(final String preamble, final AxConcept[] userArtifactStack, final AxField eventParameter, final Object value) {
        final StringBuilder builder = new StringBuilder();

        builder.append(preamble);
        builder.append(",[");

        if (userArtifactStack != null) {
            boolean first = true;
            for (final AxConcept stackKey : userArtifactStack) {
                if (first) {
                    first = false;
                }
                else {
                    builder.append(',');
                }
                if (stackKey instanceof AxArtifactKey) {
                    builder.append(((AxArtifactKey) stackKey).getID());
                }
                else if (stackKey instanceof AxReferenceKey) {
                    builder.append(((AxReferenceKey) stackKey).getID());
                }
                else {
                    builder.append(stackKey.toString());
                }
            }
        }
        builder.append("],");

        builder.append(eventParameter.toString());
        builder.append("=");
        builder.append(value);

        return builder.toString();
    }
}
