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
 * The State Task Reference Bean.
 */
@XmlType
public class BeanStateTaskRef extends BeanBase {

    private BeanKeyRef task = null;
    private String outputType = null, outputName = null;

    /**
     * Gets the task.
     *
     * @return the task
     */
    public BeanKeyRef getTask() {
        return task;
    }

    /**
     * Gets the output type.
     *
     * @return the output type
     */
    public String getOutputType() {
        return outputType;
    }

    /**
     * Gets the output name.
     *
     * @return the output name
     */
    public String getOutputName() {
        return outputName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "StateTaskRef [task=" + task + ", outputType=" + outputType + ", outputName=" + outputName + "]";
    }

}
