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
 * The Event Bean.
 */
@XmlType
public class BeanEvent extends BeanBase {

    private String name = null, version = null, nameSpace = null, source = null, target = null, uuid = null, description = null;
    private Map<String, BeanField> parameters = null;

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
     * Gets the name space.
     *
     * @return the name space
     */
    public String getNameSpace() {
        return nameSpace;
    }

    /**
     * Gets the source.
     *
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * Gets the target.
     *
     * @return the target
     */
    public String getTarget() {
        return target;
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
     * Gets the parameters.
     *
     * @return the parameters
     */
    public Map<String, BeanField> getParameters() {
        return parameters;
    }

    /**
     * Gets the parameter.
     *
     * @param p the p
     * @return the parameter
     */
    public BeanField getParameter(final String p) {
        if (parameters != null) {
            return parameters.get(p);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Event [name=" + name + ", version=" + version + ", nameSpace=" + nameSpace + ", source=" + source + ", target=" + target + ", uuid=" + uuid
                + ", description=" + description + ", parameters=" + getParameters() + "]";
    }

}
