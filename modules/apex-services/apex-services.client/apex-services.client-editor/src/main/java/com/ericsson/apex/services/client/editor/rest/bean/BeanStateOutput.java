/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.services.client.editor.rest.bean;

import javax.xml.bind.annotation.XmlType;

/**
 * The StateOutput Bean.
 */
@XmlType
public class BeanStateOutput extends BeanBase {

    private BeanKeyRef event = null;
    private String nextState = null;

    /**
     * Gets the event.
     *
     * @return the event
     */
    public BeanKeyRef getEvent() {
        return event;
    }

    /**
     * Gets the next state.
     *
     * @return the next state
     */
    public String getNextState() {
        return nextState;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "StateOutput [event=" + event + ", nextState=" + nextState + "]";
    }

}
