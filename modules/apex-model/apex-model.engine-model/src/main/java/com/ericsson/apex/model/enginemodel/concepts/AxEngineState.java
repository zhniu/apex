/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.enginemodel.concepts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * This enumeration indicates the execution state of an Apex engine.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "AxEngineState",
        namespace = "http://www.ericsson.com/apex")

public enum AxEngineState {
    /** The state of the engine is not known. */
    UNDEFINED,
    /** The engine is stopped. */
    STOPPED,
    /** The engine is running and is waiting to execute a policy. */
    READY,
    /** The engine is running and is executing a policy. */
    EXECUTING,
    /** The engine has been ordered to stop and is stoping. */
    STOPPING;
}
