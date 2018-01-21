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
 * The Field Bean.
 */
@XmlType
public class BeanField extends BeanKeyRef {

    private boolean optional = true;
    private String localName = null;

    /**
     * Gets the local name for this field.
     *
     * @return the local name for this field.
     */
    public String getLocalName() {
        return localName;
    }

    /**
     * Gets the optional flag.
     *
     * @return the optional flag
     */
    public boolean getOptional() {
        return optional;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.apex.auth.rest.editor.bean.Bean_KeyRef#toString()
     */
    @Override
    public String toString() {
        return "Field [localName=" + getLocalName() + ", name=" + getName() + ", version=" + getVersion() + ", optional=" + getOptional() + "]";
    }

}
