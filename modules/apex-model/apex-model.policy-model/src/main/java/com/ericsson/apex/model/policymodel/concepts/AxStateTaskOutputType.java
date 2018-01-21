/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.policymodel.concepts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * This enumeration defines the type of state output selection that is defined for a task in a state. The {@link AxStateTaskReference} instance for each task
 * uses this enumeration to decide what type of output selection to use when a task has completed execution.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AxStateTaskOutputType", namespace = "http://www.ericsson.com/apex")

public enum AxStateTaskOutputType {
    /** The state output selection for the task has not been defined. */
    UNDEFINED,
    /** Direct state output selection has been selected, the task will select a {@link AxStateOutput} directly. */
    DIRECT,
    /**
     * Logic state output selection has been selected, the task will select a {@link AxStateOutput} using logic defined in a {@link AxStateFinalizerLogic}
     * instance.
     */
    LOGIC
}
