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

import java.util.Map;

import javax.xml.bind.annotation.XmlType;

/**
 * The Policy Bean.
 */
@XmlType
public class BeanPolicy extends BeanBase {

    private String name = null, version = null, uuid = null, description = null, firstState = null, template = null;
    private Map<String, BeanState> states = null;

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
     * Gets the first state.
     *
     * @return the first state
     */
    public String getFirstState() {
        return firstState;
    }

    /**
     * Gets the template.
     *
     * @return the template
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Gets the states.
     *
     * @return the states
     */
    public Map<String, BeanState> getStates() {
        return states;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Policy [name=" + name + ", version=" + version + ", uuid=" + uuid + ", description=" + description + ", firstState=" + firstState
                + ", template=" + template + ", states=" + states + "]";
    }

}
