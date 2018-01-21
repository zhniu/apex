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

import java.util.Arrays;
import java.util.Map;

import javax.xml.bind.annotation.XmlType;

/**
 * The Task Bean.
 */
@XmlType
public class BeanTask extends BeanBase {

    private String name = null, version = null, uuid = null, description = null;
    private BeanLogic taskLogic = null;
    private Map<String, BeanField> inputFields = null;
    private Map<String, BeanField> outputFields = null;
    private Map<String, BeanTaskParameter> parameters = null;
    private BeanKeyRef[] contexts = null;

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the version.
     *
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Gets the uuid.
     *
     * @return the uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the task logic.
     *
     * @return the task logic
     */
    public BeanLogic getTaskLogic() {
        return taskLogic;
    }

    /**
     * Gets the input fields.
     *
     * @return the input fields
     */
    public Map<String, BeanField> getInputFields() {
        return inputFields;
    }

    /**
     * Gets the output fields.
     *
     * @return the output fields
     */
    public Map<String, BeanField> getOutputFields() {
        return outputFields;
    }

    /**
     * Gets the parameters.
     *
     * @return the parameters
     */
    public Map<String, BeanTaskParameter> getParameters() {
        return parameters;
    }

    /**
     * Gets the contexts.
     *
     * @return the contexts
     */
    public BeanKeyRef[] getContexts() {
        return contexts;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "BeanTask [name=" + name + ", version=" + version + ", uuid=" + uuid + ", description=" + description + ", taskLogic=" + taskLogic
                + ", inputFields=" + inputFields + ", outputFields=" + outputFields + ", parameters=" + parameters + ", contexts=" + Arrays.toString(contexts)
                + "]";
    }
}
